package org.luckyshot.Facades;

import org.luckyshot.Models.User;
import org.luckyshot.Views.StatsView;

import java.util.HashMap;

public class StatsFacade {
    private static StatsFacade instance;
    private User user;

    private StatsFacade(User user) {
        this.user = user;
    }

    public static StatsFacade getInstance(User user) {
        if(instance == null) {
            instance = new StatsFacade(user);
        }
        return instance;
    }

    public void showStats() {
        HashMap<String, String> stats = new HashMap<>();
        stats.put("username", user.getUsername());
        stats.put("games_played", Integer.toString(user.getGamesPlayed()));
        stats.put("games_won", Integer.toString(user.getGamesWon()));
        float winning_perc;
        if (user.getGamesPlayed() == 0) {
            winning_perc = 0;
        } else {
            winning_perc = (float) user.getGamesWon() / user.getGamesPlayed();
        }
        stats.put("win_percentage", Float.toString(winning_perc));
        stats.put("total_score", Long.toString(user.getTotalScore()));
        float avg_score;
        if (user.getGamesPlayed() == 0) {
            avg_score = 0;
        } else {
            avg_score = (float) user.getTotalScore() / user.getGamesPlayed();
        }
        stats.put("avg_score_per_game", Float.toString(avg_score));
        stats.put("number_of_kills", Integer.toString(user.getNumberOfKills()));
        stats.put("number_of_self_shots", Integer.toString(user.getNumberOfSelfShots()));
        stats.put("level", Integer.toString(user.getLevel()));
        stats.put("xp", Integer.toString(user.getXp()));
        int total_coins = (user.getLevel() - 1) * 3;
        stats.put("total_coins", Integer.toString(total_coins));

        StatsView view = new StatsView();
        view.showStats(stats);
    }
}
