package org.luckyshot.Facades;

import org.luckyshot.Facades.Services.Client;
import org.luckyshot.Models.Enums.MessageEnum;
import org.luckyshot.Models.User;
import org.luckyshot.Views.MultiplayerMenuView;

import java.util.ArrayList;

public class MultiplayerGameFacade {
    private static MultiplayerGameFacade instance;
    private final MultiplayerMenuView view;
    private static boolean ready;
    private User user;
    String roomCode;
    private final int MAX_ROOM_PLAYERS = 2;
    private MultiplayerGameFacade() {
        view = new MultiplayerMenuView();
        roomCode = null;
    }

    public static MultiplayerGameFacade getInstance() {
        if(instance == null) {
            instance = new MultiplayerGameFacade();
        }
        return instance;
    }

    public void start(User user) {
        this.user = user;
        String choice;
        boolean checkInput;
        boolean goBack = false;

        while(!goBack) {
            view.showMenu();

            do {
                checkInput = true;
                choice = view.getUserInput();
                if (choice.equals("1")) {
                    createRoom();
                } else if (choice.equals("2")) {
                    joinRoom();
                } else if (choice.equals("3")) {
                    goBack = true;
                    break;
                } else {
                    view.showMenu();
                    view.showInvalidChoice(14);
                    checkInput = false;
                }
            } while (!checkInput);
        }
    }

    public void createRoom() {
        Client client = Client.getInstance();
        client.send("CREATE_ROOM:CREATE_ROOM");
        roomCode = client.recv().getFirst().split(":")[1];
        Thread waitPlayer = new Thread(() -> {
            while(true) {
                ArrayList<String> usernames = client.recv();
                ready = usernames.size() == MAX_ROOM_PLAYERS;
                usernames.replaceAll(s -> s.split(":")[1]);
                view.showRoomMenu(ready, usernames, roomCode);
            }
        });
        waitPlayer.start();
        ArrayList<String> usernames = new ArrayList<>();
        usernames.add(user.getUsername());
        view.showRoomMenu(ready, usernames, roomCode);

        String choice;
        boolean checkInput;
        do {
            checkInput = true;
            choice = view.getUserInput();
            if (choice.equals("1")) {
                client.send("LEAVE_ROOM:" + roomCode);
                break;
            } else if (choice.equals("2") && ready) {
                waitPlayer.interrupt();
                startMultiplayerGame();
            } else {
                view.showMenu();
                view.showInvalidChoice(14);
                checkInput = false;
            }
        } while (!checkInput);
        waitPlayer.interrupt(); //ATTENZIONE
    }

    public void joinRoom() {
        boolean joined;
        ArrayList<String> usernames = new ArrayList<>();
        Client client = Client.getInstance();
        String input = null;
        do {
            joined = false;

            view.showJoinMenu();
            input = view.getUserInput();
            roomCode = input;
            client.send("JOIN_ROOM:" + input);
            ArrayList<String> message = client.recv();
            String status = message.getFirst().split(":")[0];
            if(status.equals(MessageEnum.ERROR.getMessage())) {
                view.showError("ERROR WHILE JOIN", 10, 10);
            } else {
                for (String s : message) {
                    String username = s.split(":")[1];
                    usernames.add(username);
                }
                joined = true;
            }
        } while(!joined);

        view.showRoomMenu(false, usernames, input);

        Thread waitStart = new Thread(() -> {

        });

        boolean checkInput;
        String choice;
        do {
            checkInput = true;
            choice = view.getUserInput();
            if (choice.equals("1")){
                client.send("LEAVE_ROOM:" + roomCode);
                client.recv();
            } else {
                view.showRoomMenu(false, usernames, input);
                view.showInvalidChoice(14);
                checkInput = false;
            }
        } while (!checkInput);
    }

    public void startMultiplayerGame() {

    }
}
