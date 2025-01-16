package org.luckyshot.Facades.Services;

import org.checkerframework.checker.units.qual.C;
import org.java_websocket.client.WebSocketClient;
import org.java_websocket.handshake.ServerHandshake;

import java.net.URI;

public class Client extends WebSocketClient {
    private static Client instance = null;
    private static URI serverURI = URI.create("ws://localhost:8456");

    private Client() {
        super(serverURI);
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
        System.out.println(s);
    }

    @Override
    public void onClose(int i, String s, boolean b) {
        System.out.println("Connection close");
    }

    @Override
    public void onError(Exception e) {
        System.out.println(e.getMessage());
    }
}
