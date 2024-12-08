package org.luckyshot.Views;

import java.util.Scanner;

public class GameView extends View{

    public int getUserInput() {
        System.out.print("> ");
        Scanner scanner = new Scanner(System.in);
        return scanner.nextInt();
    }

}
