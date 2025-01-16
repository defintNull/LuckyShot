package org.luckyshot;

import org.luckyshot.Facades.LoginFacade;

public class Main {
    public static void main(String[] args) {
        // Login
        LoginFacade loginFacade = LoginFacade.getInstance();
        loginFacade.start();
    }
}