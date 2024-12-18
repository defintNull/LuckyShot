package org.luckyshot.Models.Consumables;

import org.luckyshot.Models.Enums.Probability;
import org.luckyshot.Models.Gun;
import org.luckyshot.Models.SinglePlayerGame;

public class Glasses extends Consumable{
    private static Glasses instance;
    private Glasses() {
        super(Probability.LOW);
    }

    public static Glasses getInstance() {
        if(instance == null) {
            instance = new Glasses();
        }
        return instance;
    }

    public String use(SinglePlayerGame singlePlayerGame) {
        int type = Gun.getInstance().getBullet(Gun.getInstance().getBullets().size() - 1).getType();
        return String.valueOf(type);
    }

    public String getEffect(String effect) {
        return "The next bullet is " + (effect == "1" ? "live" : "fake");
    }

    public String toString() {
        return "Glasses";
    }
}
