package org.luckyshot.Models.Consumables;

public class Inverter extends Consumable{
    private static Inverter instance;
    private Inverter() {

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
