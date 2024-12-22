package org.luckyshot.Models;

import java.util.Random;

public class BotPlayer extends Player{
    public String getConsumableInput() {
        //String options = "abcdefghi"
        Random rand = new Random();
        return "use " + rand.nextInt(1,9);
    }

    public String getShootingInput() {
        Random rand = new Random();
        return "shoot " + rand.nextInt(1, 3);
    }

    public String getInput() {
        Random rand = new Random();
        String output = "";
        if(rand.nextInt(2) == 0) {
            output = getShootingInput();
        } else {
            output = getShootingInput();
            //output = getConsumableInput();
        }
        return output;
    }

    public boolean isShieldActive() {
        return false;
    }
}
