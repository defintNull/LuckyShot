package org.luckyshot.Models;

import java.util.Random;

public class BotPlayer extends Player{
    public String getConsumableInput() {
        Random rand = new Random();
        return "use " + rand.nextInt(1,9);
    }

    public String getShootingInput() {
        Random rand = new Random();
        return "shoot " + rand.nextInt(2);
    }

    public boolean isShieldActive() {
        return false;
    }
}
