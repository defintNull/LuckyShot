package org.luckyshot.Views;

import org.luckyshot.Models.Consumables.Consumable;
import org.luckyshot.Models.Consumables.ConsumableInterface;
import org.luckyshot.Models.Powerups.Powerup;
import org.luckyshot.Models.Powerups.PowerupInterface;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

public class MultiplayerGameView extends GameView {
    private String username;
    int playerNumber;
    public MultiplayerGameView(String username) {
        this.username = username;
    }

    public void showGameState(HashMap<String, String> stateMap) {
        this.stateMap = stateMap;
        playerNumber = Integer.parseInt(String.valueOf(stateMap.get(this.username)));
        int opponentPlayer;
        if(playerNumber == 1) {
            opponentPlayer = 2;
        } else {
            opponentPlayer = 1;
        }
        String letters = "abcdefghijklmnopqrstuvwxyz";
        setCursorPos(2, 2);
        System.out.print("Round " + String.valueOf(stateMap.get("roundNumber")));

        setCursorPos(2, 40);
        System.out.print("State effect: " + String.valueOf(stateMap.get("stateEffect")));

        // Showing player 2 lives and state (shield, poison)
        setCursorPos(4, 2);
        String username = String.valueOf(stateMap.get("player_" + opponentPlayer + "_username"));
        System.out.print(username + ": ");
        for(int i = 0; i < Integer.parseInt(String.valueOf(stateMap.get("player_" + opponentPlayer + "_lives"))); i++) {
            System.out.print(ANSI_RED + "♥" + ANSI_RESET);
        }
        if(String.valueOf(stateMap.get("player_" + opponentPlayer + "_isPoisoned")).equals("true")) {
            System.out.print(" A");
        }

        if(String.valueOf(stateMap.get("player_" + opponentPlayer + "_isHandcuffed")).equals("true")) {
            System.out.print(" M");
        }

        // Showing player 2 consumables
        int cNumber = 0;
        setCursorPos(6, 2);
        System.out.print("Consumables:");
        for (int i = 0; i < ConsumableInterface.getConsumableClassList().size(); i++) {
            setCursorPos(8+cNumber, 2);
            try {
                Method method = Class.forName(ConsumableInterface.getConsumableClassList().get(i).getName()).getMethod("getInstance");
                Consumable c = (Consumable)method.invoke(null);
                for(Map.Entry<String, String> entry : stateMap.entrySet()) {
                    if(("player_" + opponentPlayer + "_" + c.getClass().getSimpleName()).equals(String.valueOf(entry.getKey()))) {
                        System.out.print(letters.charAt(i) + ". " + c.toString() + ": x" + String.valueOf(entry.getValue()));
                        cNumber++;
                    }
                }
            } catch (Exception e) {
                systemError();
                System.exit(1);
//                setCursorPos(50, 1);
//                e.printStackTrace();
            }
        }

        setCursorPos(6, 27);
        System.out.print("Powerups:");
        for (int i = 0; i < PowerupInterface.getPowerupClassList().size(); i++) {
            setCursorPos(8+i, 27);
            try {
                Method method = Class.forName(PowerupInterface.getPowerupClassList().get(i).getName()).getMethod("getInstance");
                Powerup c = (Powerup)method.invoke(null);
                for(Map.Entry<String, String> entry : stateMap.entrySet()) {
                    if(("player_" + opponentPlayer + "_" + c.getClass().getSimpleName()).equals(String.valueOf(entry.getKey()))) {
                        System.out.print(i + 1 + ". " + c.toString() + ": x" + String.valueOf(entry.getValue()));
                    }
                }
            } catch (Exception e) {
                systemError();
                System.exit(1);
            }
        }

        // Showing player 1 lives and state (shield, poison)
        setCursorPos(22, 2);
        System.out.print("You: ");
        for(int i = 0; i < Integer.parseInt(String.valueOf(stateMap.get("player_" + playerNumber + "_lives"))); i++) {
            System.out.print(ANSI_RED + "♥" + ANSI_RESET);
        }
        if(String.valueOf(stateMap.get("player_" + playerNumber + "_isPoisoned")).equals("true")) {
            System.out.print(" A");
        }
        if(String.valueOf(stateMap.get("player_" + playerNumber + "_isShielded")).equals("true")) {
            System.out.print(" S");
        }
        if(String.valueOf(stateMap.get("player_" + playerNumber + "_isHandcuffed")).equals("true")) {
            System.out.print(" M");
        }

        setCursorPos(22, 20);
        System.out.print("Combo: " + String.valueOf(stateMap.get("player_" + playerNumber + "_comboCounter")));
        setCursorPos(22, 35);
        System.out.print("Score: " + String.valueOf(stateMap.get("player_" + playerNumber + "_score")));

        // Showing player 2 consumables
        cNumber = 0;
        setCursorPos(24, 2);
        System.out.print("Consumables:");
        for (int i = 0; i < ConsumableInterface.getConsumableClassList().size(); i++) {
            setCursorPos(26+cNumber, 2);
            try {
                Method method = Class.forName(ConsumableInterface.getConsumableClassList().get(i).getName()).getMethod("getInstance");
                Consumable c = (Consumable)method.invoke(null);
                for(Map.Entry<String, String> entry : stateMap.entrySet()) {
                    if(("player_" + playerNumber + "_" + c.getClass().getSimpleName()).equals(String.valueOf(entry.getKey()))) {
                        System.out.print(letters.charAt(i) + ". " + c.toString() + ": x" + String.valueOf(entry.getValue()));
                        cNumber++;
                    }
                }
            } catch (Exception e) {
                systemError();
                System.exit(1);
            }
        }

        setCursorPos(24, 27);
        System.out.print("Powerups:");
        for (int i = 0; i < PowerupInterface.getPowerupClassList().size(); i++) {
            setCursorPos(26+i, 27);
            try {
                Method method = Class.forName(PowerupInterface.getPowerupClassList().get(i).getName()).getMethod("getInstance");
                Powerup c = (Powerup)method.invoke(null);
                for(Map.Entry<String, String> entry : stateMap.entrySet()) {
                    if(("player_" + playerNumber + "_" + c.getClass().getSimpleName()).equals(String.valueOf(entry.getKey()))) {
                        System.out.print(i + 1 + ". " + c.toString() + ": x" + String.valueOf(entry.getValue()));
                    }
                }
            } catch (Exception e) {
                systemError();
                System.exit(1);
            }
        }
    }

