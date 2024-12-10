package org.luckyshot.Models.Consumables;

import java.util.ArrayList;

public abstract class Consumable {
    private String name;
    private double probability;

    public double getProbability() {
        return probability;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setProbability(double probability) {
        this.probability = probability;
    }

    public static ArrayList<Object> getConsumableList() {
        ArrayList<Object> list = new ArrayList<Object>();
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
