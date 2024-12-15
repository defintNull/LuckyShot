package org.luckyshot.Models.StateEffects;

public class Antidote extends StateEffect{
    private static Antidote instance;

    private Antidote() {

    }

    public static Antidote getInstance(Antidote instance) {
        if(instance == null) {
            instance = new Antidote();
        }
        return instance;
    }
}
