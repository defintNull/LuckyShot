package org.luckyshot;

import org.hibernate.Session;
import org.luckyshot.Facades.Facade;
import org.luckyshot.Facades.HibernateService;
import org.luckyshot.Facades.LoginFacade;
import org.luckyshot.Models.User;

public class Main {
    public static void main(String[] args) {
        // Login
        LoginFacade loginFacade = LoginFacade.getInstance();
        loginFacade.start();
    }
}