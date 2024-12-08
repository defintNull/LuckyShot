package org.luckyshot.Models;

import java.util.Random;

public class BotPlayer extends Player{
    public int getConsumableInput() {
        Random rand = new Random();
        return rand.nextInt(1,9);
    }

    public int getShootingInput() {
        Random rand = new Random();
        return rand.nextInt(2);
    }
}
