package org.luckyshot.Models;

import org.luckyshot.Models.Consumables.Consumable;
import org.luckyshot.Models.Powerups.Powerup;

import java.util.ArrayList;
import java.util.HashMap;

public class HumanPlayer extends Player{
    private int score;
    private double multiplier;
    private int comboCounter;
    private HashMap<Powerup, Integer> powerups;
    private int xp;

    public HumanPlayer(HashMap<Powerup, Integer> powerups) {
        setPowerups(powerups);
        this.xp = 0;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public double getMultiplier() {
        return multiplier;
    }

    public void setMultiplier(double multiplier) {
        this.multiplier = multiplier;
    }

    public int getComboCounter() {
        return comboCounter;
    }

    public void setComboCounter(int comboCounter) {
        this.comboCounter = comboCounter;
    }

    public void setPowerups(HashMap<Powerup, Integer> powerups) {
        this.powerups = powerups;
    }

    public HashMap<Powerup, Integer> getPowerups() {
        return powerups;
    }

    public int getXp() {
        return xp;
    }

    public void setXp(int xp) {
        this.xp = xp;
    }

    public void addXp(int xp) {
        this.xp += xp;
    }
}
