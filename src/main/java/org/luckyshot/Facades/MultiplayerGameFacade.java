package org.luckyshot.Facades;

import org.luckyshot.Facades.Services.Client;
import org.luckyshot.Facades.Services.Converters.ObjectConverter;
import org.luckyshot.Models.Enums.MessageEnum;
import org.luckyshot.Models.StateEffects.StateEffect;
import org.luckyshot.Models.StateEffects.StateEffectInterface;
import org.luckyshot.Views.MultiplayerGameView;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class MultiplayerGameFacade {
    private Client client = Client.getInstance();
    private MultiplayerGameView view;
    boolean gameEnded = false;
    private String roomCode;
    private String username;

    public MultiplayerGameFacade(String roomCode, String username) {
        view = new MultiplayerGameView(username);
        this.roomCode = roomCode;
    }

    public void start() {
        ArrayList<String> m = null;
        while (!gameEnded) {
            try {
                m = client.recv();
            } catch (Exception e) {
                view.systemError();
                System.exit(1);
            }
            if(m.size() == 1) {
                ArrayList<String> json = new ArrayList<>(Arrays.asList(m.getFirst().split(":")));
                String command = json.removeFirst();
                String params = String.join(":", json);
                if (command.equals(MessageEnum.ADD_ACTION.getMessage())) {
                    ArrayList<String> param = new ArrayList<String>(Arrays.asList(params.split(",")));
                    if (param.get(0).equals("ROUND_NUMBER")) {
                        view.showRoundStartingScreen(param.get(1));
                    } else if (param.get(0).equals("STATE_EFFECT")) {
                        String effect = null;
                        try {
                            for (int i = 0; i < StateEffectInterface.getStateEffectClassList().size(); i++) {
                                if (StateEffectInterface.getStateEffectClassList().get(i).getSimpleName().equals(param.get(1))) {
                                    Method method = Class.forName(StateEffectInterface.getStateEffectClassList().get(i).getName()).getMethod("getInstance");
                                    StateEffect stateEffect = (StateEffect) method.invoke(null);
                                    effect = stateEffect.getActivation();
                                    break;
                                }
                            }
                        } catch (Exception e) {
                            view.systemError();
                            System.exit(1);
                        }
                        view.showStateEffectActivation(effect);
                    }
                }
                else if(command.equals(MessageEnum.SHOW.getMessage())) {
                    if(params.equals("ACTIONS")) {
                        view.printLastAction();
                    }
                }
                else if (command.equals(MessageEnum.SHOW_GAME_STATE.getMessage())) {
                    ObjectConverter objectConverter = new ObjectConverter();
                    HashMap<String, String> gameState = objectConverter.jsonToObj(params, HashMap.class);
                    view.showGame(gameState);
                }
            }

        }
    }
}
