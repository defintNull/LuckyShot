package org.luckyshot.Views;

import java.io.IOException;


//DA FARE ASTRATTA
public class View {
    protected static final String ANSI_RESET = "\u001B[0m";
    protected static final String ANSI_BLACK = "\u001B[30m";
    protected static final String ANSI_RED = "\u001B[31m";
    protected static final String ANSI_GREEN = "\u001B[32m";
    protected static final String ANSI_YELLOW = "\u001B[33m";
    protected static final String ANSI_BLUE = "\u001B[34m";
    protected static final String ANSI_PURPLE = "\u001B[35m";
    protected static final String ANSI_CYAN = "\u001B[36m";
    protected static final String ANSI_WHITE = "\u001B[37m";

    protected void displayHeader() {
        System.out.println("=".repeat(20) + ANSI_PURPLE + "\n     Lucky Shot\n" + ANSI_RESET + "=".repeat(20));
    }

    protected void clearScreen() throws IOException, InterruptedException {
        final String os = System.getProperty("os.name").toLowerCase();
        if (os.contains("windows 10"))
            new ProcessBuilder("cmd", "/c", "cls").inheritIO().start().waitFor();
        else if(os.contains("windows 11")) {
                    System.out.print("\033[H\033[2J");
                    System.out.flush();
        } else
            new ProcessBuilder("clear").inheritIO().start().waitFor();
        // CON GRADLE QUESTO PULISCE LA CONSOLE MA IL RUN RIMANE (PROVA AD ESEGUIRE gradle run DA TERMINALE)
    }

    protected void slowPrintln(String s) {
        for (int i = 0; i < s.length(); i++) {
            System.out.print(s.charAt(i));
            try {
                Thread.sleep(100);
            } catch (Exception e) {
                systemError();
            }
        }
        System.out.println();
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

    public void systemError() {
        System.out.println(ANSI_RED + "System error!" + ANSI_RESET);
    }
}
