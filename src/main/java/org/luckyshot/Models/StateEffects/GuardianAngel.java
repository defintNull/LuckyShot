package org.luckyshot.Models.StateEffects;

public class GuardianAngel extends StateEffect{
    private static GuardianAngel instance;

    private GuardianAngel() {

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
