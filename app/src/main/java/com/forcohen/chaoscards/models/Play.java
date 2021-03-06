package com.forcohen.chaoscards.models;

public class Play {
    private String id;
    private EnemyCard enemyCard;
    private Player player;
    private float damageTaken;
    private Jokenpo play;

    public Play() {
    }

    public Play(EnemyCard enemyCard, Player player, float damageTaken, Jokenpo play) {
        this.enemyCard = enemyCard;
        this.player = player;
        this.damageTaken = damageTaken;
        this.play = play;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public EnemyCard getEnemyCard() {
        return enemyCard;
    }

    public void setEnemyCard(EnemyCard enemyCard) {
        this.enemyCard = enemyCard;
    }

    public Player getPlayer() {
        return player;
    }

    public void setPlayer(Player player) {
        this.player = player;
    }

    public float getDamageTaken() {
        return damageTaken;
    }

    public void setDamageTaken(float damageTaken) {
        this.damageTaken = damageTaken;
    }

    public Jokenpo getPlay() {
        return play;
    }

    public void setPlay(Jokenpo play) {
        this.play = play;
    }
}