    public void showRoundStartingScreen(String roundNumber) {
        lastAction.add("Starting round " + roundNumber + "...");
    }

    public void showStateEffectActivation(String activation) {
        lastAction.add(activation);
    }

    public void showStateEffectEffect(String effect) {
        lastAction.add(effect);
    }

    public void showConsumableActivation(String string) {
        lastAction.add(string + " used!");
    }

    public void showConsumableEffect(String effect) {
        lastAction.add(effect);
    }

    public void printLastAction() {
        setCursorPos(4, 52);

        if(stateMap.get("player_" + playerNumber + "_turn") != null) {
            if (stateMap.get("player_" + playerNumber + "_turn").equals("1")) {
                customPrint("It's your turn.", "fast", 4, 52);
            } else {
                customPrint("It's your opponent's turn.", "fast", 4, 52);
            }
        }

        if(!lastAction.isEmpty()) {
            for(int i=0; i<lastAction.size(); i++) {
                customPrint(lastAction.get(i), "slow", 6 + (2*i), 52);
            }
            try {
                Thread.sleep(2000);
            } catch (Exception e) {
                showError("sleep");
            }
        }

        lastAction.clear();
    }

    public void showWinner(String winner) {
        lastAction.add("The game is over...");
        lastAction.add("The winner is: " + winner + "!");
    }

    public void showFinalXp(String xp) {
        lastAction.add("You gained " + xp + " xp!");
    }

    public void showLevelAndXp(String user, String level, String xp) {
        lastAction.add(user + ":  Level:" + level + "      XP:" + xp);
    }

    public void showHandcuffedState(boolean state) {
        if(state) {
            lastAction.add("You are handcuffed, you lose your turn!");
        } else {
            lastAction.add("The other player has been freed.");
        }
    }

    public void showPowerupActivation(String powerup) {
        lastAction.add("A " + powerup.toLowerCase() + " has been used!");
    }

    public void showPowerupEffect(Powerup powerup) {
        lastAction.add(powerup.toString() + ": " + powerup.getEffect());
    }

    public void showConsumablesDrawn() {
        lastAction.add("Consumables drawn...");
    }

    public void addLastAction(String s) {
        lastAction.add(s);
    }

    public void showGhostGunDamage() {
        lastAction.add("The damage was doubled!");
    }

    public void showShootingTarget(String target) {
        if(target.equals("1")) {
            lastAction.addFirst("The opponent shot himself!");
        } else {
            lastAction.addFirst("The opponent shoots you!");
        }
    }

    public void showShootingResult(String bulletType) {
        if(bulletType.equals("0")) {
            lastAction.addFirst("The bullet was fake...");
        } else if(bulletType.equals("1")) {
            lastAction.addFirst("The bullet was live!");
        }
    }
}
