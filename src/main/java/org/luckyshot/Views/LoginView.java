package org.luckyshot.Views;

import java.io.IOException;
import java.util.Scanner;

public class LoginView extends View{

    public void displayLogin() {
        try {
            clearScreen();
        } catch (IOException | InterruptedException e) {
            System.out.println("!!! Error while cleaning the console !!!");
        }
        displayHeader();

        System.out.println("\nLogin\n");
    }

    public void displayRegistration() {
        try {
            clearScreen();
        } catch (IOException | InterruptedException e) {
            System.out.println("!!! Error while cleaning the console !!!");
        }
        displayHeader();

        System.out.println("\nRegistration\n");
        System.out.println("To register enter username and password");
    }

    public void displayLoginRetry() {
        System.out.println("Wrong credentials!");
        System.out.println("Try again");
    }

    public  void displayRegistrationUserRetry() {
        System.out.println("Username already taken!");
    }

    public void displayRegistrationPasswordRetry() {
        System.out.println("Password doesn't match!");
    }

    public String[] getLoginUserInput() {
        System.out.println("Enter your username: ");
        Scanner sc = new Scanner(System.in);
        String username = sc.nextLine();
        System.out.println("Enter your password: ");
        String password = sc.nextLine();
        return new String[]{username, password};
    }

    public String getRegisterUsernameInput() {
        System.out.println("Enter your username: ");
        Scanner sc = new Scanner(System.in);
        String username = sc.nextLine();
        return username;
    }

    public String[] getRegistrationPasswordInput() {
        Scanner sc = new Scanner(System.in);
        System.out.println("Enter your password: ");
        String password = sc.nextLine();
        System.out.println("ReEnter your password: ");
        String password2 = sc.nextLine();
        return new String[]{password, password2};
    }

}
