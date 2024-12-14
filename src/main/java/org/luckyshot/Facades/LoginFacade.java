package org.luckyshot.Facades;

import org.hibernate.Session;
import org.hibernate.Transaction;
import org.luckyshot.Models.User;
import org.luckyshot.Views.LoginView;
import org.luckyshot.Views.Menu;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

public class LoginFacade {
    private static LoginFacade instance;
    private final BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
    private HibernateService hibernateService;

    private LoginFacade() {

    }

    public static LoginFacade getInstance() {
        if(instance == null) {
            instance = new LoginFacade();
        }
        return instance;
    }

    public void start() {
        Menu menu = new Menu();
        menu.showLoading();
        hibernateService = HibernateService.getInstance();

        menu.showLoginMenu();

        int choice;
        do {
            choice = menu.getUserInput();
            if (choice == 1) {
                this.login();
            } else if (choice == 2) {
                this.register();
            } else if(choice == 3) {
                try {
                    this.quitGame();
                } catch (Exception e) {
                    System.exit(0);
                }
            }else {
                menu.showInvalidChoice();
            }
        } while(choice < 1 || choice > 3);
    }

    private void quitGame() throws InterruptedException {
        Menu menu = new Menu();
        menu.quitGame();
        Thread.sleep(1000);
        System.exit(0);
    }

    public void login() {
        LoginView loginView = new LoginView();
        loginView.displayLogin();

        Session session = hibernateService.getSessionFactory().openSession();
        User user = null;
        while (user == null) {
            String[] credentials = loginView.getLoginUserInput();
            try {
                user = session.createQuery("from User where username = :username", User.class)
                        .setParameter("username", credentials[0])
                        .getSingleResult();
            } catch (Exception e) {
                user = null;
                loginView.displayLoginRetry();
            }
            if(user != null) {
                if(!encoder.matches(credentials[1], user.getPassword())) {
                    user = null;
                    loginView.displayLoginRetry();
                }
            }
        }
        session.close();

        Facade facade = Facade.getInstance(user);
        facade.menu();
    }

    public void register() {

        HibernateService hibernateService = HibernateService.getInstance();
        Session session = hibernateService.getSessionFactory().openSession();

        LoginView loginView = new LoginView();
        loginView.displayRegistration();

        String username = "";
        boolean check = false;
        while (!check) {
            username = loginView.getRegisterUsernameInput();
            User user = null;
            try {
                user = session.createQuery("from User where username = :username", User.class)
                        .setParameter("username", username)
                        .getSingleResult();
            } catch (Exception e) {
                user = null;
            }
            if(user != null) {
                loginView.displayRegistrationUserRetry();
            } else {
                check = true;
            }
        }

        String[] credentials = {};
        check = false;
        while (!check) {
            credentials = loginView.getRegistrationPasswordInput();
            if(!credentials[0].equals(credentials[1])) {
                loginView.displayRegistrationPasswordRetry();
            } else {
                check = true;
            }
        }


        Transaction transaction = null;
        User user = null;
        try {
            transaction = session.beginTransaction();
            user = new User(username, encoder.encode(credentials[0]));
            session.persist(user);
            transaction.commit();
        } catch (Exception e) {
            if(transaction != null) {
                transaction.rollback();
            }
            loginView.systemError();
        }
        session.close();

        Facade facade = Facade.getInstance(user);
        facade.menu();
    }
}
