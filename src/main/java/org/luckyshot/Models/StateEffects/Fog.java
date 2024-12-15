package org.luckyshot.Models.StateEffects;

public class Fog extends StateEffect{
    private static Fog instance;

    private Fog() {

    }

    public static Fog getInstance(Fog instance) {
        if(instance == null) {
            instance = new Fog();
        }
        return instance;
    }

    public String toString() {
        return "Fog";
    }
}
