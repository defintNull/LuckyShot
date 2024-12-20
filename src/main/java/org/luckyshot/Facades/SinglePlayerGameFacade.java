package org.luckyshot.Facades;

import org.hibernate.Session;
import org.hibernate.Transaction;
import org.luckyshot.Models.*;
import org.luckyshot.Models.Consumables.*;
import org.luckyshot.Models.Powerups.*;
import org.luckyshot.Models.StateEffects.*;
import org.luckyshot.Views.SinglePlayerGameView;

import java.lang.reflect.Method;
import java.util.*;

public class SinglePlayerGameFacade {
    private static SinglePlayerGameFacade instance;
    private SinglePlayerGame singlePlayerGame;
    private final SinglePlayerGameView singlePlayerGameView;
    private User user;

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
        this.user = user;
        HumanPlayer humanPlayer = new HumanPlayer(user.getId(), user.getPowerups());
        BotPlayer botPlayer = new BotPlayer();

        user.setPlayer(humanPlayer);

        this.singlePlayerGame = new SinglePlayerGame(user.getPlayer(), botPlayer);

        boolean gameEnded = false;
        int roundNumber = 1;
        Player[] players = {singlePlayerGame.getHumanPlayer(), singlePlayerGame.getBot()};

        while(!gameEnded) {
            //Inizio di un round
            StateEffect stateEffect = getRandomStateEffect();
            Round round = new Round(roundNumber, stateEffect);
            Gun.getInstance().clearBullets();

            this.singlePlayerGame.setRound(round);
            Random rnd = new Random();
            int randomLives = rnd.nextInt(2, 5);
            this.singlePlayerGame.getRound().setMaxLives(randomLives);
            this.singlePlayerGame.getHumanPlayer().setResurrected(false);
            this.singlePlayerGame.getHumanPlayer().setLives(randomLives);
            this.singlePlayerGame.getBot().setResurrected(false);
            this.singlePlayerGame.getBot().setLives(randomLives);

            boolean roundEnded = false;
            int turn = 0;

            singlePlayerGameView.showRoundStartingScreen(roundNumber);
            singlePlayerGameView.showStateEffectActivation(stateEffect.getActivation());

            while(!roundEnded) {
                //Inizio di un turno
                Player currentPlayer = players[turn];
                Player otherPlayer = players[(turn + 1) % 2];

                Turn currentTurn = new Turn(currentPlayer, otherPlayer);
                this.singlePlayerGame.getRound().setTurn(currentTurn);

                processTurn();

                // Condizione di fine round
                if(botPlayer.getLives() <= 0) {
                    roundEnded = true;
                    roundNumber += 1;

                    //XP
                    singlePlayerGame.getHumanPlayer().addXp(30);
                }
                // Condizione di fine gioco
                if(humanPlayer.getLives() <= 0) {
                    roundEnded = true;
                    gameEnded = true;
                }

                turn = (turn + 1) % 2;
            }

            // Condizione di fine gioco
            if(singlePlayerGame.getRound().getRoundNumber() >= 3 && (humanPlayer.getLives() <= 0 || botPlayer.getLives() <= 0)) {
                gameEnded = true;
            }
        }
        // XP
        if(humanPlayer.getLives() != 0) {
            humanPlayer.addXp(120);
        }

        // Passing parameters to User
        user.setXp(user.getXp() + humanPlayer.getXp());
        while(user.getLevel() * 1000 < user.getXp()) {
            user.setXp(user.getXp() - (user.getLevel() * 1000));
            user.setLevel(user.getLevel() + 1);
            user.addCoins(3);
        }

        //Persistence
        Session session = HibernateService.getInstance().getSessionFactory().openSession();
        Transaction transaction;
        try {
            transaction = session.beginTransaction();
            session.merge(user);
            transaction.commit();
        } catch (Exception e) {
            singlePlayerGameView.showError("Synchronization error");
        }

