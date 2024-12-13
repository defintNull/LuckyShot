package org.luckyshot.Models.Powerups;

public abstract class Powerup implements PowerupInterface{
    private int cost;

    protected  Powerup(int cost) {
        this.cost = cost;
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
