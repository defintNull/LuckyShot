package org.luckyshot.Views;

import org.checkerframework.checker.units.qual.A;
import org.luckyshot.Models.Bullet;
import org.luckyshot.Models.Consumables.Consumable;
import org.luckyshot.Models.Consumables.ConsumableInterface;
import org.luckyshot.Models.HumanPlayer;
import org.luckyshot.Models.Player;
import org.luckyshot.Models.Powerups.Powerup;
import org.luckyshot.Models.Powerups.PowerupInterface;

import java.io.IOException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Objects;

public class SinglePlayerGameView extends GameView{
    HashMap<String, String> stateMap = new HashMap<>();
    ArrayList<String> lastAction = new ArrayList<String>();

    public void debugSleep(int t) {
        try {
            Thread.sleep(t);
        } catch (Exception e) {
            showError("debug sleep error");
        }
    }

    public void showError(String s) {
        showGame(stateMap);
        setCursorPos(27, 52);
        System.out.print(ANSI_RED + "ERROR: " + s + ANSI_RESET);
    }

    public void debug(String s) {
        showGame(stateMap);
        setCursorPos(28, 52);
        System.out.print(ANSI_CYAN + "DEBUG: " + s + ANSI_RESET);
    }

    public void customPrint(String message, String speed, int row, int col) {
        setCursorPos(row, col);
        if(speed.equals("fast")) {
            System.out.print(message);
        } else if(speed.equals("slow")) {
            slowPrint(message);
        }
    }

    public void drawTable() {
        displayHeader();

        for(int i = 0; i < 29; i++) {
            setCursorPos(4+i, 1);
            System.out.print("║");
        }
        for(int i = 0; i < 29; i++) {
            setCursorPos(4+i, 100);
            System.out.print("║");
        }

        for(int i = 0; i < 29; i++) {
            setCursorPos(4+i, 51);
            System.out.print("│");
        }

        setCursorPos(5, 1);
        System.out.print("╟" + "─".repeat(49) + "┤");

        setCursorPos(22, 1);
        System.out.print("╟" + "─".repeat(49) + "┤");

        setCursorPos(15, 1);
        System.out.print("╟" + "─".repeat(49) + "┼" + "─".repeat(48) + "╢");
        setCursorPos(16, 22);
        System.out.print("__,_____");
        setCursorPos(17, 21);
        System.out.print("/ __.==--\"");
        setCursorPos(18, 20);
        System.out.print("/#(-'");
        setCursorPos(19, 20);
        System.out.print("`-'");

        setCursorPos(20, 1);
        System.out.print("╟" + "─".repeat(49) + "┼" + "─".repeat(48) + "╢");
        setCursorPos(33, 1);
        System.out.print("╚" + "═".repeat(98) + "╝");

        setCursorPos(3, 51);
        System.out.print("╤");

        setCursorPos(33, 51);
        System.out.print("╧");
    }

    public void showGameState(HashMap<String, String> stateMap) {
        this.stateMap = stateMap;

        String letters = "abcdefghi"; // Da vedere perchè non scalabile
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
            System.out.print(" A");
        }

        if(stateMap.get("isBotHandcuffed").equals("true")) {
            System.out.print(" M");
        }

        // Showing bot consumables
        int cNumber = 0;
        for (int i = 0; i < ConsumableInterface.getConsumableStringList().size(); i++) {
            setCursorPos(6+cNumber, 2);
            if (stateMap.get("bot" + ConsumableInterface.getConsumableClassList().get(i).getSimpleName()) != null) {
                System.out.print(letters.charAt(i) + ". " + ConsumableInterface.getConsumableStringList().get(i) + ": x");
                System.out.print(stateMap.get("bot" + ConsumableInterface.getConsumableClassList().get(i).getSimpleName()));
                cNumber++;
            }
        }

        // Showing human lives and state (shield, poison)
        setCursorPos(21, 2);
        System.out.print("You: ");
        for(int i = 0; i < Integer.parseInt(stateMap.get("humanPlayerLives")); i++) {
            System.out.print(ANSI_RED + "♥" + ANSI_RESET);
        }
        if(stateMap.get("isHumanPoisoned").equals("true")) {
            System.out.print(" A");
        }
        if(stateMap.get("isHumanShielded").equals("true")) {
            System.out.print(" S");
        }
        if(stateMap.get("isHumanHandcuffed").equals("true")) {
            System.out.print(" M");
        }

        // Showing human consumables
        cNumber = 0;
        for (int i = 0; i < ConsumableInterface.getConsumableStringList().size(); i++) {
            setCursorPos(23+cNumber, 2);
            if (stateMap.get("human" + ConsumableInterface.getConsumableClassList().get(i).getSimpleName()) != null) {
                System.out.print(letters.charAt(i) + ". " + ConsumableInterface.getConsumableStringList().get(i) + ": x");
                System.out.print(stateMap.get("human" + ConsumableInterface.getConsumableClassList().get(i).getSimpleName()));
                cNumber++;
            }
        }

        // Showing powerups
        setCursorPos(23, 27);
        System.out.print("Powerups:");
        for (int i = 0; i < PowerupInterface.getPowerupStringList().size(); i++) {
            setCursorPos(24+i, 27);
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
        try {
            clearScreen();
        } catch (IOException | InterruptedException e) {
            showError("clear");
        }

        drawTable();
        showGameState(stateMap);
        printLastAction();
    }

    public void showBullets(ArrayList<String> bullets) {
        customPrint("Here are the bullets: ", "slow", 16, 52);

        String m = "";
        for (String bullet : bullets) {
            if(bullet.equals("0")) {
                m += ANSI_CYAN;
            } else if(bullet.equals("1")) {
                m += ANSI_RED;
            }
            m += "█ " + ANSI_RESET;
        }
        customPrint(m, "fast", 17, 52);

        customPrint("I'm loading the gun...", "slow", 19, 52);

        try {
            Thread.sleep(2000);
        } catch (Exception e) {
            showError("sleep");
        }
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

    public void showEndGameScreen(String winner) {
        lastAction.add("The game is over...");
        lastAction.add("The winner is: " + winner + "!");
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

    public void showHandcuffedState(boolean state) {
        if(state) {
            lastAction.add("You are handcuffed, loose your turn!");
        } else {
            lastAction.add("The other player has been freed! Happy freedom!");
        }
    }

    public void showEnergyDrinkChoise() {
        lastAction.add("Choose the consumable to steal");
    }
}
