package org.luckyshot.Models;

import jakarta.persistence.*;
import org.luckyshot.Models.Powerups.Powerup;

import java.util.ArrayList;

@Entity
@Table(name = "users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "username", nullable = false)
    private String username;

    @Column(name = "password", nullable = false)
    private String password;

    @Column(name = "coins", nullable = false)
    private int coins;

    //@Column(name = "powerups", nullable = false)
    //private ArrayList<Powerup> powerups;

    @Column(name = "level", nullable = false)
    private int level;

    @Column(name = "total_score", nullable = false)
    private long totalScore;

    public User() {

    }

    //private HumanPlayer player;

//    public HumanPlayer getPlayer() {
//        return player;
//    }

//    public void setPlayer(HumanPlayer player) {
//        this.player = player;
//    }

    public Long getId() {
        return id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public int getCoins() {
        return coins;
    }

    public void setCoins(int coins) {
        this.coins = coins;
    }

//    public ArrayList<Powerup> getPowerups() {
//        return powerups;
//    }
//
//    public void setPowerups(ArrayList<Powerup> powerups) {
//        this.powerups = powerups;
//    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public long getTotalScore() {
        return totalScore;
    }

    public void setTotalScore(long totalScore) {
        this.totalScore = totalScore;
    }
}
