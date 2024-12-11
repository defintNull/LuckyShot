package org.luckyshot.Views;

import org.luckyshot.Models.Consumables.Consumable;

import java.io.IOException;
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
        for(int i = 0; i < Consumable.getConsumableList().size(); i++){
            if(stateMap.get("bot" + Consumable.getConsumableList().get(i).toString()) != null) {
                System.out.print(Consumable.getConsumableList().get(i).getSimpleName() + ": x");
                System.out.println(stateMap.get("bot" + Consumable.getConsumableList().get(i).getSimpleName()));
            }
        }
        System.out.println();

        System.out.println("Human has: ");
        for(int i = 0; i < Consumable.getConsumableList().size(); i++){
            if(stateMap.get("human" + Consumable.getConsumableList().get(i).toString()) != null) {
                System.out.print(Consumable.getConsumableList().get(i).getSimpleName() + ": x");
                System.out.println(stateMap.get("human" + Consumable.getConsumableList().get(i).getSimpleName()));
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
}