        session.close();

        singlePlayerGameView.showWinner(humanPlayer.getLives() != 0 ? "you" : "bot");
        singlePlayerGameView.showFinalXp(humanPlayer.getXp());
        singlePlayerGameView.showLevelAndXp(user.getUsername(), user.getLevel(), user.getXp());
        singlePlayerGameView.showEndGameScreen();

        Facade.getInstance(user).menu();
    }

    public void showGameState() {
        HashMap<String, Object> objectStateMap = singlePlayerGame.getStateMap();
        HashMap<String, String> stateMap = new HashMap<>();

        // To show players lives on view
        stateMap.put("botLives", Integer.toString(((BotPlayer)objectStateMap.get("bot")).getLives()));
        stateMap.put("humanPlayerLives", Integer.toString(((HumanPlayer)objectStateMap.get("humanPlayer")).getLives()));

        // To show combo and score
        stateMap.put("humanCombo", Integer.toString(singlePlayerGame.getHumanPlayer().getComboCounter()));
        stateMap.put("humanScore", Integer.toString(singlePlayerGame.getHumanPlayer().getScore()));

        // To show bot consumables on view
        ArrayList<Consumable> botConsumables = (((BotPlayer) objectStateMap.get("bot")).getConsumables());
        botConsumables.forEach(consumable -> {
            if(!stateMap.containsKey("bot" + consumable.getClass().getSimpleName())) {
                stateMap.put("bot" + consumable.getClass().getSimpleName(), "0");
            }
            stateMap.put("bot" + consumable.getClass().getSimpleName(), Integer.toString(Integer.parseInt(stateMap.get("bot" + consumable.getClass().getSimpleName())) + 1));
        });

        // To show human consumables on view
        ArrayList<Consumable> humanConsumables = (((HumanPlayer) objectStateMap.get("humanPlayer")).getConsumables());
        humanConsumables.forEach(consumable -> {
            if(!stateMap.containsKey("human" + consumable.getClass().getSimpleName())) {
                stateMap.put("human" + consumable.getClass().getSimpleName(), "0");
            }
            stateMap.put("human" + consumable.getClass().getSimpleName(), Integer.toString(Integer.parseInt(stateMap.get("human" + consumable.getClass().getSimpleName())) + 1));
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
        stateMap.put("turn", singlePlayerGame.getRound().getTurn().getCurrentPlayer().getClass().getSimpleName());

        // To show powerups effects on view
        stateMap.put("isBotPoisoned", Boolean.toString(singlePlayerGame.getBot().isPoisoned()));
        stateMap.put("isHumanPoisoned", Boolean.toString(singlePlayerGame.getHumanPlayer().isPoisoned()));
        stateMap.put("isHumanShielded", Boolean.toString(singlePlayerGame.getHumanPlayer().isShieldActive()));
        stateMap.put("isBotHandcuffed", Boolean.toString(singlePlayerGame.getBot().isHandcuffed()));
        stateMap.put("isHumanHandcuffed", Boolean.toString(singlePlayerGame.getHumanPlayer().isHandcuffed()));

        singlePlayerGameView.showGame(stateMap);
    }

    public String getPlayerInput() {
        if(this.singlePlayerGame.getRound().getTurn().getCurrentPlayer().getClass() == HumanPlayer.class) {
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

        // Rimuovo lo scudo al giocatore corrente
        singlePlayerGame.getRound().getTurn().getCurrentPlayer().setShieldActive(false);

        // Rimuovo manette
        if(singlePlayerGame.getRound().getTurn().getOtherPlayer().isHandcuffed()) {
            singlePlayerGame.getRound().getTurn().getOtherPlayer().setHandcuffed(false);
            singlePlayerGameView.showHandcuffedState(false);
        }

        while(!changeTurn) {
            showGameState();

            if(!singlePlayerGame.getRound().getTurn().getCurrentPlayer().isHandcuffed()) {

                // Se la pistola è vuota, assegno i consumabili e la ricarico
                if(gun.isEmpty()) {
                    drawConsumables();
                    showGameState();
                    loadGun();
                    showGameState();
                }

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
                } else {
                    singlePlayerGameView.showError("Command not recognized");
                }
            } else {
                singlePlayerGameView.showHandcuffedState(true);
                changeTurn = true;
            }
        }
        //Poison effect
        if(singlePlayerGame.getRound().getTurn().getCurrentPlayer().isPoisoned()) {
            singlePlayerGame.getRound().getTurn().getCurrentPlayer().setLives(singlePlayerGame.getRound().getTurn().getCurrentPlayer().getLives() - 1);
            singlePlayerGame.getRound().getTurn().getCurrentPlayer().setPoisoned(false);
            singlePlayerGameView.showPowerupEffect(PoisonBullet.getInstance());
        }

        //Guardian angel
        if(singlePlayerGame.getRound().getStateEffect().getClass() == GuardianAngel.class) {
            if(!singlePlayerGame.getHumanPlayer().isResurrected() && !singlePlayerGame.getBot().isResurrected()) {
                if(singlePlayerGame.getHumanPlayer().getLives() == 0) {
                    singlePlayerGame.getHumanPlayer().setLives(1);
                    singlePlayerGame.getHumanPlayer().setResurrected(true);
                    singlePlayerGameView.showStateEffectEffect(singlePlayerGame.getRound().getStateEffect().getEffect());
                } else if(singlePlayerGame.getBot().getLives() == 0) {
                    singlePlayerGame.getBot().setLives(1);
                    singlePlayerGame.getBot().setResurrected(true);
                    singlePlayerGameView.showStateEffectEffect(singlePlayerGame.getRound().getStateEffect().getEffect());
                }
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
            useConsumable(target);
        }
    }

    public void useConsumable(String target) {
        HashMap<String, Class<? extends Consumable>> map = new HashMap<>();
        String alphabet = "abcdefghijklmnopqrstuwxyz";
        for(int i=0; i<ConsumableInterface.getConsumableClassList().size(); i++) {
            map.put(Character.toString(alphabet.charAt(i)), ConsumableInterface.getConsumableClassList().get(i));
        }

        if(!map.containsKey(target)) {
            singlePlayerGameView.showError("No consumable found");
            return;
        }

        Class<? extends Consumable> consumableClass = map.get(target);

        boolean check = false;
        for(int i=0; i<singlePlayerGame.getRound().getTurn().getCurrentPlayer().getConsumablesNumber(); i++) {
            if(singlePlayerGame.getRound().getTurn().getCurrentPlayer().getConsumables().get(i).getClass().getSimpleName().equals(consumableClass.getSimpleName())) {
                check = true;
                break;
            }
        }

        if(!check) {
            singlePlayerGameView.showError("You don't have this consumable!");
            return;
        }
        try {
            boolean used = false;
            Method method = Class.forName(consumableClass.getName()).getMethod("getInstance");
            Object obj = method.invoke(null);

            // Fog state effect
            boolean checkFog = singlePlayerGame.getRound().getStateEffect().getClass() == Fog.class && (obj.getClass() == CrystalBall.class || obj.getClass() == Glasses.class);

            //Antidote state effect
            boolean checkAntidote = singlePlayerGame.getRound().getStateEffect().getClass() == Antidote.class && (obj.getClass() == HealthPotion.class || obj.getClass() == MisteryPotion.class);

            if(checkFog || checkAntidote) {
                singlePlayerGameView.showStateEffectEffect(singlePlayerGame.getRound().getStateEffect().getEffect());
                return;
            }

            singlePlayerGameView.showConsumableActivation(obj.toString()); // Ho tolto il cast a Consumable per il warning
            String effect = ((Consumable) obj).use(singlePlayerGame);
            singlePlayerGameView.showConsumableEffect(((Consumable) obj).getEffect(effect));
            if(obj.getClass() == EnergyDrink.class) {
                singlePlayerGameView.showEnergyDrinkChoise();
                showGameState();
                Character consumableToSteal = getPlayerInput().charAt(0);
                boolean ok = false;
                for(int i = 0; i < alphabet.length(); i++) {
                    if(consumableToSteal.equals(alphabet.charAt(i))) {
                        ok = true;
                        break;
                    }
                }

                ArrayList<Consumable> otherPlayerConsumables = singlePlayerGame.getRound().getTurn().getOtherPlayer().getConsumables();
                ArrayList<Character> charList = new ArrayList<>();
                for (Consumable otherPlayerConsumable : otherPlayerConsumables) {
                    for (Map.Entry<String, Class<? extends Consumable>> entry : map.entrySet()) {
                        if (otherPlayerConsumable.getClass().equals(entry.getValue())) {
                            charList.add(entry.getKey().charAt(0));
                        }
                    }
                }

                boolean exists = false;
                for (Character character : charList) {
                    if (character == consumableToSteal) {
                        exists = true;
                        break;
                    }
                }

                if(ok && exists) {
                    Class<? extends Consumable> stolenConsumableClass = map.get(consumableToSteal.toString());
                    if(stolenConsumableClass == EnergyDrink.class) {
                        singlePlayerGameView.addLastAction("You can't choose another energy drink");
                    } else {
                        Method method2 = Class.forName(stolenConsumableClass.getName()).getMethod("getInstance");
                        Object obj2 = method2.invoke(null);

                        //Fog effect
                        boolean checkFog2 = false;
                        if(singlePlayerGame.getRound().getStateEffect().getClass() == Fog.class) {
                            if(obj2.getClass() == CrystalBall.class || obj2.getClass() == Glasses.class) {
                                checkFog2 = true;
                            }
                        }

                        if(!checkFog2) {
                            singlePlayerGameView.showConsumableActivation(obj2.toString()); // Ho tolto il cast a Consumable per il warning
                            String effect2 = ((Consumable) obj2).use(singlePlayerGame);
                            singlePlayerGameView.showConsumableEffect(((Consumable) obj2).getEffect(effect2));
                            singlePlayerGame.getRound().getTurn().getOtherPlayer().removeConsumable((Consumable) obj2);
                            used = true;
                        } else {
                            singlePlayerGameView.showStateEffectEffect(singlePlayerGame.getRound().getStateEffect().getEffect());
                        }
                    }
                }
            } else {
                used = true;
            }

            if(used) {
                singlePlayerGame.getRound().getTurn().getCurrentPlayer().removeConsumable((Consumable) obj);

                //XP
                if(singlePlayerGame.getRound().getTurn().getCurrentPlayer().getClass() == HumanPlayer.class) {
                    ((HumanPlayer) singlePlayerGame.getRound().getTurn().getCurrentPlayer()).addXp(10);
                }
            } else {
                singlePlayerGameView.addLastAction("Consumable could not be used");
            }
        } catch (Exception e) {
            singlePlayerGameView.showError("No consumable found");
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

    private Consumable getRandomConsumable() {
        HashMap<Consumable, Integer> consumableProb = new HashMap<>();

        for(Class<? extends Consumable> c : ConsumableInterface.getConsumableClassList()) {
            try {
                Class<?> cls = Class.forName(c.getName());
                Method m = cls.getMethod("getInstance");
                Object consumable = m.invoke(null);
                consumableProb.put((Consumable) consumable, ((Consumable) consumable).getProbability());
            } catch (Exception e) {
                singlePlayerGameView.showError("Error getting consumable probability");
            }
        }

        Random rand = new Random();
        Consumable consumable = null;
        int tries = 0;
        int maxTries = 100;
        boolean found = false;
        while(!found && tries < maxTries) {
            ArrayList<Consumable> consumableList = new ArrayList<>(consumableProb.keySet());
            Consumable randomConsumable = consumableList.get(rand.nextInt(consumableList.size()));
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
        singlePlayerGameView.addLastAction("Consumables drawn...");
        int maxConsumablesNumber = 8;
        Random rand = new Random();
        int r = rand.nextInt(2, 6);
        int numberOfConsumablesHumanPlayer = Math.min(r, maxConsumablesNumber - singlePlayerGame.getHumanPlayer().getConsumablesNumber());
        int numberOfConsumablesBotPlayer = Math.min(r, maxConsumablesNumber - singlePlayerGame.getBot().getConsumablesNumber());

        ArrayList<Consumable> consumables = singlePlayerGame.getHumanPlayer().getConsumables();
        for(int i = 0; i < numberOfConsumablesHumanPlayer; i++) {
            Consumable randomConsumable = getRandomConsumable();
            consumables.add(randomConsumable);
        }
        singlePlayerGame.getHumanPlayer().setConsumables(consumables);

        consumables = singlePlayerGame.getBot().getConsumables();
        for(int i = 0; i < numberOfConsumablesBotPlayer; i++) {
            Consumable randomConsumable = getRandomConsumable();
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
        if(target < 1 || target > PowerupInterface.getPowerupClassList().size()) {
            singlePlayerGameView.showError("No such powerup.");
        }
        String powerupName = PowerupInterface.getPowerupClassList().get(target - 1).getName();
        try {
            Method method = Class.forName(powerupName).getMethod("getInstance");
            Object obj = method.invoke(null);

            if (singlePlayerGame.getHumanPlayer().getPowerups().get((Powerup) obj) == 0) {
                singlePlayerGameView.showError("No such powerup.");
                return;
            }

            ((Powerup) obj).use(singlePlayerGame);
            singlePlayerGame.getHumanPlayer().getPowerups().put((Powerup) obj, singlePlayerGame.getHumanPlayer().getPowerups().get(obj) - 1);
            user.removePowerup((Powerup) obj);

            //Persistence
            Session session = HibernateService.getInstance().getSessionFactory().openSession();
            Transaction transaction;
            try {
                transaction = session.beginTransaction();
                session.merge(user);
                transaction.commit();
            } catch (Exception e) {
                singlePlayerGameView.showError("Powerup db error");
            }
            session.close();

            singlePlayerGameView.showPowerupActivation(obj.toString()); // Ho tolto il cast a Powerup per il warning
            if (obj.getClass() == Bomb.class) {
                singlePlayerGameView.showPowerupEffect(Bomb.getInstance());
            }
        } catch (Exception e) {
            singlePlayerGameView.showError("Could not use powerup...");
        }
    }

    public boolean shootingPhase(String target) {
        boolean changeTurn = false;
        boolean shot = false;

        Player currentPlayer = singlePlayerGame.getRound().getTurn().getCurrentPlayer();
        Bullet currentBullet = Gun.getInstance().popBullet();

        // 1 = self
        // 2 = other
        if(target.equals("1")) {
            if(currentBullet.getType() == 1) {
                changeTurn = true;
                if(!currentPlayer.isShieldActive()) {
                    currentPlayer.setLives(currentPlayer.getLives() - Gun.getInstance().getDamage());
                    if(Gun.getInstance().getDamage() != 1) {
                        singlePlayerGameView.showGhostGunDamage();
                    }
                    if(singlePlayerGame.getRound().getTurn().isBulletPoisoned()) {
                        currentPlayer.setPoisoned(true);
                    }

                    //Score system reset
                    if(singlePlayerGame.getRound().getTurn().getCurrentPlayer().getClass() == HumanPlayer.class) {
                        ((HumanPlayer) singlePlayerGame.getRound().getTurn().getCurrentPlayer()).setComboCounter(0);
                        ((HumanPlayer) singlePlayerGame.getRound().getTurn().getCurrentPlayer()).setMultiplier(1);
                    }
                } else {
                    singlePlayerGameView.showPowerupEffect(Shield.getInstance());
                    currentPlayer.setShieldActive(false);
                }
            } else {
                //Score system & XP
                if(singlePlayerGame.getRound().getTurn().getCurrentPlayer().getClass() == HumanPlayer.class) {
                    ((HumanPlayer) singlePlayerGame.getRound().getTurn().getCurrentPlayer()).addScore((int) Math.round(((HumanPlayer) singlePlayerGame.getRound().getTurn().getCurrentPlayer()).getMultiplier() * 80));
                    ((HumanPlayer) singlePlayerGame.getRound().getTurn().getCurrentPlayer()).incrementComboCounter();
                    if((((HumanPlayer) singlePlayerGame.getRound().getTurn().getCurrentPlayer()).getComboCounter() % 5) == 0) {
                        ((HumanPlayer) singlePlayerGame.getRound().getTurn().getCurrentPlayer()).incrementMultiplayer();
                    }

                    // XP
                    ((HumanPlayer) singlePlayerGame.getRound().getTurn().getCurrentPlayer()).addXp(20);
                }
            }
            shot = true;
        } else if(target.equals("2")) {
            if(currentBullet.getType() == 1) {
                Player otherPlayer = singlePlayerGame.getRound().getTurn().getOtherPlayer();

                if(!otherPlayer.isShieldActive()) {
                    otherPlayer.setLives(otherPlayer.getLives() - Gun.getInstance().getDamage());
                    if(Gun.getInstance().getDamage() != 1) {
                        singlePlayerGameView.showGhostGunDamage();
                    }
                    if(singlePlayerGame.getRound().getTurn().isBulletPoisoned()) {
                        otherPlayer.setPoisoned(true);
                    }

                    //Score system & XP
                    if(singlePlayerGame.getRound().getTurn().getCurrentPlayer().getClass() == HumanPlayer.class) {
                        //Score System
                        ((HumanPlayer) singlePlayerGame.getRound().getTurn().getCurrentPlayer()).addScore((int) Math.round(((HumanPlayer) singlePlayerGame.getRound().getTurn().getCurrentPlayer()).getMultiplier() * 100));
                        ((HumanPlayer) singlePlayerGame.getRound().getTurn().getCurrentPlayer()).incrementComboCounter();
                        if((((HumanPlayer) singlePlayerGame.getRound().getTurn().getCurrentPlayer()).getComboCounter() % 5) == 0) {
                            ((HumanPlayer) singlePlayerGame.getRound().getTurn().getCurrentPlayer()).incrementMultiplayer();
                        }

                        // XP
                        ((HumanPlayer) singlePlayerGame.getRound().getTurn().getCurrentPlayer()).addXp(25);
                    }
                } else {
                    singlePlayerGameView.showPowerupEffect(Shield.getInstance());
                    otherPlayer.setShieldActive(false);
                }
            } else {
                //Score system reset
                if(singlePlayerGame.getRound().getTurn().getCurrentPlayer().getClass() == HumanPlayer.class) {
                    ((HumanPlayer) singlePlayerGame.getRound().getTurn().getCurrentPlayer()).setComboCounter(0);
                    ((HumanPlayer) singlePlayerGame.getRound().getTurn().getCurrentPlayer()).setMultiplier(1);
                }
            }
            shot = true;
            changeTurn = true;
        } else {
            singlePlayerGameView.showError("Select 1 or 2");
        }

        if(shot) {
            Gun.getInstance().setDamage(1);
            singlePlayerGame.getRound().getTurn().setBulletPoisoned(false);
            singlePlayerGameView.showShootingResult(currentBullet.getType());
            if(currentPlayer.getClass() == BotPlayer.class) {
                singlePlayerGameView.showShootingTarget(target);
            }
        }
        return changeTurn;
    }
}
