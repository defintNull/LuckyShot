package org.luckyshot.Models;

import org.luckyshot.Models.Consumables.Consumable;
import org.luckyshot.Models.Powerups.Powerup;

import java.util.ArrayList;
import java.util.HashMap;

public abstract class Player {
    private int lives;
    private ArrayList<String> consumables;


    public Player() {
        consumables = new ArrayList<>();
    }

    public int getLives() {
        return lives;
    }

    public void setLives(int lives) {
        this.lives = lives;
    }

    public ArrayList<String> getConsumables() {
        return consumables;
    }

    public void setConsumables(ArrayList<String> consumables) {
        this.consumables = consumables;
    }

    public int getConsumablesNumber() {
        return consumables.size();
    }
}
