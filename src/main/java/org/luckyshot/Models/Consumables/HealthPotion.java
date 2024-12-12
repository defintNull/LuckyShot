package org.luckyshot.Models.Consumables;

import org.luckyshot.Models.Enums.Probability;

public class HealthPotion extends Consumable{

    private static HealthPotion instance;
    private HealthPotion() {
        super(Probability.LOW);
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
