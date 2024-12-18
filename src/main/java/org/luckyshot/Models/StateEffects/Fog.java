package org.luckyshot.Models.StateEffects;

import org.luckyshot.Models.SinglePlayerGame;

public class Fog extends StateEffect{
    private static Fog instance;

    private Fog() {

    }

    public void use(SinglePlayerGame singlePlayerGame) {

    }

    public String getEffect() {
        return "There's some fog on the field!";
    }

    public static Fog getInstance() {
        if(instance == null) {
            instance = new Fog();
        }
        return instance;
    }

    public String toString() {
        return "Fog";
    }
}
