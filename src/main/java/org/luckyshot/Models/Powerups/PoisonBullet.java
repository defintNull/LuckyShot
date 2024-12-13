package org.luckyshot.Models.Powerups;

import org.luckyshot.Models.Consumables.CrystalBall;

public class PoisonBullet extends Powerup{
    private static PoisonBullet instance;
    private static final int COST = 1;

    private PoisonBullet() {
        super(COST);
    }

    public static PoisonBullet getInstance() {
        if(instance == null) {
            instance = new PoisonBullet();
        }
        return instance;
    }

    public String toString() {
        return "Poison Bullet";
    }
}
