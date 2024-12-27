package org.luckyshot.Views;

import org.luckyshot.Models.Powerups.PowerupInterface;

import java.util.Scanner;

public class ShopView extends View {
    public void showShop() {
        try{
            clearScreen();
        } catch (Exception e) {
            e.printStackTrace();
        }
        displayHeader();

        setCursorPos(5, 2);
        System.out.print("Shop");
        int i = 0;
        for(i = 0; i < PowerupInterface.getPowerupStringList().size(); i++) {
            setCursorPos(7+i, 2);
            System.out.print((i + 1) + ". " + PowerupInterface.getPowerupStringList().get(i) + ": "); // VA AGGIUNTO IL COST
        }
        setCursorPos(7+i+1, 2);
        System.out.print("q. Back to menu");
    }

    public String getUserInput() {
        Scanner scanner = new Scanner(System.in);
        setCursorPos(20, 1);
        System.out.print("> ");
        return scanner.nextLine();
    }

    public void showError(String s) {
        setCursorPos(21, 2);
        System.out.print(ANSI_RED + "Error: " + s + ANSI_RESET);
        try {
            Thread.sleep(1000);
        } catch (Exception e) {
            System.out.print(ANSI_RED + "Error while sleeping" + ANSI_RESET);
        }
    }
}
