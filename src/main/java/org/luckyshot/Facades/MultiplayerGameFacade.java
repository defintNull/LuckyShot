package org.luckyshot.Facades;

import org.luckyshot.Facades.Services.Client;
import org.luckyshot.Models.Enums.MessageEnum;
import org.luckyshot.Models.User;
import org.luckyshot.Views.MultiplayerView;
import org.luckyshot.Views.SinglePlayerGameView;

import java.util.ArrayList;

public class MultiplayerGameFacade {
    private static MultiplayerGameFacade instance;
    private final MultiplayerView view;
    private static boolean ready;
    private User user;
    String roomCode;
    private MultiplayerGameFacade() {
        view = new MultiplayerView();
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
        view.showMenu();

        String choice;
        boolean ok = true;
        do {
            choice = view.getUserInput();
            if (choice.equals("1")) {
                createRoom();
            } else if (choice.equals("2")){
                joinRoom();
            } else if (choice.equals("3")) {
                break;
            } else {
                view.showMenu();
                view.showInvalidChoice(14);
                ok = false;
            }
        } while (!ok);
    }

    public void createRoom() {
        Client client = Client.getInstance();
        client.send("CREATE_ROOM:CREATE_ROOM");
        roomCode = client.recv().getFirst().split(":")[1];
        Thread waitPlayer = new Thread(() -> {
            while(true) {
                ArrayList<String> usernames = client.recv();
                if(usernames.size() == 2) {
                    ready = true;
                } else {
                    ready = false;
                }
                for (int i = 0; i < usernames.size(); i++) {
                    usernames.set(i, usernames.get(i).split(":")[1]);
                }
                view.showRoomMenu(ready, usernames, roomCode);
            }
        });
        waitPlayer.start();
        ArrayList<String> usernames = new ArrayList<>();
        usernames.add(user.getUsername());
        view.showRoomMenu(ready, usernames, roomCode);
        view.getUserInput();
        String choice;
        boolean ok = true;
        do {
            choice = view.getUserInput();
            if (choice.equals("1") && ready) {
                waitPlayer.interrupt();
                startMultiplayerGame();
            } else if (choice.equals("2")){
                client.send("LEAVE_ROOM:" + roomCode);
                break;
            } else {
                view.showMenu();
                view.showInvalidChoice(14);
                ok = false;
            }
        } while (!ok);
        waitPlayer.interrupt(); //ATTENZIONE
    }

    public void joinRoom() {
        boolean joined = false;
        ArrayList<String> usernames = new ArrayList<>();
        Client client = Client.getInstance();
        String input = null;
        do {
            view.showJoinMenu();
            input = view.getUserInput();
            roomCode = input;
            client.send("JOIN_ROOM:" + input);
            ArrayList<String> message = client.recv();
            String status = message.getFirst().split(":")[0];
            if(status.equals(MessageEnum.ERROR.getMessage())) {
                view.showError("ERROR WHILE JOIN", 10, 10);
            } else {
                for (int i = 0; i < message.size(); i++) {
                    String username = message.get(i).split(":")[1];
                    usernames.add(username);
                }
                joined = true;
            }
        } while(!joined);

        view.showRoomMenu(false, usernames, input);

        Thread waitStart = new Thread(() -> {

        });

        boolean ok = true;
        String choice;
        do {
            choice = view.getUserInput();
            if (choice.equals("1")){
                client.send("LEAVE_ROOM:" + roomCode);
                client.recv();
                ok = true;
            } else {
                view.showRoomMenu(false, usernames, input);
                view.showInvalidChoice(14);
                ok = false;
            }
        } while (!ok);
    }

    public void startMultiplayerGame() {

    }
}
