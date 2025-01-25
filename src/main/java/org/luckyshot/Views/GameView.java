package org.luckyshot.Views;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;

public class GameView extends View{
    HashMap<String, String> stateMap = new HashMap<>();
    ArrayList<String> lastAction = new ArrayList<>();
    ArrayList<String> lastErrorAction = new ArrayList<>();

    protected void displayHeader() {
        setCursorPos(1, 1);
        System.out.print("╔" + "═".repeat(98) + "╗");
        setCursorPos(2, 1);
        System.out.print("║");
        setCursorPos(2, 89);
        System.out.print(ANSI_PURPLE + "Lucky Shot\n" + ANSI_RESET);
        setCursorPos(2, 100);
        System.out.print("║");
        setCursorPos(3, 1);
        System.out.print("╠" + "═".repeat(98) + "╣");
    }

    public void showGame(HashMap<String, String> stateMap) {
        clearScreen();

        drawTable();
        showGameState(stateMap);
        printLastAction();
        printLastErrorAction();
    }

    public void showGameState(HashMap<String, String> stateMap) {

    }

    public void printLastAction() {

    }

    public void drawTable() {
        displayHeader();

        for(int i = 0; i < 30; i++) {
            setCursorPos(4+i, 1);
            System.out.print("║");
        }
        for(int i = 0; i < 30; i++) {
            setCursorPos(4+i, 100);
            System.out.print("║");
        }

        for(int i = 0; i < 30; i++) {
            setCursorPos(4+i, 51);
            System.out.print("│");
        }

        setCursorPos(5, 1);
        System.out.print("╟" + "─".repeat(49) + "┤");

        setCursorPos(23, 1);
        System.out.print("╟" + "─".repeat(49) + "┤");

        setCursorPos(16, 1);
        System.out.print("╟" + "─".repeat(49) + "┼" + "─".repeat(48) + "╢");
        setCursorPos(17, 22);
        System.out.print("__,_____");
        setCursorPos(18, 21);
        System.out.print("/ __.==--\"");
        setCursorPos(19, 20);
        System.out.print("/#(-'");
        setCursorPos(20, 20);
        System.out.print("`-'");

        setCursorPos(21, 1);
        System.out.print("╟" + "─".repeat(49) + "┼" + "─".repeat(48) + "╢");
        setCursorPos(34, 1);
        System.out.print("╚" + "═".repeat(98) + "╝");

        setCursorPos(3, 51);
        System.out.print("╤");

        setCursorPos(34, 51);
        System.out.print("╧");

        drawHowToPlayGuide();
    }

    public void drawHowToPlayGuide() {
        // Titolo della guida
        setCursorPos(22, 52);
        System.out.print(ANSI_BLUE + "Commands guide:" + ANSI_RESET);

        // Sezione 1: Shooting
        setCursorPos(24, 52);
        System.out.print("1) Shooting:");
        setCursorPos(25, 55);
        System.out.print("shoot 1: shoot yourself");
        setCursorPos(26, 55);
        System.out.print("shoot 2: shoot the opponent");

        // Sezione 2: Consumables
        setCursorPos(27, 52);
        System.out.print("2) Use consumable:");
        setCursorPos(28, 55);
        System.out.print("use a, b, c, ...");

        // Sezione 3: Powerups
        setCursorPos(29, 52);
        System.out.print("3) Use powerup:");
        setCursorPos(30, 55);
        System.out.print("use 1, 2, 3, ...");
    }

    public void showBullets(ArrayList<String> bullets) {
        customPrint("Here are the bullets: ", "slow", 17, 52);

        String m = "";
        for (String bullet : bullets) {
            if(bullet.equals("0")) {
                m += ANSI_CYAN;
            } else if(bullet.equals("1")) {
                m += ANSI_RED;
            }
            m += "█ " + ANSI_RESET;
        }
        customPrint(m, "fast", 18, 52);

        customPrint("I'm loading the gun...", "slow", 20, 52);

        try {
            Thread.sleep(2000);
        } catch (Exception e) {
            showError("sleep");
        }
    }

    public void showError(String s) {
        this.lastErrorAction.add(ANSI_RED + "ERROR: " + s + ANSI_RESET);
    }

    public void printLastErrorAction() {
        if(!lastErrorAction.isEmpty()) {
            for(int i=0; i<lastErrorAction.size(); i++) {
                customPrint(lastErrorAction.get(i), "fast", 17 + i, 52);
            }
        }
        lastErrorAction.clear();
    }

    public void customPrint(String message, String speed, int row, int col) {
        setCursorPos(row, col);
        if(speed.equals("fast")) {
            System.out.print(message);
        } else if(speed.equals("slow")) {
            slowPrint(message);
        }
    }
}
