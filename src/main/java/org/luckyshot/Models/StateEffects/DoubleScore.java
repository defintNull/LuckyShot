package org.luckyshot.Models.StateEffects;

import org.luckyshot.Models.SinglePlayerGame;

public class DoubleScore extends StateEffect {
    private static DoubleScore instance;

    private DoubleScore() {

    }

    public void use(SinglePlayerGame singlePlayerGame) {

    }

    public String getEffect() {
        return "The score is doubled!";
    }

    public static DoubleScore getInstance() {
        if(instance == null) {
            instance = new DoubleScore();
        }
        return instance;
    }

    public String toString() {
        return "Double score";
    }
}
