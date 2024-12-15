package org.luckyshot.Models;

public class Turn {
    private int phase;
    private Gun gun;
    private Player player;
    private boolean isBulletPoisoned;

    public Turn(Player currentPlayer) {
        Gun gun = Gun.getInstance();
        this.player = currentPlayer;
        isBulletPoisoned = false;
    }

    public Player getPlayer() {
        return player;
    }

    public void setPlayer(Player player) {
        this.player = player;
    }

    public int getPhase() {
        return phase;
    }
    public void setPhase(int phase) {
        this.phase = phase;
    }

    public boolean isBulletPoisoned() {
        return isBulletPoisoned;
    }

    public void setBulletPoisoned(boolean bulletPoisoned) {
        isBulletPoisoned = bulletPoisoned;
    }
}
