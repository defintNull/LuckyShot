package org.luckyshot.Models;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;

public class Gun {
    ArrayList<Bullet> bullets;
    private static Gun instance;

    private Gun() {
        bullets = new ArrayList<>();
    }

    public static Gun getInstance() {
        if(instance == null) {
            instance = new Gun();
        }
        return instance;
    }

    public Bullet popBullet() {
        return bullets.removeLast();
    }

    public boolean isEmpty() {
        return bullets.isEmpty();
    }

    public ArrayList<Bullet> generateBulletSequence() {
        Random rand = new Random();
        int n = rand.nextInt(2,9);

        ArrayList<Bullet> b = new ArrayList<>();
        b.add(new Bullet(1));
        b.add(new Bullet(0));

        for(int i = 2; i < n; i++) {
            b.add(new Bullet(rand.nextInt(0, 2)));
        }

        return b;
    }

    public Bullet getBullet(int i) {
        return bullets.get(i);
    }

    public ArrayList<Bullet> getBullets() {
        return bullets;
    }

    public void setBullets(ArrayList<Bullet> bullets) {
        Collections.shuffle(bullets);
        this.bullets = bullets;
    }
}
