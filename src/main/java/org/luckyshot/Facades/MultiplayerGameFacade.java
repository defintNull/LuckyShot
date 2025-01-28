package org.luckyshot.Facades;

import org.luckyshot.Facades.Services.Client;
import org.luckyshot.Facades.Services.Converters.ObjectConverter;
import org.luckyshot.Models.Consumables.Consumable;
import org.luckyshot.Models.Consumables.ConsumableInterface;
import org.luckyshot.Models.Enums.MessageEnum;
import org.luckyshot.Models.Powerups.PoisonBullet;
import org.luckyshot.Models.Powerups.Powerup;
import org.luckyshot.Models.Powerups.PowerupInterface;
import org.luckyshot.Models.StateEffects.GuardianAngel;
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
    private HashMap<String, String> gameState = new HashMap<>();
    boolean endGameScreen = false;

    public MultiplayerGameFacade(String roomCode, String username) {
        view = new MultiplayerGameView(username);
        this.roomCode = roomCode;
        this.username = username;
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
                    }
                    else if (param.get(0).equals("STATE_EFFECT")) {
                        StateEffect stateEffect = null;
                        try {
                            for (int i = 0; i < StateEffectInterface.getStateEffectClassList().size(); i++) {
                                if (StateEffectInterface.getStateEffectClassList().get(i).getSimpleName().equals(param.get(2))) {
                                    Method method = Class.forName(StateEffectInterface.getStateEffectClassList().get(i).getName()).getMethod("getInstance");
                                    stateEffect = (StateEffect) method.invoke(null);
                                    break;
                                }
                            }
                        } catch (Exception e) {
                            view.systemError();
                            System.exit(1);
                        }
                        if(param.get(1).equals("ACTIVATION")) {
                            view.showStateEffectActivation(stateEffect.getActivation());
                        }
                        else if(param.get(1).equals("EFFECT")) {
                            view.showStateEffectEffect(stateEffect.getEffect());
                        }
                    }
                    else if(param.get(0).equals("WIN")) {
                        endGameScreen = true;
                        if(param.get(1).equals("1")) {
                            view.showWinner(username);
                        }
                        else if(param.get(1).equals("0")) {
                            view.showWinner("not you");
                        }
                        else {
                            view.showWinner("none of you");
                        }
                    }
                    else if(param.get(0).equals("XP")) {
                        view.showFinalXp(param.get(1));
                    }
                    else if(param.get(0).equals("SUMMARY")) {
                        view.showLevelAndXp(param.get(1), param.get(2), param.get(3));
                    }
                    else if(param.get(0).equals("HANDCUFFS")) {
                        if(param.get(2).equals("1")) {
                            view.showHandcuffedState(true, Boolean.parseBoolean(param.get(3)));
                        }
                        else if(param.get(2).equals("0")) {
                            view.showHandcuffedState(false, Boolean.parseBoolean(param.get(3)));
                        }
                    }
                    else if(param.get(0).equals("POISONED")) {
                        view.showPowerupEffect(PoisonBullet.getInstance());
                    }
                    else if(param.get(0).equals("RESURRECTED")) {
                        if(param.get(1).equals(username)) {
                            view.showStateEffectActivation(GuardianAngel.getInstance().getEffect());
                        }
                        else {
                            view.showStateEffectActivation(param.get(1) + " has been given an extra life!");
                        }
                    }
                    else if(param.get(0).equals("CONSUMABLES_DRAWN")) {
                        view.showConsumablesDrawn();
                    }
                    else if(param.get(0).equals("POWERUP")) {
                        Powerup powerup = null;
                        try {
                            for (int i = 0; i < PowerupInterface.getPowerupClassList().size(); i++) {
                                if (PowerupInterface.getPowerupClassList().get(i).getSimpleName().equals(param.get(3))) {
                                    Method method = Class.forName(PowerupInterface.getPowerupClassList().get(i).getName()).getMethod("getInstance");
                                    powerup = (Powerup) method.invoke(null);
                                    break;
                                }
                            }
                        } catch (Exception e) {
                            view.systemError();
                            System.exit(1);
                        }

                        if(param.get(1).equals("ACTIVATION")) {
                            view.showPowerupActivation(powerup.toString());
                        }
                        else if(param.get(1).equals("EFFECT")) {
                            view.showPowerupEffect(powerup);
                        }
                    }
                    else if(param.get(0).equals("CONSUMABLE")) {
                        Consumable consumable = null;
                        try {
                            for (int i = 0; i < ConsumableInterface.getConsumableClassList().size(); i++) {
                                if (ConsumableInterface.getConsumableClassList().get(i).getSimpleName().equals(param.get(2))) {
                                    Method method = Class.forName(ConsumableInterface.getConsumableClassList().get(i).getName()).getMethod("getInstance");
                                    consumable = (Consumable) method.invoke(null);
                                    break;
                                }
                            }
                        } catch (Exception e) {
                            view.systemError();
                            System.exit(1);
                        }

                        if(param.get(1).equals("ACTIVATION")) {
                            view.showConsumableActivation(consumable.toString());
                        }
                        else if(param.get(1).equals("EFFECT")) {
                            view.showConsumableEffect(consumable.getEffect(param.get(3)));
                        }
                    }
                    else if(param.get(0).equals("CONSUMABLE_NOT_USED")) {
                        view.addLastAction("Consumable could not be used");
                    }
                    else if(param.get(0).equals("GHOST_GUN_DAMAGE")) {
                        view.showGhostGunDamage();
                    }
                    else if(param.get(0).equals("SHOOT")) {
                        if(param.get(1).equals("TARGET")) {
                            view.showShootingTarget(param.get(2));
                        }
                        else if(param.get(1).equals("RESULT")) {
                            view.showShootingResult(param.get(2));
                        }
                    }
                }
                else if(command.equals(MessageEnum.SHOW.getMessage())) {
                    if(params.equals("ACTIONS")) {
                        view.printLastAction(endGameScreen);
                    }
                }
                else if (command.equals(MessageEnum.SHOW_GAME_STATE.getMessage())) {
                    ObjectConverter objectConverter = new ObjectConverter();
                    gameState = objectConverter.jsonToObj(params, HashMap.class);
                    view.showGame(gameState);
                }
                else if (command.equals(MessageEnum.REFRESH.getMessage())) {
                    view.refresh(gameState);
                }
                else if (command.equals(MessageEnum.ADD_ERROR.getMessage())) {
                    ArrayList<String> param = new ArrayList<>(Arrays.asList(params.split(",")));
                    if(param.get(0).equals("COMMAND")) {
                        view.showError("Command not recognized.");
                    }
                    else if(param.get(0).equals("POWERUP_NOT_FOUND")) {
                        view.showError("Powerup not found.");
                    }
                    else if(param.get(0).equals("CONSUMABLE_NOT_FOUND")) {
                        view.showError("Consumable not found.");
                    }
                    else if(param.get(0).equals("CONSUMABLE")) {
                        if(param.get(1).equals("FORBIDDEN")) {
                            if(param.get(2).equals(username)) {
                                view.showError("You can't steal that consumable.");
                            } else {
                                view.showError("He can't steal that consumable.");
                            }
                        }
                    }
                }
                else if (command.equals(MessageEnum.SHOW_ERROR.getMessage())) {
                    view.printLastErrorAction();
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        view.systemError();
                        System.exit(1);
                    }
                }
                else if (command.equals(MessageEnum.SHOW_BULLETS.getMessage())) {
                    ArrayList<String> bullets = new ArrayList<>();
                    for(int i = 0; i < params.length(); i++) {
                        bullets.add(String.valueOf(params.charAt(i)));
                    }
                    view.showBullets(bullets);
                }
                else if (command.equals(MessageEnum.END.getMessage())) {
                    if(params.equals("OK")) {
                        client.send("GAME_END:" + roomCode);
                    }
                    gameEnded = true;
                    break;
                }
                else if (command.equals(MessageEnum.INPUT.getMessage())) {
                    String input = view.getUserInput();
                    if(input.isEmpty() || input.equals("\n")) {
                        input = "NULL,NULL";
                    }
                    if(params.equals("ACTION")) {
                        ArrayList<String> actions = new ArrayList<>(Arrays.asList(input.trim().split(" ")));
                        actions.set(0, actions.getFirst().toUpperCase());
                        input = String.join(",", actions);
                        client.send("GAME_ACTION:" + roomCode + "," + input);
                    }
                    else if(params.equals("ENERGY_DRINK")) {
                        client.send("GAME_ACTION:" + roomCode + "," + input);
                    }
                }
                client.send("ACK:" + roomCode);
            }
        }

    }
}
