package org.luckyshot.Views;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.Scanner;

// PER FAR VEDERE CARATTERI UNICODE SU POWERSHELL (almeno):
// $OutputEncoding = [Console]::InputEncoding = [Console]::OutputEncoding = New-Object System.Text.UTF8Encoding
public abstract class View {
    protected static final String ANSI_RESET = "\u001B[0m";
    protected static final String ANSI_BLACK = "\u001B[30m";
    protected static final String ANSI_RED = "\u001B[31m";
    protected static final String ANSI_GREEN = "\u001B[32m";
    protected static final String ANSI_YELLOW = "\u001B[33m";
    protected static final String ANSI_BLUE = "\u001B[34m";
    protected static final String ANSI_PURPLE = "\u001B[35m";
    protected static final String ANSI_CYAN = "\u001B[36m";
    protected static final String ANSI_WHITE = "\u001B[37m";

    public void showError(String s, int x, int y) {
        setCursorPos(x, y);
        System.out.print(ANSI_RED + "Error: " + s + ANSI_RESET);
        try {
            Thread.sleep(1000);
        } catch (Exception e) {
            System.out.print(ANSI_RED + "Error while sleeping" + ANSI_RESET);
        }
    }

    protected void displayHeader() {
        setCursorPos(1, 1);
        System.out.print("╔" + "═".repeat(98) + "╗");
        setCursorPos(2, 1);
        System.out.print("║");
        setCursorPos(2, 46);
        System.out.println(ANSI_PURPLE + "Lucky Shot\n" + ANSI_RESET);
        setCursorPos(2, 100);
        System.out.print("║");
        setCursorPos(3, 1);
        System.out.print("╚"+ "═".repeat(98) + "╝");
    }

    protected void clearScreen() {
        try {
            final String os = System.getProperty("os.name").toLowerCase();
            if (os.contains("windows"))
                new ProcessBuilder("cmd", "/c", "cls").inheritIO().start().waitFor();
            else
                new ProcessBuilder("clear").inheritIO().start().waitFor();
        }catch (Exception e) {
            systemError();
        }
    }

    protected void slowPrint(String s) {
        for (int i = 0; i < s.length(); i++) {
            System.out.print(s.charAt(i));
            try {
                Thread.sleep(100);
            } catch (Exception e) {
                systemError();
            }
        }
    }

    public void showLoading() {
        clearScreen();
        displayHeader();
        setCursorPos(5, 1);
        System.out.println("Loading...");
    }

    public void systemError() {
        System.out.println(ANSI_RED + "!!! SYSTEM ERROR !!!" + ANSI_RESET);
    }

    protected void setCursorPos(int row, int column) {
        char escCode = 0x1B;
        System.out.printf("%c[%d;%df",escCode,row,column);
    }

    public String getUserInput() {
        setCursorPos(35, 1);
        System.out.print("> ");
        Scanner scanner = new Scanner(System.in);
        return scanner.nextLine();
    }

    public String getUserInputThread() throws Exception{
        setCursorPos(35, 3);
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        StringBuilder sb = new StringBuilder();
        while (!br.ready()) {
            sb.append((char)br.read());
            Thread.sleep(200);
        }
        return sb.toString();
    }
}
