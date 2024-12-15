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
        // QUANDO IL FACADE PARTE DEVE AVVIARE LA PARTITA E TERMINA A PARTITA FINITA
        // DEVE CREARE LA PARTITA, DEVE MOSTRARE IL TAVOLO, DEVE RICEVERE L'INPUT, DEVE AGGIORNARE LA PARTITA, DEVE MOSTRARE IL TAVOLO E COSì VIA

        HumanPlayer humanPlayer = new HumanPlayer(user.getId(), user.getPowerups());
        BotPlayer botPlayer = new BotPlayer();

        user.setPlayer(humanPlayer);

        this.singlePlayerGame = new SinglePlayerGame(user.getPlayer(), botPlayer);

        boolean gameEnded = false;
        boolean roundEnded = false;
        int roundNumber = 1;
        int turn = 0;

        while(!gameEnded) {
            //Inizio di un round
            Round round = new Round(roundNumber, getRandomStateEffect());
            singlePlayerGameView.addMessage("Starting new round...", "slow");
            this.singlePlayerGame.setRound(round);
            Random rnd = new Random();
            int randomLives = rnd.nextInt(2, 5);
            this.singlePlayerGame.getHumanPlayer().setLives(randomLives);
            this.singlePlayerGame.getBot().setLives(randomLives);

            while(!roundEnded) {
                //Inizio di un turno
                Player currentPlayer = null;
                if(turn == 0) {
                    currentPlayer = singlePlayerGame.getHumanPlayer();
                } else if(turn == 1) {
                    currentPlayer = singlePlayerGame.getBot();
                }

                Turn currentTurn = new Turn(currentPlayer);
                this.singlePlayerGame.getRound().setTurn(currentTurn);

                update();

                // DA RIVEDERE LA CONDIZIONE DI FINE ROUND
                if(botPlayer.getLives() <= 0) {
                    roundEnded = true;
                    roundNumber += 1;
                }
                if(humanPlayer.getLives() <= 0) {
                    roundEnded = true;
                    gameEnded = true;
                }

                turn = (turn + 1) % 2;
            }

            // DA RIVEDERE LA CONDIZIONE DI FINE GIOCO
            if(singlePlayerGame.getRound().getRoundNumber() == 3 && (humanPlayer.getLives() <= 0 || botPlayer.getLives() <= 0)) {
                singlePlayerGameView.addMessage("End of game", "fast");
                gameEnded = true;
            }
        }
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
            stateMap.put("stateEffect", round.getStateEffect().getClass().toString());
        } else {
            stateMap.put("stateEffect", "none");
        }

        // To show current player on view
        stateMap.put("turn", singlePlayerGame.getRound().getTurn().getPlayer().getClass().getSimpleName());

        // To show powerups effects on view
        stateMap.put("isBotPoisoned", Boolean.toString(singlePlayerGame.getBot().isPoisoned()));
        stateMap.put("isHumanPoisoned", Boolean.toString(singlePlayerGame.getHumanPlayer().isPoisoned()));
        stateMap.put("isHumanShielded", Boolean.toString(singlePlayerGame.getHumanPlayer().isShieldActive()));

        singlePlayerGameView.showGameState(stateMap);
    }

    public String getUserInput() {
        if(this.singlePlayerGame.getRound().getTurn().getPlayer().getClass() == HumanPlayer.class) {
            return singlePlayerGameView.getUserInput();
        }

        if(this.singlePlayerGame.getRound().getTurn().getPhase() == 3) {
            return this.singlePlayerGame.getBot().getConsumableInput();
        }
        return this.singlePlayerGame.getBot().getShootingInput();
    }

    public void update() {
        Gun gun = Gun.getInstance();
        while(true) {
            singlePlayerGame.getRound().getTurn().getPlayer().setShieldActive(false);
            if(gun.isEmpty()) {
                consumableDrawPhase();
                showGameState();
                gunLoadingPhase();
                showGameState();
            }

            //Prendo l'input del giocatore
            String[] userInput = getUserInput().split(" ");
            String command = userInput[0].toLowerCase(); // use, shoot
            String target = userInput[1].toLowerCase(); // 1, 2, ..., a, b, ...

            if(command.equals("use")) {
                useCommand(target);
            } else if(command.equals("shoot")) {
                boolean changeTurn = shootingPhase(target); // AGGIUNGERE CONTROLLO ERRORE
                if(changeTurn) {
                    if(singlePlayerGame.getRound().getTurn().getPlayer().isPoisoned()) {
                        singlePlayerGame.getRound().getTurn().getPlayer().setLives(singlePlayerGame.getRound().getTurn().getPlayer().getLives() - 1);
                        singlePlayerGame.getRound().getTurn().getPlayer().setPoisoned(false);
                    }
                    break;
                }
                else {
                    singlePlayerGameView.systemError();
                }
            }
            //AGGIUNGERE ELSE

            showGameState();

//            if(this.singlePlayerGame.getRound().getTurn().getPlayer().getClass() == HumanPlayer.class) {
//                boolean ok = false;
//                for(Map.Entry<Powerup, Integer> entry : this.singlePlayerGame.getHumanPlayer().getPowerups().entrySet()){
//                    if(entry.getValue() != 0) {
//                        ok = true;
//                        break;
//                    }
//                }
//                if(ok) {
//                    singlePlayerGameView.addMessage("Powerup use phase", "fast");
//                    singlePlayerGameView.printMessages();
//                    powerupUsePhase();
//                    showGameState();
//                }
//
//                singlePlayerGameView.addMessage("Consumable use phase", "fast");
//                consumableUsePhase();
//                showGameState();
//            }

//            if(this.singlePlayerGame.getRound().getTurn().getPlayer().getClass() == BotPlayer.class) {
//                Random rand = new Random();
//                if(rand.nextInt(100) < 66) {
//                    consumableUsePhase();
//                    showGameState();
//                }
//            }

//            singlePlayerGameView.addMessage("Shooting phase", "fast");
//            boolean changeTurn = shootingPhase();
//            showGameState();
//
        }

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
            powerupUse(Integer.parseInt(target));
        } else {
            //consumableUse();
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
            SinglePlayerGameView view = new SinglePlayerGameView();
            view.systemError();
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
                singlePlayerGameView.systemError();
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

    public void consumableDrawPhase() {
        int maxConsumablesNumber = 8;
        this.singlePlayerGame.getRound().getTurn().setPhase(0);
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

    public void gunLoadingPhase() {
        this.singlePlayerGame.getRound().getTurn().setPhase(1);
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

    public void powerupUse(int target) {
        this.singlePlayerGame.getRound().getTurn().setPhase(2);

        String powerupName = PowerupInterface.getPowerupClassList().get(target - 1).getName();

        try {
            Method method = Class.forName(powerupName).getMethod("getInstance");
            Object obj = method.invoke(null);
            if(singlePlayerGame.getHumanPlayer().getPowerups().get((Powerup) obj) != 0) {
                ((Powerup) obj).use(singlePlayerGame);
                singlePlayerGame.getHumanPlayer().getPowerups().put((Powerup) obj, singlePlayerGame.getHumanPlayer().getPowerups().get(obj) - 1);
                singlePlayerGameView.showPowerupActivation(((Powerup)obj).toString());
            } else {
                //VA MESSO IL MESSAGGIO DI ERRORE DI MANCANZA DI POWERUP

            }
        } catch (Exception e) {
            singlePlayerGameView.systemError();
        }

    }
//
//    public void consumableUsePhase() {
//        this.singlePlayerGame.getRound().getTurn().setPhase(3);
//        //AGGIUNGERE CONTROLLO CONSUMABILI
//        int userInput = getUserInput();
//    }

    public boolean shootingPhase(String target) {
        //this.singlePlayerGame.getRound().getTurn().setPhase(4);
        boolean changeTurn = true;

        Player currentPlayer = singlePlayerGame.getRound().getTurn().getPlayer();
        Bullet currentBullet = Gun.getInstance().popBullet();
        // 1 = self
        // 2 = other
        if(target.equals("1")) {
            if(currentBullet.getType() == 0) {
                changeTurn = false;
                singlePlayerGame.getRound().getTurn().setBulletPoisoned(false);
            } else {
                if(!currentPlayer.isShieldActive()) {
                    currentPlayer.setLives(currentPlayer.getLives() - 1);
                    if(singlePlayerGame.getRound().getTurn().isBulletPoisoned()) {
                        currentPlayer.setPoisoned(true);
                    }
                } else {
                    currentPlayer.setShieldActive(false);
                }
            }
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
        } else {
            singlePlayerGameView.systemError();
        }
        singlePlayerGame.getRound().getTurn().setBulletPoisoned(false);
        singlePlayerGameView.showShootingResult(currentBullet.getType());

        return changeTurn;
    }
}
