package org.luckyshot.Facades;

import org.luckyshot.Models.Powerups.PowerupInterface;
import org.luckyshot.Views.ShopView;

public class ShopFacade {
    private static ShopFacade instance;
    private final ShopView shopView;

    private ShopFacade() {
        shopView = new ShopView();
    }

    public static ShopFacade getInstance() {
        if (instance == null) {
            instance = new ShopFacade();
        }
        return instance;
    }

    public void shopMenu() {
        while(true) {
            showShop();
            String choice = shopView.getUserInput();
            if(choice.equals("q")) {
                break;
            } else if((Integer.parseInt(choice) < 1 || Integer.parseInt(choice) > PowerupInterface.getPowerupClassList().size())) {
                shopView.showError("The desired powerup doesn't exists...");
            }
        }
    }

    public void showShop() {
        shopView.showShop();
    }
}
