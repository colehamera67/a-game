package com.zombiemmo.skill;

/**
 * Represents a player skill with experience and level tracking
 * Uses RuneScape-style leveling system (levels 1-99)
 */
public class Skill {
    private final SkillType type;
    private long experience;
    private int level;

    // Experience table for levels 1-99 (RuneScape-style)
    private static final long[] EXP_TABLE = new long[100];

    static {
        int points = 0;
        int output = 0;
        for (int level = 1; level <= 99; level++) {
            EXP_TABLE[level] = output;
            points += Math.floor(level + 300.0 * Math.pow(2.0, level / 7.0));
            output = (int) Math.floor(points / 4);
        }
    }

    public Skill(SkillType type) {
        this.type = type;
        this.experience = 0;
        this.level = 1;
    }

    /**
     * Add experience to this skill
     * @param amount Experience to add
     * @return true if player leveled up
     */
    public boolean addExperience(long amount) {
        int oldLevel = level;
        experience += amount;
        level = getLevelForExperience(experience);
        return level > oldLevel;
    }

    /**
     * Get the current level for given experience
     */
    public static int getLevelForExperience(long experience) {
        for (int level = 99; level >= 1; level--) {
            if (experience >= EXP_TABLE[level]) {
                return level;
            }
        }
        return 1;
    }

    /**
     * Get experience required for next level
     */
    public long getExperienceToNextLevel() {
        if (level >= 99) {
            return 0;
        }
        return EXP_TABLE[level + 1] - experience;
    }

    /**
     * Get experience required for a specific level
     */
    public static long getExperienceForLevel(int level) {
        if (level < 1 || level > 99) {
            return 0;
        }
        return EXP_TABLE[level];
    }

    // Getters
    public SkillType getType() {
        return type;
    }

    public long getExperience() {
        return experience;
    }

    public int getLevel() {
        return level;
    }

    public void setExperience(long experience) {
        this.experience = experience;
        this.level = getLevelForExperience(experience);
    }

    @Override
    public String toString() {
        return String.format("%s: Level %d (XP: %d, Next: %d)",
            type.getDisplayName(), level, experience, getExperienceToNextLevel());
    }
}
