package org.luckyshot.Models.StateEffects;

public class DoubleScore extends StateEffect {
    private static DoubleScore instance;

    private DoubleScore() {

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
