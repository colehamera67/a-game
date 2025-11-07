package com.zombiemmo.resource;

import com.zombiemmo.skill.SkillType;

/**
 * Types of gatherable resources
 */
public enum ResourceType {
    // Trees (Woodcutting)
    NORMAL_TREE("Normal Tree", SkillType.WOODCUTTING, 1, 25, 1),
    OAK_TREE("Oak Tree", SkillType.WOODCUTTING, 15, 37, 2),
    WILLOW_TREE("Willow Tree", SkillType.WOODCUTTING, 30, 67, 3),
    MAPLE_TREE("Maple Tree", SkillType.WOODCUTTING, 45, 100, 4),
    YEW_TREE("Yew Tree", SkillType.WOODCUTTING, 60, 175, 5),
    MAGIC_TREE("Magic Tree", SkillType.WOODCUTTING, 75, 250, 6),

    // Rocks (Mining)
    COPPER_ROCK("Copper Rock", SkillType.MINING, 1, 17, 1),
    TIN_ROCK("Tin Rock", SkillType.MINING, 1, 17, 1),
    IRON_ROCK("Iron Rock", SkillType.MINING, 15, 35, 2),
    COAL_ROCK("Coal Rock", SkillType.MINING, 30, 50, 3),
    GOLD_ROCK("Gold Rock", SkillType.MINING, 40, 65, 4),
    MITHRIL_ROCK("Mithril Rock", SkillType.MINING, 55, 80, 5),
    ADAMANT_ROCK("Adamant Rock", SkillType.MINING, 70, 95, 6),
    RUNE_ROCK("Rune Rock", SkillType.MINING, 85, 125, 7),

    // Fishing spots
    SHRIMP_SPOT("Shrimp/Anchovies Spot", SkillType.FISHING, 1, 10, 1),
    TROUT_SPOT("Trout/Salmon Spot", SkillType.FISHING, 20, 50, 2),
    LOBSTER_SPOT("Lobster Spot", SkillType.FISHING, 40, 90, 3),
    SWORDFISH_SPOT("Swordfish Spot", SkillType.FISHING, 50, 100, 4),
    SHARK_SPOT("Shark Spot", SkillType.FISHING, 76, 110, 5),

    // Plants (Foraging)
    HERBS("Herbs", SkillType.FORAGING, 1, 15, 1),
    BERRIES("Berries", SkillType.FORAGING, 10, 25, 2),
    MUSHROOMS("Mushrooms", SkillType.FORAGING, 20, 40, 3),
    RARE_HERBS("Rare Herbs", SkillType.FORAGING, 40, 75, 4);

    private final String displayName;
    private final SkillType requiredSkill;
    private final int requiredLevel;
    private final long experienceReward;
    private final int itemId;

    ResourceType(String displayName, SkillType requiredSkill, int requiredLevel,
                 long experienceReward, int itemId) {
        this.displayName = displayName;
        this.requiredSkill = requiredSkill;
        this.requiredLevel = requiredLevel;
        this.experienceReward = experienceReward;
        this.itemId = itemId;
    }

    public String getDisplayName() {
        return displayName;
    }

    public SkillType getRequiredSkill() {
        return requiredSkill;
    }

    public int getRequiredLevel() {
        return requiredLevel;
    }

    public long getExperienceReward() {
        return experienceReward;
    }

    public int getItemId() {
        return itemId;
    }
}
