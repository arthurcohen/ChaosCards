package com.forcohen.chaoscards.models;

import java.util.ArrayList;
import java.util.List;

public class Trial {
    private String id;
    private List<String> players;
    private boolean isAvailable;
    private EnemyCard enemyCard;
    private List<Play> plays;

    public Trial() {
        this.players = new ArrayList<String>();
        this.plays = new ArrayList<Play>();
    }

    public Trial(List<String> players, EnemyCard enemyCard, List<Play> plays) {
        this.players = players;
        this.enemyCard = enemyCard;
        this.plays = plays;

        this.players = new ArrayList<String>();

        this.plays = new ArrayList<Play>();
    }

    public Trial(String player) {
        this.players = new ArrayList<String>();
        players.add(player);

        this.isAvailable = true;
        this.players = new ArrayList<String>();
        this.plays = new ArrayList<Play>();
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public List<String> getPlayers() {
        return players;
    }

    public void setPlayer(List<String> players) {
        this.players = players;
    }

    public EnemyCard getEnemyCard() {
        return enemyCard;
    }

    public void setEnemyCard(EnemyCard enemyCard) {
        this.enemyCard = enemyCard;
    }

    public List<Play> getPlays() {
        return plays;
    }

    public void setPlays(List<Play> plays) {
        this.plays = plays;
    }

    public boolean isAvailable() {
        return isAvailable;
    }

    public void setAvailable(boolean available) {
        isAvailable = available;
    }
}
