package com.zombiemmo.entity;

import java.util.Random;

/**
 * Represents a zombie enemy
 */
public class Zombie extends Entity {
    private final ZombieType type;
    private final int level;
    private final int maxDamage;
    private final int defense;
    private final Random random;

    public Zombie(ZombieType type, int level, Position position) {
        super(position);
        this.type = type;
        this.level = Math.max(type.getMinLevel(), Math.min(level, type.getMaxLevel()));
        this.random = new Random();

        // Calculate stats based on level and type
        this.maxHealth = calculateMaxHealth();
        this.currentHealth = maxHealth;
        this.maxDamage = calculateMaxDamage();
        this.defense = calculateDefense();
    }

    private int calculateMaxHealth() {
        // Base health scales with level
        int baseHealth = level * 10;

        // Type modifiers
        switch (type) {
            case TANK:
            case BLOATER:
                return baseHealth * 2;
            case BOSS:
                return baseHealth * 3;
            case WALKER:
                return (int) (baseHealth * 0.8);
            default:
                return baseHealth;
        }
    }

    private int calculateMaxDamage() {
        return type.getBaseAttack() + (level / 2);
    }

    private int calculateDefense() {
        int baseDefense = level / 2;

        switch (type) {
            case TANK:
            case BLOATER:
                return baseDefense * 2;
            case BOSS:
                return baseDefense * 3;
            case SPITTER:
            case SCREAMER:
                return (int) (baseDefense * 0.7);
            default:
                return baseDefense;
        }
    }

    /**
     * Calculate damage dealt to player
     */
    public int calculateDamage() {
        // Random damage between 1 and maxDamage
        return random.nextInt(maxDamage) + 1;
    }

    /**
     * Check if attack infects the player
     */
    public boolean causesInfection() {
        return random.nextInt(100) < type.getInfectionChance();
    }

    /**
     * Calculate experience gained from killing this zombie
     */
    public long getExperienceReward() {
        // Base XP for the level
        long baseXp = level * 10;

        // Type multipliers
        double multiplier = 1.0;
        switch (type) {
            case RUNNER:
            case HUNTER:
                multiplier = 1.2;
                break;
            case TANK:
            case SPITTER:
                multiplier = 1.5;
                break;
            case SCREAMER:
            case BLOATER:
                multiplier = 1.8;
                break;
            case ELITE:
                multiplier = 2.5;
                break;
            case BOSS:
                multiplier = 5.0;
                break;
        }

        return (long) (baseXp * multiplier);
    }

    public ZombieType getType() {
        return type;
    }

    public int getLevel() {
        return level;
    }

    public int getMaxDamage() {
        return maxDamage;
    }

    public int getDefense() {
        return defense;
    }

    @Override
    public void updateMaxHealth() {
        // Zombie health is fixed at creation
    }

    @Override
    public String toString() {
        return String.format("%s (Lvl %d) HP:%d/%d Pos:%s",
            type.getDisplayName(), level, currentHealth, maxHealth, position);
    }

    /**
     * Create a random zombie for a given area level
     */
    public static Zombie createRandom(int areaLevel, Position position) {
        Random random = new Random();

        // Select zombie type based on area level
        ZombieType type;
        if (areaLevel < 5) {
            type = ZombieType.WALKER;
        } else if (areaLevel < 10) {
            type = random.nextBoolean() ? ZombieType.WALKER : ZombieType.RUNNER;
        } else if (areaLevel < 20) {
            ZombieType[] types = {ZombieType.RUNNER, ZombieType.TANK, ZombieType.SPITTER};
            type = types[random.nextInt(types.length)];
        } else if (areaLevel < 35) {
            ZombieType[] types = {ZombieType.TANK, ZombieType.SPITTER, ZombieType.SCREAMER, ZombieType.HUNTER};
            type = types[random.nextInt(types.length)];
        } else if (areaLevel < 50) {
            ZombieType[] types = {ZombieType.HUNTER, ZombieType.BLOATER, ZombieType.ELITE};
            type = types[random.nextInt(types.length)];
        } else {
            type = random.nextInt(10) == 0 ? ZombieType.BOSS : ZombieType.ELITE;
        }

        // Randomize level slightly around area level
        int level = Math.max(1, areaLevel + random.nextInt(5) - 2);

        return new Zombie(type, level, position);
    }
}
