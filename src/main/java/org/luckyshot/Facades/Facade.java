package org.luckyshot.Facades;

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
        HashMap<String, String> map = new HashMap<>();
        map.put("username", user.getUsername());
        map.put("level", Integer.toString(user.getLevel()));
        map.put("xp", Integer.toString(user.getXp()));
        menu.showMenu(map);

        int choice;
        do {
            choice = menu.getUserInput();
            if (choice == 1) {
                this.startSinglePlayerMatch();
            } else if (choice == 2) {
                this.startMultiPlayerMatch();
            } else if (choice == 3) {
                this.showShop();
            } else if (choice == 4) {
                this.showStats();
            } else if (choice == 5) {
                try {
                    this.quitGame();
                } catch (Exception e) {
                    System.out.println("Error while quitting the game.");
                    System.exit(1);
                }
            } else {
                menu.showInvalidChoice();
            }
        } while(choice < 1 || choice > 5);
    }

    private void startSinglePlayerMatch() {
        SinglePlayerGameFacade singlePlayerGameFacade = SinglePlayerGameFacade.getInstance();
        singlePlayerGameFacade.start(user);
    }

    private void startMultiPlayerMatch() {

    }

    private void showShop() {

    }

    private void showStats() {

    }

    private void quitGame() throws InterruptedException {
        Menu menu = new Menu();
        menu.quitGame();
        Thread.sleep(1000);
        System.exit(0);
    }
}
