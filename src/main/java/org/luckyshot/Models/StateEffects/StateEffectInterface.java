package org.luckyshot.Models.StateEffects;

import org.luckyshot.Models.Powerups.Powerup;
import org.luckyshot.Models.Powerups.PowerupInterface;
import org.luckyshot.Views.SinglePlayerGameView;

import java.lang.reflect.Method;
import java.util.ArrayList;

public interface StateEffectInterface {
    static ArrayList<String> getStateEffectStringList() {
        ArrayList<String> list = new ArrayList<String>();
        for(int i = 0; i < getStateEffectClassList().size(); i++) {
            try {
                Method method = Class.forName(StateEffectInterface.getStateEffectClassList().get(i).getName()).getMethod("getInstance");
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

    static ArrayList<Class<? extends StateEffect>> getStateEffectClassList() {
        ArrayList<Class<? extends StateEffect>> list = new ArrayList<Class<? extends StateEffect>>();
        list.add(Antidote.class);
        list.add(DoubleScore.class);
        list.add(Fog.class);
        list.add(GuardianAngel.class);
        return list;
    }
}
