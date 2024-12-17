package org.luckyshot.Facades;

import org.luckyshot.Models.*;
import org.luckyshot.Models.Consumables.Consumable;
import org.luckyshot.Models.Consumables.ConsumableInterface;
import org.luckyshot.Models.Powerups.Powerup;
import org.luckyshot.Models.Powerups.PowerupInterface;
import org.luckyshot.Models.StateEffects.StateEffect;
import org.luckyshot.Models.StateEffects.StateEffectInterface;
import org.luckyshot.Views.SinglePlayerGameView;
import org.luckyshot.Views.View;

import java.lang.reflect.Method;
import java.util.*;
import java.util.stream.Collectors;

public class SinglePlayerGameFacade {
    private static SinglePlayerGameFacade instance;
    private SinglePlayerGame singlePlayerGame;
    private final SinglePlayerGameView singlePlayerGameView;

    private SinglePlayerGameFacade() {
        singlePlayerGameView = new SinglePlayerGameView();
    }

    public static SinglePlayerGameFacade getInstance() {
        if(instance == null) {
            instance = new SinglePlayerGameFacade();
        }
        return instance;
    }

    public void start(User user) {
        HumanPlayer humanPlayer = new HumanPlayer(user.getId(), user.getPowerups());
        BotPlayer botPlayer = new BotPlayer();

        user.setPlayer(humanPlayer);

        this.singlePlayerGame = new SinglePlayerGame(user.getPlayer(), botPlayer);

        boolean gameEnded = false;
        int roundNumber = 1;
        Player[] players = {singlePlayerGame.getHumanPlayer(), singlePlayerGame.getBot()};

        while(!gameEnded) {
            //Inizio di un round
            Round round = new Round(roundNumber, getRandomStateEffect());
            Gun.getInstance().clearBullets();

            this.singlePlayerGame.setRound(round);
            Random rnd = new Random();
            int randomLives = rnd.nextInt(2, 5);
            this.singlePlayerGame.getHumanPlayer().setLives(randomLives);
            this.singlePlayerGame.getBot().setLives(randomLives);

            boolean roundEnded = false;
            int turn = 0;

            while(!roundEnded) {
                //Inizio di un turno
                Player currentPlayer = players[turn];

                Turn currentTurn = new Turn(currentPlayer);
                this.singlePlayerGame.getRound().setTurn(currentTurn);

                processTurn();

                // Condizione di fine round
                if(botPlayer.getLives() <= 0) {
                    roundEnded = true;
                    roundNumber += 1;
                }
                // Condizione di fine gioco
                if(humanPlayer.getLives() <= 0) {
                    roundEnded = true;
                    gameEnded = true;
                }

                turn = (turn + 1) % 2;
            }

            showGameState();
            singlePlayerGameView.showError("Round ended");

            // Condizione di fine gioco
            if(singlePlayerGame.getRound().getRoundNumber() >= 3 && (humanPlayer.getLives() <= 0 || botPlayer.getLives() <= 0)) {
                gameEnded = true;
            }
        }
        showGameState();
        singlePlayerGameView.showError("Game ended");
    }

    public void showGameState() {
        HashMap<String, Object> objectStateMap = singlePlayerGame.getStateMap();
        HashMap<String, String> stateMap = new HashMap<>();

        // To show players lives on view
        stateMap.put("botLives", Integer.toString(((BotPlayer)objectStateMap.get("bot")).getLives()));
        stateMap.put("humanPlayerLives", Integer.toString(((HumanPlayer)objectStateMap.get("humanPlayer")).getLives()));

        // To show bot consumables on view
        ArrayList<String> botConsumables = (((BotPlayer) objectStateMap.get("bot")).getConsumables());
        botConsumables.forEach(consumable -> {
            if(!stateMap.containsKey("bot" + consumable)) {
                stateMap.put("bot" + consumable, "0");
            }
            stateMap.put("bot" + consumable, Integer.toString(Integer.parseInt(stateMap.get("bot" + consumable)) + 1));
        });

        // To show human consumables on view
        ArrayList<String> humanConsumables = (((HumanPlayer) objectStateMap.get("humanPlayer")).getConsumables());
        humanConsumables.forEach(consumable -> {
            if(!stateMap.containsKey("human" + consumable)) {
                stateMap.put("human" + consumable, "0");
            }
            stateMap.put("human" + consumable, Integer.toString(Integer.parseInt(stateMap.get("human" + consumable)) + 1));
        });

        // To show human powerups on view
        HashMap<Powerup, Integer> powerups = (((HumanPlayer) objectStateMap.get("humanPlayer")).getPowerups());
        for(Powerup powerup : powerups.keySet()) {
            stateMap.put(powerup.toString(), Integer.toString(powerups.get(powerup)));
        }

        // To show round number on view
        Round round = (Round)objectStateMap.get("round");
        stateMap.put("roundNumber", Integer.toString(round.getRoundNumber()));

        // To show state effect on view
        StateEffect currentStateEffect = round.getStateEffect();
        if(currentStateEffect != null) {
            stateMap.put("stateEffect", round.getStateEffect().toString());
        } else {
            stateMap.put("stateEffect", "none");
        }

        // To show current player on view
        stateMap.put("turn", singlePlayerGame.getRound().getTurn().getPlayer().getClass().getSimpleName());

        // To show powerups effects on view
        stateMap.put("isBotPoisoned", Boolean.toString(singlePlayerGame.getBot().isPoisoned()));
        stateMap.put("isHumanPoisoned", Boolean.toString(singlePlayerGame.getHumanPlayer().isPoisoned()));
        stateMap.put("isHumanShielded", Boolean.toString(singlePlayerGame.getHumanPlayer().isShieldActive()));

        singlePlayerGameView.showGame(stateMap);
    }

