package org.luckyshot.Models.Powerups;

import org.luckyshot.Models.Consumables.*;
import org.luckyshot.Views.SinglePlayerGameView;

import java.lang.reflect.Method;
import java.util.ArrayList;

public interface PowerupInterface {
    static ArrayList<String> getPowerupStringList() {
        ArrayList<String> list = new ArrayList<String>();
        for(int i = 0; i < getPowerupClassList().size(); i++) {
            try {
                Method method = Class.forName(PowerupInterface.getPowerupClassList().get(i).getName()).getMethod("getInstance");
                Object obj = method.invoke(null);
                String n = ((Powerup) obj).toString();
                list.add(n);
            } catch (Exception e) {
                SinglePlayerGameView view = new SinglePlayerGameView();
                view.systemError();
            }
        }
        return list;
    }

    static ArrayList<Class<? extends Powerup>> getPowerupClassList() {
        ArrayList<Class<? extends Powerup>> list = new ArrayList<Class<? extends Powerup>>();
        list.add(Bomb.class);
        list.add(PoisonBullet.class);
        list.add(Shield.class);
        return list;
    }
}
