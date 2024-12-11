package org.luckyshot.Models.Consumables;

public class CrystalBall extends Consumable {
    private static CrystalBall instance;
    private CrystalBall() {

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
