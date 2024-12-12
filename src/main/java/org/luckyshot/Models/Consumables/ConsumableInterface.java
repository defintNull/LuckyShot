package org.luckyshot.Models.Consumables;

import java.util.ArrayList;

public interface ConsumableInterface {
    static ArrayList<String> getConsumableStringList() {
        ArrayList<String> list = new ArrayList<String>();
        list.add(CrystalBall.class.getSimpleName());
        list.add(EnergyDrink.class.getSimpleName());
        list.add(GhostGun.class.getSimpleName());
        list.add(Glasses.class.getSimpleName());
        list.add(Handcuffs.class.getSimpleName());
        list.add(HealthPotion.class.getSimpleName());
        list.add(Inverter.class.getSimpleName());
        list.add(Magnet.class.getSimpleName());
        list.add(MisteryPotion.class.getSimpleName());
        return list;
    }

    static ArrayList<Class<? extends Consumable>> getConsumableClassList() {
        ArrayList<Class<? extends Consumable>> list = new ArrayList<Class<? extends Consumable>>();
        list.add(CrystalBall.class);
        list.add(EnergyDrink.class);
        list.add(GhostGun.class);
        list.add(Glasses.class);
        list.add(Handcuffs.class);
        list.add(HealthPotion.class);
        list.add(Inverter.class);
        list.add(Magnet.class);
        list.add(MisteryPotion.class);
        return list;
    }
}
