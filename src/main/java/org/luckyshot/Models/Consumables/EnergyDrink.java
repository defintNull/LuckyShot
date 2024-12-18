package org.luckyshot.Models.Consumables;

import org.luckyshot.Models.Enums.Probability;
import org.luckyshot.Models.SinglePlayerGame;

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

    public String use(SinglePlayerGame singlePlayerGame) {
        return "";
    }

    public String getEffect() {
        return "Now you can steal a consumable...";
    }

    public String toString() {
        return "Energy drink";
    }
}
