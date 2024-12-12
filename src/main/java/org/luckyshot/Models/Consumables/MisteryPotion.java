package org.luckyshot.Models.Consumables;

import org.luckyshot.Models.Enums.Probability;

public class MisteryPotion extends Consumable{
    private static MisteryPotion instance;
    private MisteryPotion() {
        super(Probability.MEDIUM);
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
