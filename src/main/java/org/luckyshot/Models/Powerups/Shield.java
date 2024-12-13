package org.luckyshot.Models.Powerups;

import org.luckyshot.Models.Consumables.CrystalBall;

public class Shield extends Powerup{
    private static Shield instance;
    private static final int COST = 1;

    private Shield() {
        super(COST);
    }

    public static Shield getInstance() {
        if(instance == null) {
            instance = new Shield();
        }
        return instance;
    }

    public String toString() {
        return "Shield";
    }
}
