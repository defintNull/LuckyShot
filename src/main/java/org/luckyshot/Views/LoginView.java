package org.luckyshot.Views;

import java.util.Scanner;

public class LoginView extends View{

    public void display() {
        displayHeader();

        System.out.println("\nLogin\n");
    }

    public String[] getUserInput() {
        System.out.println("Enter your username: ");
        Scanner sc = new Scanner(System.in);
        String username = sc.nextLine();
        System.out.println("Enter your password: ");
        String password = sc.nextLine();
        return new String[]{username, password};
    }

}
