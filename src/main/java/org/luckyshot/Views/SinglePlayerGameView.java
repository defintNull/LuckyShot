package org.luckyshot.Views;

import org.luckyshot.Models.Consumables.ConsumableInterface;
import org.luckyshot.Models.Powerups.Powerup;
import org.luckyshot.Models.Powerups.PowerupInterface;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

public class SinglePlayerGameView extends GameView{
    public void showGameState(HashMap<String, String> stateMap) {
        this.stateMap = stateMap;

        String letters = "abcdefghijklmnopqrstuvwxyz";
        setCursorPos(2, 2);
        System.out.print("Round " + stateMap.get("roundNumber"));

        setCursorPos(2, 40);
        System.out.print("State effect: " + stateMap.get("stateEffect"));

        // Showing bot lives and state (shield, poison)
        setCursorPos(4, 2);
        System.out.print("Bot: ");
        for(int i = 0; i < Integer.parseInt(stateMap.get("botLives")); i++) {
            System.out.print(ANSI_RED + "♥" + ANSI_RESET);
        }
        if(stateMap.get("isBotPoisoned").equals("true")) {
            System.out.print(" P");
        }

        if(stateMap.get("isBotHandcuffed").equals("true")) {
            System.out.print(" H");
        }

        // Showing bot consumables
        int cNumber = 0;
        setCursorPos(6, 2);
        System.out.print("Consumables:");
        for (int i = 0; i < ConsumableInterface.getConsumableStringList().size(); i++) {
            setCursorPos(8+cNumber, 2);
            if (stateMap.get("bot" + ConsumableInterface.getConsumableClassList().get(i).getSimpleName()) != null) {
                System.out.print(letters.charAt(i) + ". " + ConsumableInterface.getConsumableStringList().get(i) + ": x");
                System.out.print(stateMap.get("bot" + ConsumableInterface.getConsumableClassList().get(i).getSimpleName()));
                cNumber++;
            }
        }

        // Showing human lives and state (shield, poison)
        setCursorPos(22, 2);
        System.out.print("You: ");
        for(int i = 0; i < Integer.parseInt(stateMap.get("humanPlayerLives")); i++) {
            System.out.print(ANSI_RED + "♥" + ANSI_RESET);
        }
        if(stateMap.get("isHumanPoisoned").equals("true")) {
            System.out.print(" P");
        }
        if(stateMap.get("isHumanShielded").equals("true")) {
            System.out.print(" S");
        }
        if(stateMap.get("isHumanHandcuffed").equals("true")) {
            System.out.print(" H");
        }
        // SPAZIO VA FATTO PROPORZIONALE AL NUMERO DI CIFRE
        setCursorPos(22, 20);
        System.out.print("Combo: " + stateMap.get("humanCombo"));
        setCursorPos(22, 35);
        System.out.print("Score: " + stateMap.get("humanScore"));

        // Showing human consumables
        cNumber = 0;
        setCursorPos(24, 2);
        System.out.print("Consumables:");
        for (int i = 0; i < ConsumableInterface.getConsumableStringList().size(); i++) {
            setCursorPos(26+cNumber, 2);
            if (stateMap.get("human" + ConsumableInterface.getConsumableClassList().get(i).getSimpleName()) != null) {
                System.out.print(letters.charAt(i) + ". " + ConsumableInterface.getConsumableStringList().get(i) + ": x");
                System.out.print(stateMap.get("human" + ConsumableInterface.getConsumableClassList().get(i).getSimpleName()));
                cNumber++;
            }
        }

        // Showing powerups
        setCursorPos(24, 27);
        System.out.print("Powerups:");
        for (int i = 0; i < PowerupInterface.getPowerupStringList().size(); i++) {
            setCursorPos(26+i, 27);
            System.out.print(i + 1 + ". " + PowerupInterface.getPowerupStringList().get(i) + ": x");
            System.out.print(stateMap.get(PowerupInterface.getPowerupStringList().get(i)));
        }
    }

    public void printLastAction() {
        setCursorPos(4, 52);

        if(stateMap.get("turn").equals("HumanPlayer")) {
            customPrint("It's your turn.", "fast", 4, 52);
        } else {
            customPrint("It's Bot turn.", "fast", 4, 52);
        }

        if(!lastAction.isEmpty()) {
            for(int i=0; i<lastAction.size(); i++) {
                customPrint(lastAction.get(i), "slow", 6 + (2*i), 52);
            }
            try {
                Thread.sleep(2000);
            } catch (Exception e) {
                showError("sleep");
            }
        }

        lastAction.clear();
    }

    public void showGame(HashMap<String, String> stateMap) {
        clearScreen();

        drawTable();
        showGameState(stateMap);
        printLastAction();
        printLastErrorAction();
    }

    public void showShootingTarget(String target) {
        if(target.equals("1")) {
            lastAction.addFirst("The bot shot himself!");
        } else {
            lastAction.addFirst("The bot shoots you!");
        }
    }

    public void showShootingResult(int bulletType) {
        if(bulletType == 0) {
            lastAction.addFirst("The bullet was fake...");
        } else if(bulletType == 1) {
            lastAction.addFirst("The bullet was live!");
        }
    }

    public void showPowerupActivation(String powerup) {
        lastAction.add("A " + powerup.toLowerCase() + " has been used!");
    }

    public void showPowerupEffect(Powerup powerup) {
        lastAction.add(powerup.toString() + ": " + powerup.getEffect());
    }

    public void addLastAction(String s) {
        lastAction.add(s);
    }

    public void showFinalXp(int xp) {
        lastAction.add("You gained " + xp + " xp!");
    }

    public void showLevelAndXp(String user, int level, int xp) {
        lastAction.add(user + ":  Level:" + level + "      XP:" + xp);
    }

    public void showWinner(String winner) {
        lastAction.add("The game is over...");
        lastAction.add("The winner is: " + winner + "!");
    }

    public void showEndGameScreen() {
        showGame(this.stateMap);
        try {
            Thread.sleep(2000);
        } catch (Exception e) {
            showError("sleep");
        }
    }

    public void showRoundStartingScreen(int roundNumber) {
        lastAction.add("Starting round " + roundNumber + "...");
        try {
            Thread.sleep(2000);
        } catch (Exception e) {
            showError("sleep");
        }
    }

    public void showConsumableActivation(String string) {
        lastAction.add(string + " used!");
    }

    public void showConsumableEffect(String effect) {
        lastAction.add(effect);
    }

    public void showGhostGunDamage() {
        lastAction.add("The damage was doubled!");
    }

    public void showStateEffectActivation(String activation) {
        lastAction.add(activation);
    }

    public void showStateEffectEffect(String effect) {
        lastAction.add(effect);
    }

    public void showHandcuffedState(boolean state) {
        if(state) {
            lastAction.add("You are handcuffed, you lose your turn!");
        } else {
            lastAction.add("The other player has been freed.");
        }
    }

    public void showEnergyDrinkChoise() {
        lastAction.add("Choose the consumable to steal.");
    }

    public void showConsumablesDrawn() {
        lastAction.add("Consumables drawn...");
    }
}
