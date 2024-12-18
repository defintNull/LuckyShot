package org.luckyshot.Models.StateEffects;

import org.luckyshot.Models.SinglePlayerGame;

public class GuardianAngel extends StateEffect{
    private static GuardianAngel instance;

    private GuardianAngel() {

    }

    public void use(SinglePlayerGame singlePlayerGame) {

    }

    public String getEffect() {
        return "The guardian angel protected a player!";
    }

    public static GuardianAngel getInstance() {
        if(instance == null) {
            instance = new GuardianAngel();
        }
        return instance;
    }

    public String toString() {
        return "Guardian angel";
    }
}
