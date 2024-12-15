package org.luckyshot.Models;

import org.luckyshot.Models.StateEffects.StateEffect;

import javax.swing.plaf.nimbus.State;
import java.util.Random;

public class Round {
    private int roundNumber;
    private StateEffect stateEffect;
    private Turn turn;
    public Round(int n, StateEffect stateEffect) {
        this.roundNumber = n;
        this.stateEffect = stateEffect;
    }

    public Turn getTurn() {
        return turn;
    }

    public void setTurn(Turn turn) {
        this.turn = turn;
    }

    public int getRoundNumber() {
        return roundNumber;
    }

    public void setRoundNumber(int roundNumber) {
        this.roundNumber = roundNumber;
    }

    public StateEffect getStateEffect() {
        return stateEffect;
    }

    public void setStateEffect(StateEffect stateEffect) {
        this.stateEffect = stateEffect;
    }
}
