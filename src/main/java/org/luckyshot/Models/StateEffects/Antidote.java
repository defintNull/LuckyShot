package org.luckyshot.Models.StateEffects;

public class Antidote extends StateEffect{
    private static Antidote instance;

    private Antidote() {

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
