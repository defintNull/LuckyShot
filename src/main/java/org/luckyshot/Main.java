package org.luckyshot;

import org.luckyshot.Facades.LoginFacade;
import org.luckyshot.Views.LoginView;
import org.luckyshot.Views.View;

public class Main {
    public static void main(String[] args) {
        // Login
        LoginFacade loginFacade = LoginFacade.getInstance();
        try {
            loginFacade.start();
        } catch (Exception e) {
            LoginView loginView = new LoginView();
            loginView.clearScreen();
        }
    }
}