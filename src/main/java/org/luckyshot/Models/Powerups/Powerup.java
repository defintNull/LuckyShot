package org.luckyshot.Models.Powerups;

import org.luckyshot.Models.SinglePlayerGame;

public abstract class Powerup implements PowerupInterface{
    private int cost;

    protected  Powerup(int cost) {
        this.cost = cost;
    }

    public void use(SinglePlayerGame singlePlayerGame) {

    }

    public int getCost() {
        return cost;
    }

    public void setCost(int cost) {
        this.cost = cost;
    }

    public String toString() {
        return this.getClass().getSimpleName();
    }
}
