package org.luckyshot.Facades;

import org.luckyshot.Facades.Services.Client;
import org.luckyshot.Models.Enums.MessageEnum;
import org.luckyshot.Models.User;
import org.luckyshot.Views.MultiplayerMenuView;

import java.util.ArrayList;

public class MultiplayerMenuFacade {
    private static MultiplayerMenuFacade instance;
    private final MultiplayerMenuView view;
    private User user;
    private String roomCode;
    private final int MAX_ROOM_PLAYERS = 2;

    private boolean roomClosed = false;
    private boolean gameStarted = false;
    private static boolean ready;
    private String choice = null;
    private boolean inputFlag = false;
    private boolean readyStart = false;

    //Thread state
    private boolean state1 = false;
    private boolean state2 = false;

    private MultiplayerMenuFacade() {
        view = new MultiplayerMenuView();
        roomCode = null;
    }

    public static MultiplayerMenuFacade getInstance() {
        if(instance == null) {
            instance = new MultiplayerMenuFacade();
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
        ready = false;
        readyStart = false;
        Thread waitPlayer = new Thread(() -> {
            while (state1) {
                try {
                    ArrayList<String> message = client.recv();
                    String command = message.getFirst().split(":")[0];
                    if(command.equals(MessageEnum.OK.getMessage())) {
                        ready = message.size() == MAX_ROOM_PLAYERS;
                        message.replaceAll(s -> s.split(":")[1]);
                        view.showRoomMenu(ready, message, roomCode);
                    } else {
                        view.systemError();
                        System.exit(1);
                    }
                } catch (Exception e) {
                    break;
                }
            }
        });

        //Starting thread
        state1 = true;
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

                //Stop Thread
                state1 = false;
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
                //Stop Thread
                state1 = false;
                waitPlayer.interrupt();

                client.send("START_GAME:" + roomCode);

                //Qualcosa di attesa
                view.showWaitingStartGame();

                try {
                    ArrayList<String> message = client.recv();
                    String param = message.getFirst().split(":")[1];
                    if(param.equals("GAME_STARTED")){
                        //START GAME
                        startMultiplayerGame();
                    } else {
                        view.systemError();
                        break;
                    }
                } catch (Exception e) {
                    view.systemError();
                    System.exit(1);
                }

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

        Thread waitStart = new Thread(() -> {
            while (state1) {
                try {
                    ArrayList<String> m = client.recv();
                    if(m.size() >= 2) {
                        m.replaceAll(s -> s.split(":")[1]);
                        view.showRoomMenu(false, m, roomCode);
                    } else {
                        String value = m.getFirst().split(":")[1];
                        if(value.equals("ROOM_CLOSED")) {
                            roomClosed = true;
                            view.showRoomClosed();
                        } else if(value.equals("READY")) {
                            gameStarted = true;
                            view.showRoomMenu(true, usernames, roomCode);
                            view.showReadyGame();
                        }
                        break;
                    }
                } catch (Exception e) {
                    break;
                }
            }
        });

        //Start Thread
        state1 = true;
        waitStart.start();

        Thread threadInput = new Thread(() -> {
            while(state2) {
                if(inputFlag) {
                    choice = null;
                    choice = view.getUserInput();
                    inputFlag = false;
                } else {
                    try {
                        Thread.sleep(50);
                    } catch (Exception e) {
                        break;
                    }
                }
            }
        });

        //Start Thread
        state2 = true;
        threadInput.start();

        boolean checkInput = true;
        inputFlag = true;
        choice = null;
        roomClosed = false;
        gameStarted = false;
        do {
            checkInput = true;
            if(choice == null) {
                checkInput = false;
                try {
                    Thread.sleep(30);
                }
                catch (InterruptedException e) {
                    view.systemError();
                }
            } else if(roomClosed) {
                //Stopping threads
                state1 = false;
                waitStart.interrupt();
                state2 = false;
                threadInput.interrupt();

                return;
            }
            else if (choice.equals("1")){
                //Stopping threads
                state1 = false;
                waitStart.interrupt();
                state2 = false;
                threadInput.interrupt();

                client.send("LEAVE_ROOM:" + roomCode);
                try {
                    client.recv();
                    return;
                } catch (Exception e) {
                    view.systemError();
                    System.exit(1);
                }
            } else if(choice.equals("2") && gameStarted) {
                //Comincia il gioco

                //Stopping threads
                state1 = false;
                waitStart.interrupt();
                state2 = false;
                threadInput.interrupt();

                client.send("READY:" + roomCode);
                try {
                    ArrayList<String> response = client.recv();
                    String command = response.getFirst().split(":")[0];
                    String param = response.getFirst().split(":")[1];
                    if(command.equals(MessageEnum.OK.getMessage()) && param.equals("GAME_STARTED")) {
                        startMultiplayerGame();
                    } else {
                        view.systemError();
                        System.exit(1);
                    }
                } catch (Exception e) {
                    view.systemError();
                    e.printStackTrace();
                    System.exit(1);
                }

            } else {
                view.showRoomMenu(gameStarted, usernames, input);
                view.showInvalidChoice(14);
                checkInput = false;
                inputFlag = true;
                choice = null;
            }
        } while (!checkInput);
    }

    public void startMultiplayerGame() {
        MultiplayerGameFacade multiplayerGameFacade = new MultiplayerGameFacade(roomCode, user.getUsername());
        multiplayerGameFacade.start();
    }
}