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

        System.out.println("\nMain menu\n");
        System.out.println("1. Single player");
        System.out.println("2. Multiplayer");
        System.out.println("3. Shop");
        System.out.println("4. Stats");
        System.out.println("5. Quit");
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
        System.out.println("Goodbye, thanks for playing!");
    }
}
