package com.zombiemmo.skill;

/**
 * RuneScape-style skill types for the zombie MMO
 */
public enum SkillType {
    // Combat skills
    ATTACK("Attack", "Accuracy and damage in melee combat"),
    STRENGTH("Strength", "Maximum melee damage"),
    DEFENSE("Defense", "Ability to wear armor and avoid hits"),
    HITPOINTS("Hitpoints", "Your health and survivability"),
    RANGED("Ranged", "Accuracy and damage with ranged weapons"),
    MAGIC("Magic", "Magical attacks and spells"),

    // Gathering skills
    MINING("Mining", "Extract ores from rocks"),
    WOODCUTTING("Woodcutting", "Chop down trees for wood"),
    FISHING("Fishing", "Catch fish from fishing spots"),
    FORAGING("Foraging", "Gather plants and herbs"),

    // Production skills
    SMITHING("Smithing", "Create weapons and armor from bars"),
    CRAFTING("Crafting", "Create items from various materials"),
    COOKING("Cooking", "Prepare food for healing"),
    CONSTRUCTION("Construction", "Build bases and fortifications"),

    // Support skills
    SURVIVAL("Survival", "Resist zombie infections and harsh conditions"),
    MEDICINE("Medicine", "Heal yourself and others"),
    ENGINEERING("Engineering", "Create traps and mechanical devices"),
    SCAVENGING("Scavenging", "Find better loot from containers");

    private final String displayName;
    private final String description;

    SkillType(String displayName, String description) {
        this.displayName = displayName;
        this.description = description;
    }

    public String getDisplayName() {
        return displayName;
    }

    public String getDescription() {
        return description;
    }
}
