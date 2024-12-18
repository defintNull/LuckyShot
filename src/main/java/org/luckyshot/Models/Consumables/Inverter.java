package org.luckyshot.Models.Consumables;

import org.luckyshot.Models.Enums.Probability;
import org.luckyshot.Models.Gun;
import org.luckyshot.Models.SinglePlayerGame;

public class Inverter extends Consumable{
    private static Inverter instance;
    private Inverter() {
        super(Probability.HIGH);
    }

    public static Inverter getInstance() {
        if(instance == null) {
            instance = new Inverter();
        }
        return instance;
    }

    public String use(SinglePlayerGame singlePlayerGame) {
        int type = Gun.getInstance().getBullet(Gun.getInstance().getBullets().size() - 1).getType();
        Gun.getInstance().getBullet(Gun.getInstance().getBullets().size() - 1).setType((type + 1) % 2);
        return "";
    }

    public String getEffect(String effect) {
        return "Bullet inverted!";
    }

    public String toString() {
        return "Inverter";
    }
}
