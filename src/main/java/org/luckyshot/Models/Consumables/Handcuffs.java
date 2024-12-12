package org.luckyshot.Models.Consumables;

import org.luckyshot.Models.Enums.Probability;

public class Handcuffs extends Consumable{

    private static Handcuffs instance;
    private Handcuffs() {
        super(Probability.LOW);
    }

    public static Handcuffs getInstance() {
        if(instance == null) {
            instance = new Handcuffs();
        }
        return instance;
    }
    public String toString() {
        return "Handcuffs";
    }
}
