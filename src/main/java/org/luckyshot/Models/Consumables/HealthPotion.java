package org.luckyshot.Models.Consumables;

public class HealthPotion extends Consumable{

    private static HealthPotion instance;
    private HealthPotion() {

    }

    public static HealthPotion getInstance() {
        if(instance == null) {
            instance = new HealthPotion();
        }
        return instance;
    }
    public String toString() {
        return "Health potion";
    }
}
