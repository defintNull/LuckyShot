package org.luckyshot.Models.Consumables;

import org.luckyshot.Models.Enums.Probability;

import java.util.ArrayList;

public abstract class Consumable implements ConsumableInterface{
    private Probability probability;

    public Consumable(Probability probability) {
        this.probability = probability;
    }

    public Probability getProbability() {
        return probability;
    }

    public String toString() {
        return this.getClass().getSimpleName();
    }
}
