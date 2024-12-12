package org.luckyshot.Models.Consumables;

import org.luckyshot.Models.Enums.Probability;

import java.util.ArrayList;

public abstract class Consumable implements ConsumableInterface{
    private int probability;

    public Consumable(Probability probability) {
        this.probability = probabilityRange.get(probability);
    }

    public int getProbability() {
        return probability;
    }

    public String toString() {
        return this.getClass().getSimpleName();
    }
}
