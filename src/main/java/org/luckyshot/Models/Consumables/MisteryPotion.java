package org.luckyshot.Models.Consumables;

import org.luckyshot.Models.Enums.Probability;
import org.luckyshot.Models.SinglePlayerGame;

import java.util.Random;

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

    public String use(SinglePlayerGame singlePlayerGame) {
        Random rand = new Random();
        int lives = singlePlayerGame.getRound().getTurn().getCurrentPlayer().getLives();
        if(rand.nextInt(0, 2) == 0) {
            if(lives + 1 < singlePlayerGame.getRound().getMaxLives()) {
                singlePlayerGame.getRound().getTurn().getCurrentPlayer().setLives(lives + 1);
            }
        } else {
            singlePlayerGame.getRound().getTurn().getCurrentPlayer().setLives(lives - 2);
        }

        return "";
    }

    public String getEffect() {
        return "Hope to stay alive...";
    }

    public String toString() {
        return "Mistery potion";
    }
}
