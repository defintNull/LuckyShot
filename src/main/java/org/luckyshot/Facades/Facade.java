package org.luckyshot.Facades;

import org.luckyshot.Facades.Services.Client;
import org.luckyshot.Models.User;
import org.luckyshot.Views.MainMenuView;

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
        MainMenuView mainMenuView = new MainMenuView();
        boolean quitting = false;
        while(!quitting) {
            HashMap<String, String> map = new HashMap<>();
            map.put("username", user.getUsername());
            map.put("level", Integer.toString(user.getLevel()));
            map.put("xp", Integer.toString(user.getXp()));
            mainMenuView.showMenu(map);

            boolean success = false;
            String choice;
            boolean checkInput = true;
            do {
                choice = mainMenuView.getUserInput();
                if (choice.equals("1")) {
                    this.startSinglePlayerMatch();
                    success = true;
                } else if (choice.equals("2")) {
                    this.startMultiplayerMatch();
                    success = true;
                } else if (choice.equals("3")) {
                    success = this.shopMenu();
                } else if (choice.equals("4")) {
                    success = this.showStats();
                } else if (choice.equals("5")) {
                    try {
                        success = true;
                        quitting = true;
                        this.quitGame();
                    } catch (Exception e) {
                        System.out.println("Error while quitting the game.");
                        System.exit(1);
                    }
                } else {
                    mainMenuView.showMenu(map);
                    mainMenuView.showInvalidChoice(14);
                    checkInput = false;
                }
            } while (!checkInput);

            if(!success) {
                mainMenuView.showError("Server error", 2, 10);
                try{
                    Thread.sleep(1000);
                } catch(InterruptedException e){
                    mainMenuView.systemError();
                }
                break;
            }
        }
        try {
            Thread.sleep(1000);
        } catch(InterruptedException e){
            mainMenuView.systemError();
            System.exit(1);
        }
        System.exit(0);
    }

    private void startSinglePlayerMatch() {
        SinglePlayerGameFacade.getInstance().start(user);
    }

    private void startMultiplayerMatch() {
        MultiplayerMenuFacade.getInstance().start(user);
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

    private void quitGame() {
        Client client = Client.getInstance();
        client.close();
    }
}
