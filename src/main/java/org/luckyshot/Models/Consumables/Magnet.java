package org.luckyshot.Models.Consumables;

public class Magnet extends Consumable{
    private static Magnet instance;
    private Magnet() {

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