    public String getPlayerInput() {
        if(this.singlePlayerGame.getRound().getTurn().getPlayer().getClass() == HumanPlayer.class) {
            return singlePlayerGameView.getUserInput();
        }

        try{
            Thread.sleep(1000);
        } catch (Exception e) {
            singlePlayerGameView.showError("sleep error");
        }
        return this.singlePlayerGame.getBot().getInput();
    }

    public void processTurn() {
        Gun gun = Gun.getInstance();
        boolean changeTurn = false;
        while(!changeTurn) {
            showGameState();
            // Rimuovo lo scudo al giocatore corrente
            singlePlayerGame.getRound().getTurn().getPlayer().setShieldActive(false);

            // Se la pistola è vuota, assegno i consumabili e la ricarico
            if(gun.isEmpty()) {
                drawConsumables();
                showGameState();
                loadGun();
                showGameState();
            }

            //Prendo l'input del giocatore corrente
            String[] userInput = getPlayerInput().split(" ");
            while(userInput.length != 2) {
                singlePlayerGameView.showError("Wrong syntax, command not recognized");
                userInput = getPlayerInput().split(" ");
            }
            String command = userInput[0].toLowerCase(); // use, shoot
            String target = userInput[1].toLowerCase(); // 1, 2, ..., a, b, ...

            if(command.equals("use")) {
                useCommand(target);
                if(singlePlayerGame.getHumanPlayer().getLives() <= 0 || singlePlayerGame.getBot().getLives() <= 0) {
                    changeTurn = true;
                }
            } else if(command.equals("shoot")) {
                changeTurn = shootingPhase(target);

                if(changeTurn) {
                    if(singlePlayerGame.getRound().getTurn().getPlayer().isPoisoned()) {
                        singlePlayerGame.getRound().getTurn().getPlayer().setLives(singlePlayerGame.getRound().getTurn().getPlayer().getLives() - 1);
                        singlePlayerGame.getRound().getTurn().getPlayer().setPoisoned(false);
                    }
                }
            } else {
                singlePlayerGameView.showError("Command not recognized");
            }
        }
        showGameState();
    }

    public static boolean isInteger(String s) {
        try {
            Integer.parseInt(s);
        } catch(NumberFormatException | NullPointerException e) {
            return false;
        }
        return true;
    }

    private void useCommand(String target) {
        if(isInteger(target)) {
            usePowerup(Integer.parseInt(target)); //Aggiungere controllo numero
        } else {
            //useConsumable(target);
        }
    }

    public StateEffect getRandomStateEffect() {
        ArrayList<Class<? extends StateEffect>> stateEffects = StateEffectInterface.getStateEffectClassList();
        Random rand = new Random();
        StateEffect stateEffect = null;
        try {
            Method method = Class.forName(stateEffects.get(rand.nextInt(0, stateEffects.size())).getName()).getMethod("getInstance");
            Object obj = method.invoke(null);
            stateEffect = (StateEffect) obj;
        } catch (Exception e) {
            singlePlayerGameView.showError("Could not get state effect");
        }

        return stateEffect;
    }

    private String getRandomConsumable() {
        HashMap<String, Integer> consumableProb = new HashMap<>();

        for(Class<? extends Consumable> c : ConsumableInterface.getConsumableClassList()) {
            try {
                Class<?> cls = Class.forName(c.getName());
                Method m = cls.getMethod("getInstance");
                Object consumable = m.invoke(null);
                consumableProb.put(c.getSimpleName(), ((Consumable) consumable).getProbability());
            } catch (Exception e) {
                singlePlayerGameView.showError("Error getting consumable probability");
            }
        }

        Random rand = new Random();
        String consumable = "";
        int tries = 0;
        int maxTries = 100;
        boolean found = false;
        while(!found && tries < maxTries) {
            ArrayList<String> consumableList = new ArrayList<>(consumableProb.keySet());
            String randomConsumable = consumableList.get(rand.nextInt(consumableList.size()));
            int r = rand.nextInt(100);
            if (r < consumableProb.get(randomConsumable) || tries == maxTries - 1) {
                found = true;
                consumable = randomConsumable;
            }
            tries++;
        }

        return consumable;
    }

