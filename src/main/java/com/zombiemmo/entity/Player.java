package com.zombiemmo.entity;

import com.zombiemmo.item.Inventory;
import com.zombiemmo.skill.SkillManager;
import com.zombiemmo.skill.SkillType;

/**
 * Represents a player character
 */
public class Player extends Entity {
    private final String username;
    private final SkillManager skillManager;
    private final Inventory inventory;
    private int infectionLevel; // 0-100, death at 100

    public Player(String username, Position position) {
        super(position);
        this.username = username;
        this.skillManager = new SkillManager();
        this.inventory = new Inventory(28); // RuneScape-style 28 slots
        this.infectionLevel = 0;

        // Set initial HP based on hitpoints level
        this.currentHealth = skillManager.getLevel(SkillType.HITPOINTS) * 10;
        this.maxHealth = currentHealth;
    }

    @Override
    public void updateMaxHealth() {
        this.maxHealth = skillManager.getLevel(SkillType.HITPOINTS) * 10;
        this.currentHealth = Math.min(currentHealth, maxHealth);
    }

    public String getUsername() {
        return username;
    }

    public SkillManager getSkillManager() {
        return skillManager;
    }

    public Inventory getInventory() {
        return inventory;
    }

    public int getInfectionLevel() {
        return infectionLevel;
    }

    public void addInfection(int amount) {
        infectionLevel = Math.min(100, infectionLevel + amount);
        if (infectionLevel >= 100) {
            this.alive = false;
        }
    }

    public void reduceInfection(int amount) {
        infectionLevel = Math.max(0, infectionLevel - amount);
    }

    /**
     * Get combat level
     */
    public int getCombatLevel() {
        return skillManager.getCombatLevel();
    }

    @Override
    public String toString() {
        return String.format("Player[%s] CB:%d HP:%d/%d Pos:%s",
            username, getCombatLevel(), currentHealth, maxHealth, position);
    }

    /**
     * Display player stats
     */
    public String displayStats() {
        StringBuilder sb = new StringBuilder();
        sb.append("=== PLAYER STATS ===\n");
        sb.append(String.format("Username: %s\n", username));
        sb.append(String.format("Combat Level: %d\n", getCombatLevel()));
        sb.append(String.format("Health: %d/%d\n", currentHealth, maxHealth));
        sb.append(String.format("Infection: %d%%\n", infectionLevel));
        sb.append(String.format("Position: %s\n", position));
        sb.append(String.format("Status: %s\n", alive ? "Alive" : "Dead"));
        return sb.toString();
    }
}
