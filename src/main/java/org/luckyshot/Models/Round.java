package org.luckyshot.Models;

import org.luckyshot.Models.StateEffects.StateEffect;

import java.util.Random;

public class Round {
    private int roundNumber;
    private StateEffect stateEffect;
    private Turn turn;

    public Round(int n, HumanPlayer player, BotPlayer bot) {
        Random rnd = new Random();
        int randomLives = rnd.nextInt(2, 5);
        player.setLives(randomLives);
        bot.setLives(randomLives);

        this.roundNumber = n;
        this.turn = new Turn(player);

        //DA VEDERE SE INIZIALIZZARE GIA CON STATO O NO
        this.stateEffect = null;
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
