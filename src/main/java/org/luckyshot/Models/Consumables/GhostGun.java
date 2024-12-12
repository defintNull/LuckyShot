package org.luckyshot.Models.Consumables;

import org.luckyshot.Models.Enums.Probability;

public class GhostGun extends Consumable{
    private static GhostGun instance;
    private GhostGun() {
        super(Probability.MEDIUM_HIGH);
    }

    public static GhostGun getInstance() {
        if(instance == null) {
            instance = new GhostGun();
        }
        return instance;
    }
    public String toString() {
        return "Ghost gun";
    }
}
