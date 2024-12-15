package org.luckyshot.Views;

import org.checkerframework.checker.units.qual.A;
import org.luckyshot.Models.Bullet;
import org.luckyshot.Models.Consumables.Consumable;
import org.luckyshot.Models.Consumables.ConsumableInterface;

import java.io.IOException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Objects;

public class SinglePlayerGameView extends GameView{
    public void drawTable() {
        try {
            clearScreen();
        } catch (IOException | InterruptedException e) {
            System.out.println("Errore clear");
        }
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
        System.out.println();

        int c = 0;
        for (int i = 0; i < ConsumableInterface.getConsumableStringList().size(); i++) {
            setCursorPos(6+i, 2);
            if (stateMap.get("bot" + ConsumableInterface.getConsumableStringList().get(i)) != null) {
                try {
                    System.out.print(letters.charAt(i) + ". " + ConsumableInterface.getConsumableStringList().get(i) + ": x");
                    System.out.println(stateMap.get("bot" + ConsumableInterface.getConsumableStringList().get(i)));
                } catch (Exception e) {
                    System.out.print(e.getMessage());
                }
                c += 1;
            }
        }

        setCursorPos(21, 2);
        System.out.print("You: ");
        for(int i = 0; i < Integer.parseInt(stateMap.get("humanPlayerLives")); i++) {
            System.out.print(ANSI_RED + "♥" + ANSI_RESET);
        }
        System.out.println();

        c = 0;
        for (int i = 0; i < ConsumableInterface.getConsumableStringList().size(); i++) {
            setCursorPos(23+i, 2);
            if (stateMap.get("human" + ConsumableInterface.getConsumableStringList().get(i)) != null) {
                try {
                    Method method = Class.forName(ConsumableInterface.getConsumableClassList().get(i).getName()).getMethod("getInstance");
                    Object obj = method.invoke(null);
                    String n = ((Consumable) obj).toString();
                    System.out.print(letters.charAt(i) + ". " + n + ": x");
                    System.out.println(stateMap.get("human" + ConsumableInterface.getConsumableStringList().get(i)));
                } catch (Exception e) {
                    System.out.print(e.getMessage());
                }
            }
        }

        setCursorPos(4, 52);
        if(stateMap.get("turn").equals("HumanPlayer")) {
            System.out.println("It's your turn.");
        } else {
            System.out.println("It's your opponent's turn.");
        }
    }

    public void showBullets(ArrayList<String> bullets) {
        setCursorPos(4, 52);
        slowPrint("Here are the bullets: ");

        for (String bullet : bullets) {
            if(bullet.equals("0")) {
                System.out.print(ANSI_CYAN);
            } else if(bullet.equals("1")) {
                System.out.print(ANSI_RED);
            }
            System.out.print("█ " + ANSI_RESET);
        }
        System.out.println();
        setCursorPos(5, 52);
        try {
            Thread.sleep(1000);
        } catch (Exception e) {
            System.out.println("Error while sleep");
        }

        slowPrint("I'm loading the gun...");
        try {
            Thread.sleep(1000);
        } catch (Exception e) {
            System.out.println("Error while sleep");
        }
    }

    public int askPowerup() {
        System.out.println("Do you want to use any powerups?");
        return getUserInput();
    }

    public void showPowerups(ArrayList<HashMap<String, String>> powerups) {
        setCursorPos(23, 30);
        System.out.println("Powerups:");
        int i = 0;
        for(HashMap<String, String> element : powerups) {
            setCursorPos(24+i, 30);
            System.out.println(element.get("name") + ": " + element.get("occurrences"));
            i++;
        }
    }
}
