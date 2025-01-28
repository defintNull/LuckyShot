package org.luckyshot.Models.Powerups;

import org.luckyshot.Models.Consumables.CrystalBall;
import org.luckyshot.Models.SinglePlayerGame;

public class Shield extends Powerup{
    private static Shield instance;
    private static final int COST = 15;

    private Shield() {
        super(COST);
    }

    public void use(SinglePlayerGame singlePlayerGame) {
        singlePlayerGame.getHumanPlayer().setShieldActive(true);
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

    public String getEffect() {
        return "The shield saved a life!";
    }
}