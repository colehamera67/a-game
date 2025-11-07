package com.zombiemmo.entity;

/**
 * Base class for all entities (players, zombies, NPCs)
 */
public abstract class Entity {
    protected Position position;
    protected int currentHealth;
    protected int maxHealth;
    protected boolean alive;

    public Entity(Position position) {
        this.position = position;
        this.alive = true;
    }

    public Position getPosition() {
        return position;
    }

    public void setPosition(Position position) {
        this.position = position;
    }

    public int getCurrentHealth() {
        return currentHealth;
    }

    public int getMaxHealth() {
        return maxHealth;
    }

    public void setCurrentHealth(int health) {
        this.currentHealth = Math.max(0, Math.min(health, maxHealth));
        if (this.currentHealth == 0) {
            this.alive = false;
        }
    }

    public void damage(int amount) {
        setCurrentHealth(currentHealth - amount);
    }

    public void heal(int amount) {
        setCurrentHealth(currentHealth + amount);
    }

    public boolean isAlive() {
        return alive;
    }

    public void setAlive(boolean alive) {
        this.alive = alive;
    }

    /**
     * Update max health based on stats (overridden by subclasses)
     */
    public abstract void updateMaxHealth();
}
