package org.luckyshot.Models.Consumables;

public class MisteryPotion extends Consumable{
    private static MisteryPotion instance;
    private MisteryPotion() {

    }

    public static MisteryPotion getInstance() {
        if(instance == null) {
            instance = new MisteryPotion();
        }
        return instance;
    }
    public String toString() {
        return "Mistery potion";
    }
}
