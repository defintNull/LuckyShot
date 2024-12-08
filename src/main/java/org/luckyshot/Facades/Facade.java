package org.luckyshot.Facades;

import org.luckyshot.Models.User;
import org.luckyshot.Views.Menu;

public class Facade {
    private static Facade instance;
    private User user;

    private Facade() {

    }

    public static Facade getInstance() {
        if(instance == null) {
            instance = new Facade();
        }
        return instance;
    }

    public void menu() {
        Menu menu = new Menu();
        menu.showMenu();

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
                    System.out.println("Error");
                    System.exit(1);
                }
            } else {
                menu.showInvalidChoice();
            }
        } while(choice < 1 || choice > 5);
    }

    private void startSinglePlayerMatch() {
        SinglePlayerGameFacade singlePlayerGameFacade = SinglePlayerGameFacade.getInstance();
        // Ci va lo user come parametro.
        User user = new User();
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
