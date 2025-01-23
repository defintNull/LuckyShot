package org.luckyshot.Facades.Services;

import org.java_websocket.client.WebSocketClient;
import org.java_websocket.handshake.ServerHandshake;
import org.luckyshot.Views.MainMenuView;

import java.net.URI;
import java.util.ArrayList;

public class Client extends WebSocketClient {
    private static Client instance = null;
    private static URI serverURI = URI.create("ws://localhost:8456");
    private ArrayList<String> buffer;

    private Client() {
        super(serverURI);
        buffer = new ArrayList<>();
    }

    public static Client getInstance() {
        if(instance == null) {
            instance = new Client();
        }
        return instance;
    }

    @Override
    public void onOpen(ServerHandshake serverHandshake) {
        System.out.println("Connected");
    }

    @Override
    public void onMessage(String s) {
        if(s.equals("START")){
            buffer = new ArrayList<>();
        }
        buffer.add(s);
    }

    @Override
    public void onClose(int i, String s, boolean b) {
        MainMenuView mainMenuView = new MainMenuView();
        mainMenuView.quitGame();
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            System.exit(0);
        }
        System.exit(0);
    }

    @Override
    public void onError(Exception e) {
        System.out.println(e.getMessage());
    }

    private ArrayList<String> getBuffer() {
        if (!buffer.isEmpty() && buffer.getLast().equals("STOP")) {
            buffer.remove(buffer.getFirst());
            buffer.remove(buffer.getLast());
            return buffer;
        }
        return null;
    }

    public ArrayList<String> recv() {
        ArrayList<String> r = getBuffer();

        while(r == null) {
            r = getBuffer();
            try{
                Thread.sleep(10);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        return r;
    }
}
