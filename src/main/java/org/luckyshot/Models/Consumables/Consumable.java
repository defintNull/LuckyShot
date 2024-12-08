package org.luckyshot.Models.Consumables;

public abstract class Consumable {
    private String name;
    private double probability;

    public double getProbability() {
        return probability;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setProbability(double probability) {
        this.probability = probability;
    }
}
