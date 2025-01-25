package org.luckyshot.Facades;

import org.luckyshot.Facades.Services.Client;
import org.luckyshot.Views.MultiplayerGameView;

import java.util.HashMap;

public class MultiplayerGameFacade {
    private Client client = Client.getInstance();
    private MultiplayerGameView view;
    boolean gameEnded = false;

    public MultiplayerGameFacade() {
        view = new MultiplayerGameView();
    }

    public void start() {
        view.showGame(getGameStateMap());
        while (!gameEnded) {
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public HashMap<String, String> getGameStateMap() {
        //Qui riceve la mappa dello stato del gioco in JSON e la converte in HashMap

        String objectStateMapJSON = null;
        try {
            objectStateMapJSON = client.recv().getFirst();
        } catch (Exception e) {
            view.systemError();
            System.exit(1);
        }

        HashMap<String, String> objectStateMap = new HashMap<>();
        HashMap<String, String> gameStateMap = new HashMap<>();
        return gameStateMap;
    }
}
