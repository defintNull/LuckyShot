package org.luckyshot.Models.Consumables;

import org.luckyshot.Models.Enums.Probability;

public class Magnet extends Consumable{
    private static Magnet instance;
    private Magnet() {
        super(Probability.HIGH);
    }

    public static Magnet getInstance() {
        if(instance == null) {
            instance = new Magnet();
        }
        return instance;
    }
    public String toString() {
        return "Magnet";
    }
}
