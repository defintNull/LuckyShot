package org.luckyshot.Views;

import java.io.IOException;

public class View {
    private static final String ANSI_RESET = "\u001B[0m";
    private static final String ANSI_BLACK = "\u001B[30m";
    private static final String ANSI_RED = "\u001B[31m";
    private static final String ANSI_GREEN = "\u001B[32m";
    private static final String ANSI_YELLOW = "\u001B[33m";
    private static final String ANSI_BLUE = "\u001B[34m";
    private static final String ANSI_PURPLE = "\u001B[35m";
    private static final String ANSI_CYAN = "\u001B[36m";
    private static final String ANSI_WHITE = "\u001B[37m";

    protected void displayHeader() {
        System.out.println("=".repeat(20) + ANSI_PURPLE + "\n     Lucky Shot\n" + ANSI_RESET + "=".repeat(20));
    }

    protected void clearScreen() throws IOException, InterruptedException {
//        final String os = System.getProperty("os.name").toLowerCase();
//        if (os.contains("windows"))
//            new ProcessBuilder("cmd", "/c", "cls").inheritIO().start().waitFor();
//        else
//            new ProcessBuilder("clear").inheritIO().start().waitFor();
        // CON GRADLE QUESTO PULISCE LA CONSOLE MA IL RUN RIMANE (PROVA AD ESEGUIRE gradle run DA TERMINALE)
        System.out.print("\033[H\033[2J");
        System.out.flush();
    }

    public void systemError() {
        System.out.println("System error!");
    }
}
