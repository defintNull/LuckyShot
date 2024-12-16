package org.luckyshot.Views;

import org.checkerframework.checker.units.qual.A;
import org.luckyshot.Models.Bullet;
import org.luckyshot.Models.Consumables.Consumable;
import org.luckyshot.Models.Consumables.ConsumableInterface;
import org.luckyshot.Models.Powerups.PowerupInterface;

import java.io.IOException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Objects;

public class SinglePlayerGameView extends GameView{
    //ArrayList<ArrayList<String>> messages = new ArrayList<>();
    HashMap<String, String> stateMap = new HashMap<>();
    String lastAction;

    public void debugSleep() {
        try {
            Thread.sleep(10000);
        } catch (Exception e) {
            System.out.println("Errore debug");
        }
    }

    public void showError(String s) {
        setCursorPos(27, 52);
        System.out.print(ANSI_RED + "ERROR: " + s + ANSI_RESET);
    }

    public void debug(String s) {
        setCursorPos(28, 52);
        System.out.print(ANSI_CYAN + "DEBUG: " + s + ANSI_RESET);
    }

    public void drawTable() {
        displayHeader();

        for(int i = 0; i < 29; i++) {
            setCursorPos(4+i, 1);
            System.out.println("║");
        }
        for(int i = 0; i < 29; i++) {
            setCursorPos(4+i, 100);
            System.out.println("║");
        }

        for(int i = 0; i < 29; i++) {
            setCursorPos(4+i, 51);
            System.out.println("│");
        }

        setCursorPos(5, 1);
        System.out.println("╟" + "─".repeat(49) + "┤");

        setCursorPos(22, 1);
        System.out.println("╟" + "─".repeat(49) + "┤");

        setCursorPos(15, 1);
        System.out.println("╟" + "─".repeat(49) + "┤");
        setCursorPos(16, 22);
        System.out.print("__,_____");
        setCursorPos(17, 21);
        System.out.print("/ __.==--\"");
        setCursorPos(18, 20);
        System.out.print("/#(-'");
        setCursorPos(19, 20);
        System.out.print("`-'");

        setCursorPos(20, 1);
        System.out.println("╟" + "─".repeat(49) + "┤");
        setCursorPos(33, 1);
        System.out.println("╚" + "═".repeat(98) + "╝");

        setCursorPos(3, 51);
        System.out.print("╤");

        setCursorPos(33, 51);
        System.out.println("╧");
    }

    public void showGameState(HashMap<String, String> stateMap) {
        this.stateMap = stateMap;
        try {
            clearScreen();
        } catch (IOException | InterruptedException e) {
            System.out.println("Errore clear");
        }

        drawTable();

        String letters = "abcdefghi";
        setCursorPos(2, 2);
        System.out.println("Round " + stateMap.get("roundNumber"));

        setCursorPos(2, 40);
        System.out.println("State effect: " + stateMap.get("stateEffect"));

        setCursorPos(4, 2);
        System.out.print("Bot: ");
        for(int i = 0; i < Integer.parseInt(stateMap.get("botLives")); i++) {
            System.out.print(ANSI_RED + "♥" + ANSI_RESET);
        }
        if(stateMap.get("isBotPoisoned").equals("true")) {
            System.out.print(" A");
        }
        System.out.println();

        for (int i = 0; i < ConsumableInterface.getConsumableStringList().size(); i++) {
            setCursorPos(6+i, 2);
            if (stateMap.get("bot" + ConsumableInterface.getConsumableClassList().get(i).getSimpleName()) != null) {
                System.out.print(letters.charAt(i) + ". " + ConsumableInterface.getConsumableStringList().get(i) + ": x");
                System.out.println(stateMap.get("bot" + ConsumableInterface.getConsumableClassList().get(i).getSimpleName()));
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

        // Showing human powerups
        for (int i = 0; i < ConsumableInterface.getConsumableStringList().size(); i++) {
            setCursorPos(23+i, 2);
            if (stateMap.get("human" + ConsumableInterface.getConsumableClassList().get(i).getSimpleName()) != null) {
                System.out.print(letters.charAt(i) + ". " + ConsumableInterface.getConsumableStringList().get(i) + ": x");
                System.out.println(stateMap.get("human" + ConsumableInterface.getConsumableClassList().get(i).getSimpleName()));
            }
        }

        // Showing powerups
        setCursorPos(23, 27);
        System.out.println("Powerups:");
        for (int i = 0; i < PowerupInterface.getPowerupStringList().size(); i++) {
            setCursorPos(24+i, 27);
            System.out.print(i + 1 + ". " + PowerupInterface.getPowerupStringList().get(i) + ": x");
            System.out.println(stateMap.get(PowerupInterface.getPowerupStringList().get(i)));
        }

        setCursorPos(5, 52);
        if(stateMap.get("turn").equals("HumanPlayer")) {
            customPrint("It's your turn.", "fast");
        } else {
            customPrint("It's your opponent's turn.", "fast");
        }

        printLastAction();
    }

    public void printLastAction() {
        setCursorPos(4, 52);
        System.out.print(lastAction);
    }

    public void customPrint(String message, String speed) {
        if(speed.equals("fast")) {
            System.out.print(message);
        } else if(speed.equals("slow")) {
            slowPrint(message);
        }
    }

    public void showBullets(ArrayList<String> bullets) {
        setCursorPos(4, 52);
        lastAction = "Here are the bullets: ";

        String m = "";
        for (String bullet : bullets) {
            if(bullet.equals("0")) {
                m += ANSI_CYAN;
            } else if(bullet.equals("1")) {
                m += ANSI_RED;
            }
            m += "█ " + ANSI_RESET;
        }
        lastAction += m;

        lastAction += "I'm loading the gun...";

        try {
            Thread.sleep(2000);
        } catch (Exception e) {
            showError("sleep");
        }
    }

    public void showShootingResult(int bulletType) {
        if(bulletType == 0) {
            lastAction = "The bullet was fake";
        } else if(bulletType == 1) {
            lastAction = "The bullet was live";
        }
    }

    public void showPowerupActivation(String powerup) {
        lastAction = "A " + powerup.toLowerCase() + " has been used!";
    }
}
