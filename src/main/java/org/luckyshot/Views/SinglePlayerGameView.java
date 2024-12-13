package org.luckyshot.Views;

import org.checkerframework.checker.units.qual.A;
import org.luckyshot.Models.Bullet;
import org.luckyshot.Models.Consumables.ConsumableInterface;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

public class SinglePlayerGameView extends GameView{
    public void showGameState(HashMap<String, String> stateMap) {
        try {
            clearScreen();
        } catch (IOException | InterruptedException e) {
            System.out.println("Errore clear");
        }
        displayHeader();

        System.out.println("Round " + stateMap.get("roundNumber"));

        System.out.print("Bot: ");
        for(int i = 0; i < Integer.parseInt(stateMap.get("botLives")); i++) {
            System.out.print("♥");
        }
        System.out.print("\t\t\t");
        System.out.print("You: ");
        for(int i = 0; i < Integer.parseInt(stateMap.get("humanPlayerLives")); i++) {
            System.out.print("♥");
        }
        System.out.println("\t\t\t");


        int c = 0;
        System.out.print("Bot has: ");
        for (int i = 0; i < ConsumableInterface.getConsumableStringList().size(); i++) {
            if (stateMap.get("bot" + ConsumableInterface.getConsumableStringList().get(i)) != null) {
                System.out.print(ConsumableInterface.getConsumableStringList().get(i) + ": x");
                System.out.println(stateMap.get("bot" + ConsumableInterface.getConsumableStringList().get(i)));
                c += 1;
            }
        }
        if(c == 0) {
            System.out.println("no consumables");
        }
        System.out.println();

        System.out.print("Human has: ");
        c = 0;
        for (int i = 0; i < ConsumableInterface.getConsumableStringList().size(); i++) {
            if (stateMap.get("human" + ConsumableInterface.getConsumableStringList().get(i)) != null) {
                System.out.print(ConsumableInterface.getConsumableStringList().get(i) + ": x");
                System.out.println(stateMap.get("human" + ConsumableInterface.getConsumableStringList().get(i)));
                c += 1;
            }
        }
        if(c == 0) {
            System.out.println("no consumables");
        }


        if(stateMap.get("stateEffect").equals("none")) {
            System.out.println("State effect " + stateMap.get("stateEffect"));
        } else {
            System.out.println("State effect: No state effect");
        }

        if(stateMap.get("turn").equals("HumanPlayer")) {
            System.out.println("It's your turn.");
        } else {
            System.out.println("It's your opponent's turn.");
        }

    }

    public void showBullets(ArrayList<String> bullets) {
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
        System.out.println("Powerups:");
        for(HashMap<String, String> element : powerups) {
            System.out.println(element.get("name") + ": " + element.get("occurrences"));
        }
    }
}