    public void drawConsumables() {
        int maxConsumablesNumber = 8;
        Random rand = new Random();
        int r = rand.nextInt(2, 6);
        int numberOfConsumablesHumanPlayer = Math.min(r, maxConsumablesNumber - singlePlayerGame.getHumanPlayer().getConsumablesNumber());
        int numberOfConsumablesBotPlayer = Math.min(r, maxConsumablesNumber - singlePlayerGame.getBot().getConsumablesNumber());

        ArrayList<String> consumables = singlePlayerGame.getHumanPlayer().getConsumables();
        for(int i = 0; i < numberOfConsumablesHumanPlayer; i++) {
            String randomConsumable = getRandomConsumable();
            consumables.add(randomConsumable);
        }
        singlePlayerGame.getHumanPlayer().setConsumables(consumables);

        consumables = singlePlayerGame.getBot().getConsumables();
        for(int i = 0; i < numberOfConsumablesBotPlayer; i++) {
            String randomConsumable = getRandomConsumable();
            consumables.add(randomConsumable);
        }
        singlePlayerGame.getBot().setConsumables(consumables);
    }

    public void loadGun() {
        ArrayList<Bullet> b = Gun.getInstance().generateBulletSequence();
        showBullets(b);
        Gun.getInstance().setBullets(b);
    }

    public void showBullets(ArrayList<Bullet> bullets) {
        ArrayList<String> b = new ArrayList<>();
        for (Bullet bullet : bullets) {
            b.add(Integer.toString(bullet.getType()));
        }
        singlePlayerGameView.showBullets(b);
    }

    public void usePowerup(int target) {
        String powerupName = PowerupInterface.getPowerupClassList().get(target - 1).getName();
        try {
            Method method = Class.forName(powerupName).getMethod("getInstance");
            Object obj = method.invoke(null);
            if(singlePlayerGame.getHumanPlayer().getPowerups().get((Powerup) obj) != 0) {
                ((Powerup) obj).use(singlePlayerGame);
                singlePlayerGame.getHumanPlayer().getPowerups().put((Powerup) obj, singlePlayerGame.getHumanPlayer().getPowerups().get(obj) - 1);
                singlePlayerGameView.showPowerupActivation(((Powerup)obj).toString());
            } else {
                singlePlayerGameView.showError("No such powerup");
            }
        } catch (Exception e) {
            singlePlayerGameView.showError("Could not use powerup");
        }
    }

    public boolean shootingPhase(String target) {
        boolean changeTurn = false;
        boolean shot = false;

        Player currentPlayer = singlePlayerGame.getRound().getTurn().getPlayer();
        Bullet currentBullet = Gun.getInstance().popBullet();
        // 1 = self
        // 2 = other
        if(target.equals("1")) {
            if(currentBullet.getType() == 1) {
                changeTurn = true;
                if(!currentPlayer.isShieldActive()) {
                    currentPlayer.setLives(currentPlayer.getLives() - 1);
                    if(singlePlayerGame.getRound().getTurn().isBulletPoisoned()) {
                        currentPlayer.setPoisoned(true);
                    }
                } else {
                    currentPlayer.setShieldActive(false);
                }
            }
            shot = true;
        } else if(target.equals("2")) {
            if(currentBullet.getType() == 1) {
                Player otherPlayer = null;
                if(currentPlayer.getClass() == HumanPlayer.class) {
                    otherPlayer = singlePlayerGame.getBot();
                } else {
                    otherPlayer = singlePlayerGame.getHumanPlayer();
                }

                if(!otherPlayer.isShieldActive()) {
                    otherPlayer.setLives(otherPlayer.getLives() - 1);
                    if(singlePlayerGame.getRound().getTurn().isBulletPoisoned()) {
                        otherPlayer.setPoisoned(true);
                    }
                } else {
                    otherPlayer.setShieldActive(false);
                }
            }
            shot = true;
            changeTurn = true;
        } else {
            singlePlayerGameView.showError("Select 1 or 2");
        }

        if(shot) {
            singlePlayerGame.getRound().getTurn().setBulletPoisoned(false);
            singlePlayerGameView.showShootingResult(currentBullet.getType());
        }
        return changeTurn;
    }
}
