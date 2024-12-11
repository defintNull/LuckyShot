package org.luckyshot.Models.Consumables;

public class GhostGun extends Consumable{
    private static GhostGun instance;
    private GhostGun() {

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
