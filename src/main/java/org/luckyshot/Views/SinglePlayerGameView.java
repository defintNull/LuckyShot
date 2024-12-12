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

        System.out.println("Bot lives: " + stateMap.get("botLives"));
        System.out.println("Your lives: " + stateMap.get("humanPlayerLives"));
        System.out.println("Round number: " + stateMap.get("roundNumber"));

        System.out.println("Bot has: ");
        for(int i = 0; i < ConsumableInterface.getConsumableStringList().size(); i++){
            if(stateMap.get("bot" + ConsumableInterface.getConsumableStringList().get(i)) != null) {
                System.out.print(ConsumableInterface.getConsumableStringList().get(i) + ": x");
                System.out.println(stateMap.get("bot" + ConsumableInterface.getConsumableStringList().get(i)));
            }
        }
        System.out.println();

        System.out.println("Human has: ");
        for(int i = 0; i < ConsumableInterface.getConsumableStringList().size(); i++){
            if(stateMap.get("human" + ConsumableInterface.getConsumableStringList().get(i)) != null) {
                System.out.print(ConsumableInterface.getConsumableStringList().get(i) + ": x");
                System.out.println(stateMap.get("human" + ConsumableInterface.getConsumableStringList().get(i)));
            }
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
}
