package com.forcohen.chaoscards.models;

import com.google.firebase.auth.FirebaseUser;

public class Player {
    String player;

    public Player() {
    }

    public Player(String player) {
        this.player = player;
    }

    public String getPlayer() {
        return player;
    }

    public void setPlayer(String player) {
        this.player = player;
    }
}
