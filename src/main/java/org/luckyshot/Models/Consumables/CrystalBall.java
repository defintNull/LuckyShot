package org.luckyshot.Models.Consumables;

import org.luckyshot.Models.Enums.Probability;

public class CrystalBall extends Consumable {
    private static CrystalBall instance;
    private CrystalBall() {
        super(Probability.MEDIUM);
    }

    public static CrystalBall getInstance() {
        if(instance == null) {
            instance = new CrystalBall();
        }
        return instance;
    }

    public String toString() {
        return "Crystal ball";
    }
}
