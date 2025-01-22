package org.luckyshot.Facades;

import org.luckyshot.Facades.Services.Client;
import org.luckyshot.Facades.Services.Converters.ObjectConverter;
import org.luckyshot.Models.Enums.MessageEnum;
import org.luckyshot.Models.User;
import org.luckyshot.Views.LoginView;
import org.luckyshot.Views.Menu;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.concurrent.TimeUnit;

public class LoginFacade {
    private static LoginFacade instance;
    private final BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

    private LoginFacade() {

    }

    public static LoginFacade getInstance() {
        if(instance == null) {
            instance = new LoginFacade();
        }
        return instance;
    }

    public void start() {
        try {
            ProcessBuilder processBuilder = new ProcessBuilder("cmd", "/c", "chcp 65001");
            Process process = processBuilder.start();
            process.waitFor();
        } catch (Exception e) {
            LoginView loginView = new LoginView();
            loginView.showError("Unable to start", 2, 10);
            return;
        }

        Client client = Client.getInstance();
        try {
            if(!client.connectBlocking()) {
                return;
            }
        } catch (Exception e) {
            return;
        }

        loginMenu();
    }

    public void loginMenu() {
        Menu menu = new Menu();
        while(true) {
            menu.showLoading();

            menu.showLoginMenu();

            int choice;
            do {
                choice = menu.getUserInput();
                if (choice == 1) {
                    this.login();
                } else if (choice == 2) {
                    this.register();
                } else if (choice == 3) {
                    try {
                        this.quitGame();
                    } catch (Exception e) {
                        System.exit(0);
                    }
                } else {
                    menu.showLoginMenu();
                    menu.showInvalidChoice(14);
                }
            } while (choice < 1 || choice > 3);
        }
    }

    private void quitGame() throws InterruptedException {
        Client client = Client.getInstance();
        client.close();
    }

    public void login() {
        LoginView loginView = new LoginView();
        loginView.displayLogin();

        String result;
        String status;

        String[] credentials = loginView.getLoginUserInput();
        String username = credentials[0];
        String password = credentials[1];

        //AGGIUNGERE CONTROLLO CARATTERI :&

        Client client = Client.getInstance();
        client.send("LOGIN:" + username + "&" + password);
        ArrayList<String> recv = client.recv();

        result = recv.getFirst();
        status = result.split(":")[0];
        String message = result.split(":")[1];

        while(status.equals(MessageEnum.ERROR.getMessage())) {
            if(message.equals("ALREADY_LOGGED")) {
                loginView.displayAlreadyLogged();
            } else if(message.equals("NOT_FOUND")) {
                loginView.displayLoginRetry();
            }

            credentials = loginView.getLoginUserInput();
            username = credentials[0];
            password = credentials[1];

            //AGGIUNGERE CONTROLLO CARATTERI :&

            client = Client.getInstance();
            client.send("LOGIN:" + username + "&" + password);
            recv = client.recv();

            result = recv.getFirst();
            status = result.split(":")[0];
        }

        ObjectConverter converter = new ObjectConverter();
        ArrayList<String> json = new ArrayList<>(Arrays.asList(result.split(":")));
        json.removeFirst();
        result = String.join(":", json);
        User user = converter.jsonToUser(result);

        Facade facade = Facade.getInstance(user);
        facade.menu();
    }

    public void register() {
        LoginView loginView = new LoginView();
        loginView.displayRegistration();

        Client client = Client.getInstance();

        String username = loginView.getRegisterUsernameInput();
        String[] passwords = {};
        boolean check = false;
        while (!check) {
            passwords = loginView.getRegistrationPasswordInput();
            if(!passwords[0].equals(passwords[1])) {
                loginView.displayRegistrationPasswordRetry();
            } else {
                check = true;
            }
        }


        client.send("REGISTER:" + username+"&"+encoder.encode(passwords[0]));

        ArrayList<String> recv = client.recv();

        String result = recv.getFirst();
        String status = result.split(":")[0];

        while(status.equals(MessageEnum.ERROR.getMessage())) {
            loginView.displayRegistrationUserRetry();

            username = loginView.getRegisterUsernameInput();
            passwords = new String[]{};
            check = false;
            while (!check) {
                passwords = loginView.getRegistrationPasswordInput();
                if(!passwords[0].equals(passwords[1])) {
                    loginView.displayRegistrationPasswordRetry();
                } else {
                    check = true;
                }
            }


            client.send("REGISTER:" + username+"&"+encoder.encode(passwords[0]));

            recv = client.recv();

            result = recv.getFirst();
            status = result.split(":")[0];
        }

        loginView.displayRegistrationSuccess();
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
