package org.luckyshot.Models.StateEffects;

public class DoubleScore extends StateEffect {
    private static DoubleScore instance;

    private DoubleScore() {

    }

    public static DoubleScore getInstance(DoubleScore instance) {
        if(instance == null) {
            instance = new DoubleScore();
        }
        return instance;
    }
}
