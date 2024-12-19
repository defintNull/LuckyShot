package org.luckyshot.Models.StateEffects;

import org.luckyshot.Models.SinglePlayerGame;

public abstract class StateEffect {

    public String toString() {
        return this.getClass().getSimpleName();
    }

    public String getEffect() {
        return "no effect";
    }

    public String getActivation() {
        return "No state effect for this round";
    }
}
