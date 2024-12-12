package org.luckyshot.Models.Consumables;

import org.luckyshot.Models.Enums.Probability;

public class Glasses extends Consumable{
    private static Glasses instance;
    private Glasses() {
        super(Probability.LOW);
    }

    public static Glasses getInstance() {
        if(instance == null) {
            instance = new Glasses();
        }
        return instance;
    }
    public String toString() {
        return "Glasses";
    }
}
