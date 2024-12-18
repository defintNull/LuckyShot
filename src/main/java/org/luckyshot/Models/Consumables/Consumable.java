package org.luckyshot.Models.Consumables;

import org.luckyshot.Models.Enums.Probability;
import org.luckyshot.Models.SinglePlayerGame;

import java.util.ArrayList;

public abstract class Consumable implements ConsumableInterface{
    private int probability;

    protected Consumable(Probability probability) {
        this.probability = probabilityRange.get(probability);
    }

    public int getProbability() {
        return probability;
    }

    public String toString() {
        return this.getClass().getSimpleName();
    }

    public String use(SinglePlayerGame singlePlayerGame) {
        return "Nothing happened!";
    }

    public String getEffect(String parameters) {
        return "No effect";
    }
}
