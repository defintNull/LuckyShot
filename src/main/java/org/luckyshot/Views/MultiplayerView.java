package org.luckyshot.Views;

import java.util.ArrayList;
import java.util.Scanner;

public class MultiplayerView extends GameView {
    public void showMenu() {
        try {
            clearScreen();
        } catch (Exception e) {
            showError("clear", 12, 2);
        }
        displayHeader();
        System.out.println("\nMenu");
        System.out.println("1. Create room");
        System.out.println("2. Join room");
        System.out.println("3. Back");
    }

    public void showInvalidChoice(int row) {
        setCursorPos(row, 2);
        System.out.print(ANSI_RED + "Not a valid option, try again." + ANSI_RESET);
    }

    public void showRoomMenu(boolean ready, ArrayList<String> usernames, String code) {
        try {
            clearScreen();
        } catch (Exception e) {
            showError("clear", 12, 2);
        }
        displayHeader();

        System.out.println("\nRoom menu");
        if(ready) {
            System.out.println("READY");
            System.out.println("1. Start game");
        }
        System.out.println("2. Quit room");

        for (String username : usernames) {
            System.out.println(username);
        }

        System.out.println(code);

        setCursorPos(35, 1);
        System.out.print("> ");
    }

    public void showJoinMenu() {
        try {
            clearScreen();
        } catch (Exception e) {
            showError("clear", 12, 2);
        }
        displayHeader();

        System.out.println("\nInsert code");

    }
}
