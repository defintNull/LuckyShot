package org.luckyshot.Views;

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
