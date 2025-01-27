package org.luckyshot.Views;

import java.util.ArrayList;

public class MultiplayerMenuView extends View {
    public void showMenu() {
        clearScreen();
        displayHeader();
        setCursorPos(4, 2);
        System.out.print("Multiplayer menu");

        setCursorPos(6, 2);
        System.out.println("1. Create room");
        setCursorPos(7, 2);
        System.out.println("2. Join room");
        setCursorPos(8, 2);
        System.out.println("3. Back");
    }

    public void showInvalidChoice(int row) {
        setCursorPos(row, 2);
        System.out.print(ANSI_RED + "Not a valid option, try again." + ANSI_RESET);
    }

    public void showRoomMenu(boolean ready, ArrayList<String> usernames, String code) {
        clearScreen();
        displayHeader();

        setCursorPos(4, 2);
        System.out.print("Room menu");

        setCursorPos(6, 2);
        System.out.print("1. Quit room");
        if(ready) {
            setCursorPos(7, 2);
            System.out.print("2. Start game");
            setCursorPos(9, 2);
            System.out.print("READY");
        }

        setCursorPos(6, 30);
        System.out.print("Room's players:");
        int i = 0;
        for (String username : usernames) {
            setCursorPos(7 + i, 30);
            System.out.print(username);
            i += 1;
        }

        setCursorPos(12, 30);
        System.out.print("Room's code: " + code);

        setCursorPos(35, 1);
        System.out.print("> ");
    }

    public void showJoinMenu() {
        clearScreen();
        displayHeader();

        setCursorPos(5, 2);
        System.out.print("Insert room's code: ");
    }

    public void showRoomClosed() {
        clearScreen();
        displayHeader();

        setCursorPos(5, 2);
        System.out.print("Room closed. Back to menu.");

        setCursorPos(7, 2);
        System.out.print("Press enter to continue...");
    }

    public void showWaitingStartGame() {
        setCursorPos(15, 10);
        System.out.println("Waiting other players...");
    }

    public void showReadyGame() {
        setCursorPos(35, 3);
        System.out.println("Select 2 to continue...");
    }
}
