package org.luckyshot.Models;

public class HumanPlayer extends Player{
    private int score;
    private double multiplier;
    private int comboCounter;

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
}
