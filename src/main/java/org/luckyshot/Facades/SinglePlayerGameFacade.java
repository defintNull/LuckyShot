package org.luckyshot.Facades;

import org.luckyshot.Models.*;
import org.luckyshot.Models.Consumables.Consumable;
import org.luckyshot.Models.StateEffects.StateEffect;
import org.luckyshot.Views.SinglePlayerGameView;
import org.luckyshot.Views.View;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

public class SinglePlayerGameFacade {
    private static SinglePlayerGameFacade instance;
    private SinglePlayerGame singlePlayerGame;

    private SinglePlayerGameFacade() {

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

        HumanPlayer humanPlayer = new HumanPlayer(user.getPowerups());
        BotPlayer botPlayer = new BotPlayer();

        user.setPlayer(humanPlayer);

        this.singlePlayerGame = new SinglePlayerGame(user.getPlayer(), botPlayer);

        showGameState();

        boolean gameEnded = false;
        while(!gameEnded) {
            update();

            // DA RIVEDERE LA CONDIZIONE DI FINE GIOCO
            if(singlePlayerGame.getRound().getRoundNumber() == 3 && humanPlayer.getLives() <= 0 || botPlayer.getLives() <= 0) {
                gameEnded = true;
            }
        }
    }

    public void showGameState() {
        HashMap<String, Object> objectStateMap = singlePlayerGame.getStateMap();
        HashMap<String, String> stateMap = new HashMap<>();

        stateMap.put("botLives", Integer.toString(((BotPlayer)objectStateMap.get("bot")).getLives()));
        stateMap.put("humanPlayerLives", Integer.toString(((HumanPlayer)objectStateMap.get("humanPlayer")).getLives()));

        ArrayList<String> botConsumables = (((BotPlayer) objectStateMap.get("bot")).getConsumables());
        botConsumables.forEach(consumable -> {
            if(!stateMap.containsKey("bot" + consumable)) {
                stateMap.put("bot" + consumable, "0");
            }
            stateMap.put("bot" + consumable, Integer.toString(Integer.parseInt(stateMap.get("bot" + consumable)) + 1));
        });

        ArrayList<String> humanConsumables = (((HumanPlayer) objectStateMap.get("humanPlayer")).getConsumables());
        humanConsumables.forEach(consumable -> {
            if(!stateMap.containsKey("human" + consumable)) {
                stateMap.put("human" + consumable, "0");
            }
            stateMap.put("human" + consumable, Integer.toString(Integer.parseInt(stateMap.get("human" + consumable)) + 1));
        });

        Round round = (Round)objectStateMap.get("round");
        stateMap.put("roundNumber", Integer.toString(round.getRoundNumber()));
        StateEffect currentStateEffect = round.getStateEffect();
        if(currentStateEffect != null) {
            stateMap.put("stateEffect", round.getStateEffect().getClass().toString());
        } else {
            stateMap.put("stateEffect", "none");
        }

        stateMap.put("turn", singlePlayerGame.getRound().getTurn().getPlayer().getClass().getSimpleName());

        System.out.println(stateMap);

        SinglePlayerGameView singlePlayerGameView = new SinglePlayerGameView();
        singlePlayerGameView.showGameState(stateMap);
    }

    public int getUserInput() {
        if(this.singlePlayerGame.getRound().getTurn().getPlayer().getClass() == HumanPlayer.class) {
            SinglePlayerGameView singlePlayerGameView = new SinglePlayerGameView();
            return singlePlayerGameView.getUserInput();
        }

        if(this.singlePlayerGame.getRound().getTurn().getPhase() == 3) {
            return this.singlePlayerGame.getBot().getConsumableInput();
        }
        return this.singlePlayerGame.getBot().getShootingInput();
    }

    public void update() {
        Gun gun = Gun.getInstance();
        if(gun.isEmpty()) {
            consumableDrawPhase();
            gunLoadingPhase();
            showGameState();
        }

        if(this.singlePlayerGame.getRound().getTurn().getPlayer().getClass() == HumanPlayer.class) {
            powerupUsePhase();
            showGameState();

            consumableUsePhase();
            showGameState();
        }

        if(this.singlePlayerGame.getRound().getTurn().getPlayer().getClass() == BotPlayer.class) {
            Random rand = new Random();
            if(rand.nextInt(100) < 66) {
                consumableUsePhase();
                showGameState();
            }
        }

        shootingPhase();
        showGameState();
    }


    private String getRandomConsumable() {
        HashMap consumableProb = new HashMap<>();

        for(Class<? extends Consumable> c : Consumable.getConsumableClassList()) {
            try {
                Class<?> cls = Class.forName(c.getName());
                Method m = cls.getMethod("getInstance");
                Object consumable = m.invoke(null);
                System.out.println(((Consumable) consumable).getProbability());
            } catch (Exception e) {
                View view = new View();
                view.systemError();
            }
        }

        return "";
    }

    public void consumableDrawPhase() {
        int maxConsumablesNumber = 8;
        this.singlePlayerGame.getRound().getTurn().setPhase(0);
        Random rand = new Random();
        int r = rand.nextInt(2, 6);
        int numberOfConsumablesHumanPlayer = Math.min(r, maxConsumablesNumber - singlePlayerGame.getHumanPlayer().getConsumablesNumber());
        int numberOfConsumablesBotPlayer = Math.min(r, maxConsumablesNumber - singlePlayerGame.getBot().getConsumablesNumber());

        getRandomConsumable();

        ArrayList<String> consumables = new ArrayList<>();
        for(int i = 0; i < numberOfConsumablesHumanPlayer; i++) {
            String randomConsumable = Consumable.getConsumableStringList().get(rand.nextInt(0, Consumable.getConsumableStringList().size()));
            consumables.add(randomConsumable);
        }

        singlePlayerGame.getHumanPlayer().setConsumables(consumables);

        consumables = new ArrayList<>();
        for(int i = 0; i < numberOfConsumablesBotPlayer; i++) {
            String randomConsumable = Consumable.getConsumableStringList().get(rand.nextInt(0, Consumable.getConsumableStringList().size()));
            consumables.add(randomConsumable);
        }
        singlePlayerGame.getBot().setConsumables(consumables);
    }

    public void gunLoadingPhase() {
        this.singlePlayerGame.getRound().getTurn().setPhase(1);
        ArrayList<Bullet> b = Gun.getInstance().generateBulletSequence();
        Gun.getInstance().setBullets(b);
    }

    public void powerupUsePhase() {
        this.singlePlayerGame.getRound().getTurn().setPhase(2);
        //AGGIUNGERE CONTROLLO POWERUP
        //if(this.singlePlayerGame.getHumanPlayer().)
        int userInput = getUserInput();
    }

    public void consumableUsePhase() {
        this.singlePlayerGame.getRound().getTurn().setPhase(3);
        //AGGIUNGERE CONTROLLO CONSUMABILI
        int userInput = getUserInput();
    }

    public void shootingPhase() {
        this.singlePlayerGame.getRound().getTurn().setPhase(4);
        int userInput = getUserInput();
    }
}
