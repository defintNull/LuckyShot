package org.luckyshot.Models.Powerups;

import org.luckyshot.Models.Consumables.CrystalBall;

public class Bomb extends Powerup{
    private static Bomb instance;
    private static final int COST = 1;

    private Bomb() {
        super(COST);
    }

    public static Bomb getInstance() {
        if(instance == null) {
            instance = new Bomb();
        }
        return instance;
    }

    public String toString() {
        return "Bomb";
    }
}
