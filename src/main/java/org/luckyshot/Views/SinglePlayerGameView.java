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
    ArrayList<ArrayList<String>> messages = new ArrayList<>();
    HashMap<String, String> stateMap = new HashMap<>();

    public void debugSleep() {
        try {
            Thread.sleep(10000);
        } catch (Exception e) {
            System.out.println("Errore debug");
        }
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
        //clearMessages();

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

        if(stateMap.get("turn").equals("HumanPlayer")) {
            addMessageOnTop("It's your turn.", "fast");
        } else {
            addMessageOnTop("It's your opponent's turn.", "fast");
        }

        printMessages();
    }

    public void showGame(HashMap<String, String> stateMap) {
        showGameState(stateMap);
        printMessages();
    }

    public void printMessages() {
        for(int i = 0; i < messages.size(); i++) {
            setCursorPos(4+i, 52);
            if(messages.get(i).get(1).equals("fast")) {
                System.out.print(messages.get(i).getFirst());
            } else if(messages.get(i).get(1).equals("slow")) {
                slowPrint(messages.get(i).getFirst());
            }
        }
    }

    public void showBullets(ArrayList<String> bullets) {
        setCursorPos(4, 52);
        addMessage("Here are the bullets: ", "slow");

        String m = "";
        for (String bullet : bullets) {
            if(bullet.equals("0")) {
                m += ANSI_CYAN;
            } else if(bullet.equals("1")) {
                m += ANSI_RED;
            }
            m += "█ " + ANSI_RESET;
        }
        addMessage(m, "fast");

        addMessage("I'm loading the gun...", "slow");

        printMessages();
        try {
            Thread.sleep(2000);
        } catch (Exception e) {
            clearMessages();
            setCursorPos(4, 52);
            System.out.println("Error while sleep");
        }
        clearMessages();
    }

    public void showShootingResult(int bulletType) {
        if(bulletType == 0) {
            addMessage("The bullet was fake", "fast");
        } else if(bulletType == 1) {
            addMessage("The bullet was live", "fast");
        }
    }

    public void showPowerupActivation(String powerup) {
        addMessage("A " + powerup.toLowerCase() + " has been used!", "fast");
        printMessages();
    }

    public ArrayList<ArrayList<String>> getMessages() {
        return messages;
    }

    public void setMessages(ArrayList<ArrayList<String>> messages) {
        this.messages = messages;
    }

    public void addMessage(String message, String speed) {
        messages.add(new ArrayList<>(Arrays.asList(message, speed)));
    }

    public void addMessageOnTop(String message, String speed) {
        messages.addFirst(new ArrayList<>(Arrays.asList(message, speed)));
    }

    public void clearMessages() {
        messages.clear();
        try {
            clearScreen();
        } catch (IOException | InterruptedException e) {
            System.out.println("Errore clear");
        }
        showGameState(stateMap);
    }
}
