package org.luckyshot.Views;

import org.luckyshot.Models.Powerups.PowerupInterface;

import java.util.HashMap;
import java.util.Scanner;

public class ShopView extends View {
    public void showShop(HashMap<String, Integer> map, int coins) {
        try{
            clearScreen();
        } catch (Exception e) {
            showError("Error while clearing the screen");
        }
        displayHeader();

        setCursorPos(5, 2);
        System.out.print("Shop");
        int i = 0;
        for(i = 0; i < PowerupInterface.getPowerupStringList().size(); i++) {
            setCursorPos(7+i, 2);
            System.out.print((i + 1) + ". " + PowerupInterface.getPowerupStringList().get(i) + ": " + map.get(PowerupInterface.getPowerupClassList().get(i).getSimpleName()).toString() + " coins");
        }
        setCursorPos(7+i+1, 2);
        System.out.print("q. Back to menu");

        setCursorPos(9 + i + 1, 2);
        System.out.print("You have " + coins + " coins");

        setCursorPos(10 + i + 1, 2);
        System.out.print("What do you want to buy?");

        setCursorPos(12 + i + 1, 1);
        System.out.print("> ");
    }

    public String getUserInput() {
        Scanner scanner = new Scanner(System.in);
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
