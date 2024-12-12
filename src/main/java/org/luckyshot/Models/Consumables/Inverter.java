package org.luckyshot.Models.Consumables;

import org.luckyshot.Models.Enums.Probability;

public class Inverter extends Consumable{
    private static Inverter instance;
    private Inverter() {
        super(Probability.HIGH);
    }

    public static Inverter getInstance() {
        if(instance == null) {
            instance = new Inverter();
        }
        return instance;
    }
    public String toString() {
        return "Inverter";
    }
}
