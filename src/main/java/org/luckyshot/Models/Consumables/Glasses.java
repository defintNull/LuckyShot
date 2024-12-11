package org.luckyshot.Models.Consumables;

public class Glasses extends Consumable{
    private static Glasses instance;
    private Glasses() {

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
