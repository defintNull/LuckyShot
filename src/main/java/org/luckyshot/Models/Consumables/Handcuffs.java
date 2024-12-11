package org.luckyshot.Models.Consumables;

public class Handcuffs extends Consumable{

    private static Handcuffs instance;
    private Handcuffs() {

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
