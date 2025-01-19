package org.luckyshot.Facades;

import org.luckyshot.Facades.Services.Client;
import org.luckyshot.Models.User;
import org.luckyshot.Views.Menu;

import java.util.HashMap;

public class Facade {
    private static Facade instance;
    private final User user;

    private Facade(User user) {
        this.user = user;
    }

    public static Facade getInstance(User user) {
        if(instance == null) {
            instance = new Facade(user);
        }
        return instance;
    }

    private boolean isInteger(String s) {
        try {
            Integer.parseInt(s);
            return true;
        } catch (NumberFormatException ex) {
            return false;
        }
    }

    public void menu() {
        Menu menu = new Menu();
        while(true) {
            HashMap<String, String> map = new HashMap<>();
            map.put("username", user.getUsername());
            map.put("level", Integer.toString(user.getLevel()));
            map.put("xp", Integer.toString(user.getXp()));
            menu.showMenu(map);

            boolean success = false;
            int choice;
            do {
                choice = menu.getUserInput();
                if (choice == 1) {
                    this.startSinglePlayerMatch();
                    success = true;
                } else if (choice == 2) {
                    this.startMultiplayerMatch();
                    success = true;
                } else if (choice == 3) {
                    success = this.shopMenu();
                } else if (choice == 4) {
                    success = this.showStats();
                } else if (choice == 5) {
                    try {
                        this.quitGame();
                    } catch (Exception e) {
                        System.out.println("Error while quitting the game.");
                        System.exit(1);
                    }
                } else {
                    menu.showMenu(map);
                    menu.showInvalidChoice(14);
                }
            } while (choice < 1 || choice > 5);

            if(!success) {
                menu.showError("Server error", 2, 10);
                try{
                    Thread.sleep(1000);
                } catch(InterruptedException e){
                    e.printStackTrace();
                }
                break;
            }
        }
    }

    private void startSinglePlayerMatch() {
        SinglePlayerGameFacade singlePlayerGameFacade = SinglePlayerGameFacade.getInstance();
        singlePlayerGameFacade.start(user);
    }

    private void startMultiplayerMatch() {

    }

    private boolean shopMenu() {
        ShopFacade shopFacade = ShopFacade.getInstance(user);
        return shopFacade.shopMenu();
    }

    private boolean showStats() {
        StatsFacade statsFacade = StatsFacade.getInstance(user);
        statsFacade.showStats();
        return true;
    }

    private void quitGame() throws InterruptedException {
        Client client = Client.getInstance();
        client.close();
    }
}
