package org.luckyshot.Views;

import java.util.HashMap;
import java.util.Scanner;

public class StatsView extends View {
    public void showStats(HashMap<String, String> stats) {
        clearScreen();
        displayHeader();
        setCursorPos(5, 2);
        System.out.println("Player stats");

        setCursorPos(6, 2);
        System.out.println("Username: " + stats.get("username"));
        setCursorPos(7, 2);
        System.out.println("Games played: " + stats.get("games_played"));
        setCursorPos(8, 2);
        System.out.println("Games won: " + stats.get("games_won"));
        setCursorPos(9, 2);
        System.out.println("Win percentage: " + stats.get("win_percentage"));
        setCursorPos(10, 2);
        System.out.println("Total score: " + stats.get("total_score"));
        setCursorPos(11, 2);
        System.out.println("Average score per game: " + stats.get("avg_score_per_game"));
        setCursorPos(12, 2);
        System.out.println("Number of kills: " + stats.get("number_of_kills"));
        setCursorPos(13, 2);
        System.out.println("Number of self shots: " + stats.get("number_of_self_shots"));
        setCursorPos(14, 2);
        System.out.println("Level: " + stats.get("level"));
        setCursorPos(15, 2);
        System.out.println("XP points: " + stats.get("xp"));
        setCursorPos(16, 2);
        System.out.println("Total coins: " + stats.get("total_coins"));

        setCursorPos(18, 2);
        System.out.println("Press Enter key to continue...");
        try {
            System.in.read();
            Scanner sc = new Scanner(System.in);
            sc.nextLine();
        }
        catch(Exception e) {
            showError("Error while reading key", 21, 2);
        }
    }
}
