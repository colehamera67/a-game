package com.zombiemmo.combat;

/**
 * Result of a single combat action
 */
public class CombatResult {
    private boolean hit;
    private int damage;
    private boolean killed;
    private boolean infected;
    private int infectionAmount;

    public CombatResult() {
        this.hit = false;
        this.damage = 0;
        this.killed = false;
        this.infected = false;
        this.infectionAmount = 0;
    }

    public boolean isHit() {
        return hit;
    }

    public void setHit(boolean hit) {
        this.hit = hit;
    }

    public int getDamage() {
        return damage;
    }

    public void setDamage(int damage) {
        this.damage = damage;
    }

    public boolean isKilled() {
        return killed;
    }

    public void setKilled(boolean killed) {
        this.killed = killed;
    }

    public boolean isInfected() {
        return infected;
    }

    public void setInfected(boolean infected) {
        this.infected = infected;
    }

    public int getInfectionAmount() {
        return infectionAmount;
    }

    public void setInfectionAmount(int infectionAmount) {
        this.infectionAmount = infectionAmount;
    }
}
