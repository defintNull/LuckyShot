package org.luckyshot.Models.Powerups;

import org.luckyshot.Models.Consumables.CrystalBall;
import org.luckyshot.Models.SinglePlayerGame;

public class Bomb extends Powerup{
    private static Bomb instance;
    private static final int COST = 1;

    private Bomb() {
        super(COST);
    }

    public void use(SinglePlayerGame singlePlayerGame) {
        singlePlayerGame.getHumanPlayer().setLives(singlePlayerGame.getHumanPlayer().getLives() - 1);
        singlePlayerGame.getBot().setLives(singlePlayerGame.getBot().getLives() - 1);

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

    public String getEffect() {
        return "Every players lose a life!";
    }
}
