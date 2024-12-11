package org.luckyshot.Models;

import jakarta.persistence.*;
import org.luckyshot.Models.Powerups.Powerup;

import java.util.ArrayList;
import java.util.HashMap;

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

    @Transient
    private HashMap<Powerup, Integer> powerups;

    @Column(name = "level", nullable = false)
    private int level;

    @Column(name = "total_score", nullable = false)
    private long totalScore;

    public User() {

    }

    public User(String username, String password, int coins, int level, long totalScore) {
        this.username = username;
        this.password = password;
        this.coins = coins;
        this.level = level;
        this.totalScore = totalScore;
    }

    public User(String username, String password) {
        this(username, password, 0, 0, 0);
    }

    @Transient
    private HumanPlayer player;

    public HumanPlayer getPlayer() {
        return player;
    }

    public void setPlayer(HumanPlayer player) {
        this.player = player;
    }

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

    public HashMap<Powerup, Integer> getPowerups() {
        return powerups;
    }

    public void setPowerups(HashMap<Powerup, Integer> powerups) {
        this.powerups = powerups;
    }

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
