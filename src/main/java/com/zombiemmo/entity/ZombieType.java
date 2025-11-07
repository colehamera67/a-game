package com.zombiemmo.entity;

/**
 * Different types of zombies with varying difficulty
 */
public enum ZombieType {
    WALKER("Walker", 1, 5, 3, 1.0, 5),
    RUNNER("Runner", 5, 8, 5, 1.5, 10),
    TANK("Tank", 10, 15, 10, 0.8, 20),
    SPITTER("Spitter", 8, 12, 4, 1.0, 15),
    SCREAMER("Screamer", 6, 10, 5, 1.2, 12),
    HUNTER("Hunter", 15, 20, 8, 2.0, 30),
    BLOATER("Bloater", 20, 25, 15, 0.7, 40),
    ELITE("Elite Zombie", 30, 40, 12, 1.5, 60),
    BOSS("Zombie Lord", 50, 60, 20, 1.0, 150);

    private final String displayName;
    private final int minLevel;
    private final int maxLevel;
    private final int baseAttack;
    private final double speedMultiplier;
    private final int infectionChance; // % chance to infect on hit

    ZombieType(String displayName, int minLevel, int maxLevel, int baseAttack,
               double speedMultiplier, int infectionChance) {
        this.displayName = displayName;
        this.minLevel = minLevel;
        this.maxLevel = maxLevel;
        this.baseAttack = baseAttack;
        this.speedMultiplier = speedMultiplier;
        this.infectionChance = infectionChance;
    }

    public String getDisplayName() {
        return displayName;
    }

    public int getMinLevel() {
        return minLevel;
    }

    public int getMaxLevel() {
        return maxLevel;
    }

    public int getBaseAttack() {
        return baseAttack;
    }

    public double getSpeedMultiplier() {
        return speedMultiplier;
    }

    public int getInfectionChance() {
        return infectionChance;
    }
}
