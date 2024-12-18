package org.luckyshot.Models.Consumables;

import org.luckyshot.Models.Enums.Probability;
import org.luckyshot.Models.Gun;
import org.luckyshot.Models.SinglePlayerGame;

public class Handcuffs extends Consumable{

    private static Handcuffs instance;
    private Handcuffs() {
        super(Probability.LOW);
    }

    public static Handcuffs getInstance() {
        if(instance == null) {
            instance = new Handcuffs();
        }
        return instance;
    }

    public String use(SinglePlayerGame singlePlayerGame) {
        singlePlayerGame.getRound().getTurn().getOtherPlayer().setHandcuffed(true);
        return "";
    }

    public String getEffect() {
        return "Your opponent is handcuffed...";
    }

    public String toString() {
        return "Handcuffs";
    }
}
