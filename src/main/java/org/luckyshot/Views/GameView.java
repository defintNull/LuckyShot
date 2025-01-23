package org.luckyshot.Views;

import java.util.Scanner;

public class GameView extends View{
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
}
