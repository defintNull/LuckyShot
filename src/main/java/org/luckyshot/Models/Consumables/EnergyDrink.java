package org.luckyshot.Models.Consumables;

import org.luckyshot.Models.Enums.Probability;

public class EnergyDrink extends Consumable{
    private static EnergyDrink instance;
    private EnergyDrink() {
        super(Probability.MEDIUM);
    }

    public static EnergyDrink getInstance() {
        if(instance == null) {
            instance = new EnergyDrink();
        }
        return instance;
    }

    public String toString() {
        return "Energy drink";
    }
}
