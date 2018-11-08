package com.forcohen.chaoscards.models;

public class EnemyCard {
    private String id;
    private String enemyName;
    private CardType enemyType;
    private float baseDamage;
    private float baseHealth;
    private int level;
    private boolean isABoss;

    public EnemyCard(){

    }

    public EnemyCard(String id, String enemyName, CardType enemyType, float baseDamage, float baseHealth, int level, boolean isABoss) {
        this.id = id;
        this.enemyName = enemyName;
        this.enemyType = enemyType;
        this.baseDamage = baseDamage;
        this.baseHealth = baseHealth;
        this.level = level;
        this.isABoss = isABoss;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getEnemyName() {
        return enemyName;
    }

    public void setEnemyName(String enemyName) {
        this.enemyName = enemyName;
    }

    public CardType getEnemyType() {
        return enemyType;
    }

    public void setEnemyType(CardType enemyType) {
        this.enemyType = enemyType;
    }

    public float getBaseDamage() {
        return baseDamage;
    }

    public void setBaseDamage(float baseDamage) {
        this.baseDamage = baseDamage;
    }

    public float getBaseHealth() {
        return baseHealth;
    }

    public void setBaseHealth(float baseHealth) {
        this.baseHealth = baseHealth;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public boolean isABoss() {
        return isABoss;
    }

    public void setABoss(boolean ABoss) {
        isABoss = ABoss;
    }
}
