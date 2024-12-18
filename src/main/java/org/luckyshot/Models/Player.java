package org.luckyshot.Models;

import org.luckyshot.Models.Consumables.Consumable;
import org.luckyshot.Models.Powerups.Powerup;

import java.util.ArrayList;
import java.util.HashMap;

public abstract class Player {
    private int lives;
    private ArrayList<Consumable> consumables;
    private boolean isShieldActive;
    private boolean isPoisoned;
    private boolean isHandcuffed;

    public Player() {
        consumables = new ArrayList<>();
    }

    public int getLives() {
        return lives;
    }

    public void setLives(int lives) {
        this.lives = lives;
    }

    public ArrayList<Consumable> getConsumables() {
        return consumables;
    }

    public void setConsumables(ArrayList<Consumable> consumables) {
        this.consumables = consumables;
    }

    public void removeConsumable(Consumable consumable) {
        this.consumables.remove(consumable);
    }

    public int getConsumablesNumber() {
        return consumables.size();
    }

    public boolean isShieldActive() {
        return isShieldActive;
    }

    public void setShieldActive(boolean shieldActive) {
        isShieldActive = shieldActive;
    }

    public boolean isPoisoned() {
        return isPoisoned;
    }

    public void setPoisoned(boolean poisoned) {
        isPoisoned = poisoned;
    }

    public boolean isHandcuffed() {
        return isHandcuffed;
    }

    public void setHandcuffed(boolean handcuffed) {
        isHandcuffed = handcuffed;
    }
}
