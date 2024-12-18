package org.luckyshot.Models.StateEffects;

import org.luckyshot.Models.SinglePlayerGame;

public class Antidote extends StateEffect{
    private static Antidote instance;

    private Antidote() {

    }

    public void use(SinglePlayerGame singlePlayerGame) {

    }

    public String getEffect() {
        return "No potion will affect any player!";
    }

    public static Antidote getInstance() {
        if(instance == null) {
            instance = new Antidote();
        }
        return instance;
    }

    public String toString() {
        return "Antidote";
    }
}
