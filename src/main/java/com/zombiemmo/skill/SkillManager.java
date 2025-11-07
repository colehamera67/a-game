package com.zombiemmo.skill;

import java.util.EnumMap;
import java.util.Map;

/**
 * Manages all skills for a player
 */
public class SkillManager {
    private final Map<SkillType, Skill> skills;

    public SkillManager() {
        skills = new EnumMap<>(SkillType.class);
        for (SkillType type : SkillType.values()) {
            skills.put(type, new Skill(type));
        }
        // Start with 10 HP (like RuneScape)
        skills.get(SkillType.HITPOINTS).setExperience(Skill.getExperienceForLevel(10));
    }

    /**
     * Get a specific skill
     */
    public Skill getSkill(SkillType type) {
        return skills.get(type);
    }

    /**
     * Add experience to a skill
     * @return true if player leveled up
     */
    public boolean addExperience(SkillType type, long amount) {
        Skill skill = skills.get(type);
        return skill.addExperience(amount);
    }

    /**
     * Get the level of a skill
     */
    public int getLevel(SkillType type) {
        return skills.get(type).getLevel();
    }

    /**
     * Get total level (sum of all skill levels)
     */
    public int getTotalLevel() {
        return skills.values().stream()
            .mapToInt(Skill::getLevel)
            .sum();
    }

    /**
     * Get total experience (sum of all skill experience)
     */
    public long getTotalExperience() {
        return skills.values().stream()
            .mapToLong(Skill::getExperience)
            .sum();
    }

    /**
     * Get combat level (RuneScape-style calculation)
     */
    public int getCombatLevel() {
        double base = 0.25 * (getLevel(SkillType.DEFENSE) + getLevel(SkillType.HITPOINTS));
        double melee = 0.325 * (getLevel(SkillType.ATTACK) + getLevel(SkillType.STRENGTH));
        double ranged = 0.325 * (1.5 * getLevel(SkillType.RANGED));
        double magic = 0.325 * (1.5 * getLevel(SkillType.MAGIC));

        return (int) (base + Math.max(melee, Math.max(ranged, magic)));
    }

    /**
     * Display all skills
     */
    public String displaySkills() {
        StringBuilder sb = new StringBuilder();
        sb.append("=== SKILLS ===\n");
        sb.append(String.format("Combat Level: %d\n", getCombatLevel()));
        sb.append(String.format("Total Level: %d\n\n", getTotalLevel()));

        for (SkillType type : SkillType.values()) {
            Skill skill = skills.get(type);
            sb.append(skill.toString()).append("\n");
        }

        return sb.toString();
    }
}
