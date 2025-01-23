package org.luckyshot.Views;

import java.util.HashMap;

public class MainMenuView extends View{
    public void showMenu(HashMap<String, String> map) {
        clearScreen();
        displayHeader();


        setCursorPos(5, 50);
        System.out.print("Player: " + map.get("username"));
        setCursorPos(6, 50);
        System.out.print("Level: " + map.get("level"));
        setCursorPos(7, 50);
        System.out.print("XP: " + map.get("xp"));

        setCursorPos(5, 2);
        System.out.print("Select an option:");
        setCursorPos(6, 2);
        System.out.print("1. Single player");
        setCursorPos(7, 2);
        System.out.print("2. Multiplayer");
        setCursorPos(8, 2);
        System.out.print("3. Shop");
        setCursorPos(9, 2);
        System.out.print("4. Stats");
        setCursorPos(10, 2);
        System.out.print("5. Quit");
    }

    public void showLoginMenu() {
        clearScreen();
        displayHeader();

        setCursorPos(5, 1);
        System.out.println("Login menu");
        System.out.println("1. Login");
        System.out.println("2. Register");
        System.out.println("3. Quit");
        setCursorPos(10, 1);
    }

    public void showInvalidChoice(int row) {
        setCursorPos(row, 1);
        System.out.print(ANSI_RED + "Not a valid option, try again." + ANSI_RESET);
    }

    public void quitGame() {
        System.out.println("Goodbye, hope to see you again!");
    }
}
