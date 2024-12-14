package org.luckyshot.Views;

import java.io.IOException;
import java.util.Scanner;

public class Menu extends View{
    public void showMenu() {
        try {
            clearScreen();
        } catch (IOException | InterruptedException e) {
            System.out.println("!!! Error while cleaning the console !!!");
        }
        displayHeader();

        setCursorPos(5, 1);
        System.out.println("Main menu\n");
        System.out.println("1. Single player");
        System.out.println("2. Multiplayer");
        System.out.println("3. Shop");
        System.out.println("4. Stats");
        System.out.println("5. Quit");
    }

    public void showLoginMenu() {
        try {
            clearScreen();
        } catch (IOException | InterruptedException e) {
            System.out.println("!!! Error while cleaning the console !!!");
        }
        displayHeader();

        setCursorPos(5, 1);
        System.out.println("Login menu");
        System.out.println("1. Login");
        System.out.println("2. Register");
        System.out.println("3. Quit");
        setCursorPos(10, 1);
    }

    public int getUserInput() {
        Scanner scanner = new Scanner(System.in);
        System.out.print("Select an option:");
        return scanner.nextInt();
    }

    public void showInvalidChoice() {
        System.out.println("Not a valid option, try again.");
    }

    public void quitGame() {
        System.out.println("Goodbye, hope to see you again!");
    }
}
