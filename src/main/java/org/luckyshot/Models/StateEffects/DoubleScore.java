package org.luckyshot.Models.StateEffects;

import org.luckyshot.Models.SinglePlayerGame;

public class DoubleScore extends StateEffect {
    private static DoubleScore instance;

    private DoubleScore() {

    }

    public String getActivation() {
        return "The score now is doubled!";
    }

    public String getEffect() {
        return "Your score was doubled!";
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
