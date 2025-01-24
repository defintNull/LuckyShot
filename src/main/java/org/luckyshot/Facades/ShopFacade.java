package org.luckyshot.Facades;

import org.luckyshot.Facades.Services.Client;
import org.luckyshot.Facades.Services.Converters.ObjectConverter;
import org.luckyshot.Models.Enums.MessageEnum;
import org.luckyshot.Models.Powerups.Powerup;
import org.luckyshot.Models.Powerups.PowerupInterface;
import org.luckyshot.Models.User;
import org.luckyshot.Views.ShopView;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;

public class ShopFacade {
    private static ShopFacade instance;
    private final ShopView shopView;
    private final User user;

    private ShopFacade(User user) {
        shopView = new ShopView();
        this.user = user;
    }

    public static ShopFacade getInstance(User user) {
        if (instance == null) {
            instance = new ShopFacade(user);
        }
        return instance;
    }

    public boolean shopMenu() {
        while(true) {
            showShop();
            String choice = shopView.getUserInput();
            ArrayList<String> choices = new ArrayList<>();
            boolean valid = false;
            for(int i = 0; i < PowerupInterface.getPowerupClassList().size(); i++) {
                choices.add(Integer.toString(i + 1));
                if(choice.equals(Integer.toString(i + 1))) {
                    valid = true;
                }
            }
            if(choice.equals("q")) {
                break;
            }
            if(!valid) {
                shopView.showError("Insert a valid choice...", 21, 2);
                continue;
            }
            if((Integer.parseInt(choice) < 1 || Integer.parseInt(choice) > PowerupInterface.getPowerupClassList().size())) {
                shopView.showError("The desired powerup doesn't exists...", 21, 2);
                continue;
            }
            // Compra il powerup scelto

            Class<? extends Powerup> chosenPowerup = PowerupInterface.getPowerupClassList().get(Integer.parseInt(choice) - 1);
            int price;
            Powerup powerup;
            try {
                Method method = Class.forName(chosenPowerup.getName()).getMethod("getInstance");
                Object obj = method.invoke(null);
                powerup = (Powerup) obj;
                price = powerup.getCost();
            } catch (Exception e) {
                shopView.showError("No method found", 21, 2);
                continue;
            }
            if(user.getCoins() < price) {
                shopView.showError("You can't afford that...", 21, 2);
                continue;
            }
            user.setCoins(user.getCoins() - price);
            user.addPowerup(powerup);

            Client client = Client.getInstance();
            ObjectConverter converter = new ObjectConverter();
            client.send("UPDATE_USER:" + converter.userToJson(user));
            ArrayList<String> recv = null;
            try {
                recv = client.recv();
            } catch (Exception e) {

            }

            String m = recv.getFirst();
            String status = m.split(":")[0];
            String result = m.split(":")[1];

            if(status.equals(MessageEnum.ERROR.getMessage()) && result.equals("FATAL")) {
                user.setCoins(user.getCoins() + price);
                user.removePowerup(powerup);
                return false;
            }
        }
        return true;
    }

    public void showShop() {
        HashMap<String, Integer> map = new HashMap<>();
        for(int i = 0; i < PowerupInterface.getPowerupClassList().size(); i++) {
            Class<? extends Powerup> powerupClass = PowerupInterface.getPowerupClassList().get(i);
            try {
                Method method = Class.forName(powerupClass.getName()).getMethod("getInstance");
                Object obj = method.invoke(null);
                map.put(powerupClass.getSimpleName(), ((Powerup)obj).getCost());
            } catch (Exception e) {
                shopView.showError("No method found",21, 2);
            }
        }

        shopView.showShop(map, user.getCoins());
    }
}
