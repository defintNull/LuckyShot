package org.luckyshot.Models.Consumables;

public class EnergyDrink extends Consumable{
    private static EnergyDrink instance;
    private EnergyDrink() {

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
