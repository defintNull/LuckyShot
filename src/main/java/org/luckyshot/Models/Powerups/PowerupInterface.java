package org.luckyshot.Models.Powerups;

import org.luckyshot.Models.Consumables.*;

import java.util.ArrayList;

public interface PowerupInterface {
    static ArrayList<String> getConsumableStringList() {
        ArrayList<String> list = new ArrayList<String>();
        list.add(Bomb.class.getSimpleName());
        list.add(PoisonBullet.class.getSimpleName());
        list.add(Shield.class.getSimpleName());
        return list;
    }

    static ArrayList<Class<? extends Powerup>> getConsumableClassList() {
        ArrayList<Class<? extends Powerup>> list = new ArrayList<Class<? extends Powerup>>();
        list.add(Bomb.class);
        list.add(PoisonBullet.class);
        list.add(Shield.class);
        return list;
    }
}
