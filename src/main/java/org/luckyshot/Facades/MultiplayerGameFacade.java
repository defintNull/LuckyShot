package org.luckyshot.Facades;

import org.luckyshot.Facades.Services.Client;
import org.luckyshot.Models.Enums.MessageEnum;
import org.luckyshot.Models.User;
import org.luckyshot.Views.MultiplayerMenuView;

import java.io.ByteArrayInputStream;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class MultiplayerGameFacade {
    private static MultiplayerGameFacade instance;
    private final MultiplayerMenuView view;
    private static boolean ready;
    private User user;
    private String roomCode;
    private boolean roomClosed = false;
    private boolean gameStarted = false;
    private final int MAX_ROOM_PLAYERS = 2;
    private String choice = null;

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
        try {
            roomCode = client.recv().getFirst().split(":")[1];
        } catch (Exception e) {
            view.systemError();
            System.exit(1);
        }
        Thread waitPlayer = new Thread(() -> {
            while (!Thread.currentThread().isInterrupted()) {
                try {
                    ArrayList<String> usernames = client.recv();
                    ready = usernames.size() == MAX_ROOM_PLAYERS;
                    usernames.replaceAll(s -> s.split(":")[1]);
                    view.showRoomMenu(ready, usernames, roomCode);
                } catch (Exception e) {
                    break;
                }
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
                waitPlayer.interrupt();
                client.send("LEAVE_ROOM:" + roomCode);
                try {
                    client.recv();
                } catch (Exception e) {
                    view.systemError();
                    System.exit(1);
                }
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
    }

    public void joinRoom() {
        boolean joined;
        ArrayList<String> usernames = new ArrayList<>();
        Client client = Client.getInstance();
        String input = null;
        joined = false;

        view.showJoinMenu();
        input = view.getUserInput();
        roomCode = input;
        client.send("JOIN_ROOM:" + input);
        ArrayList<String> message = null;
        try {
            message = client.recv();
        } catch (Exception e) {
            view.systemError();
            System.exit(1);
        }
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

        if(!joined) {
            return;
        }
        view.showRoomMenu(false, usernames, input);

        roomClosed = false;
        gameStarted = false;

        Thread waitStart = new Thread(() -> {
            while (!Thread.currentThread().isInterrupted()) {
                try {
                    ArrayList<String> m = client.recv();
                    if(m.size() >= 2) {
                        m.replaceAll(s -> s.split(":")[1]);
                        view.showRoomMenu(false, m, roomCode);
                    } else {
                        String value = m.getFirst().split(":")[1];
                        if(value.equals("ROOM_CLOSED")) {
                            roomClosed = true;
                        } else if(value.equals("START_GAME")) {
                            gameStarted = true;
                        }
                    }
                } catch (Exception e) {
                    break;
                }
            }
        });
        waitStart.start();

        Thread getInput = new Thread(() -> {
            boolean interrupted = gameStarted || roomClosed;
            while (!interrupted) {
                try {
                    interrupted = gameStarted || roomClosed;
                    choice = view.getUserInputThread();
                    Thread.sleep(30);
                } catch (Exception e) {
                    break;
                }
            }
        });

        boolean checkInput;
        getInput.start();
        do {
            checkInput = true;
            if(gameStarted) {
                waitStart.interrupt();
                getInput.interrupt();
                startMultiplayerGame();
                break;
            }
            if(roomClosed) {
                waitStart.interrupt();
                getInput.interrupt();
                view.showRoomClosed();
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    view.systemError();
                }
                return;
            }
            if(choice == null) {
                checkInput = false;
                try {
                    Thread.sleep(30);
                }
                catch (InterruptedException e) {
                    view.systemError();
                }
                continue;
            }
            if (choice.equals("1")){
                waitStart.interrupt();
                getInput.interrupt();
                client.send("LEAVE_ROOM:" + roomCode);
                try {
                    client.recv();
                } catch (Exception e) {
                    view.systemError();
                    System.exit(1);
                }
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
