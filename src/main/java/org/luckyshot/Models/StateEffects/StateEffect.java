package org.luckyshot.Models.StateEffects;

import org.luckyshot.Models.SinglePlayerGame;

public abstract class StateEffect {
    public void use(SinglePlayerGame singlePlayerGame) {

    }
    public String toString() {
        return this.getClass().getSimpleName();
    }

    public String getEffect() {
        return "no effect";
    }
}
