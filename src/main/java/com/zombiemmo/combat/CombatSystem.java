package com.zombiemmo.combat;

import com.zombiemmo.entity.Player;
import com.zombiemmo.entity.Zombie;
import com.zombiemmo.skill.SkillType;

import java.util.Random;

/**
 * RuneScape-style combat system
 */
public class CombatSystem {
    private final Random random = new Random();

    /**
     * Player attacks zombie
     * @return Combat result
     */
    public CombatResult playerAttackZombie(Player player, Zombie zombie) {
        CombatResult result = new CombatResult();

        // Calculate hit chance (RuneScape-style)
        int attackLevel = player.getSkillManager().getLevel(SkillType.ATTACK);
        int strengthLevel = player.getSkillManager().getLevel(SkillType.STRENGTH);

        // Effective attack level
        int effectiveAttack = attackLevel + 8;
        int maxAttackRoll = effectiveAttack * 64;

        // Zombie defense
        int effectiveDefense = zombie.getDefense() + 8;
        int maxDefenseRoll = effectiveDefense * 64;

        // Hit calculation
        int attackRoll = random.nextInt(maxAttackRoll + 1);
        int defenseRoll = random.nextInt(maxDefenseRoll + 1);

        if (attackRoll > defenseRoll) {
            // Hit! Calculate damage
            int maxHit = calculateMaxHit(strengthLevel);
            int damage = random.nextInt(maxHit + 1);

            zombie.damage(damage);
            result.setHit(true);
            result.setDamage(damage);

            // Add combat experience (4 XP per damage for attack, strength, and hitpoints)
            long xp = damage * 4;
            player.getSkillManager().addExperience(SkillType.ATTACK, xp);
            player.getSkillManager().addExperience(SkillType.STRENGTH, xp);
            player.getSkillManager().addExperience(SkillType.HITPOINTS, xp);

            // Check if zombie died
            if (!zombie.isAlive()) {
                result.setKilled(true);
                // Award full kill experience
                long killXp = zombie.getExperienceReward();
                player.getSkillManager().addExperience(SkillType.ATTACK, killXp);
                player.getSkillManager().addExperience(SkillType.STRENGTH, killXp);
                player.getSkillManager().addExperience(SkillType.DEFENSE, killXp);
                player.getSkillManager().addExperience(SkillType.HITPOINTS, killXp / 2);
            }
        } else {
            // Miss
            result.setHit(false);
        }

        return result;
    }

    /**
     * Zombie attacks player
     * @return Combat result
     */
    public CombatResult zombieAttackPlayer(Zombie zombie, Player player) {
        CombatResult result = new CombatResult();

        // Zombie attack roll
        int zombieAttack = zombie.getLevel() + zombie.getMaxDamage();
        int maxAttackRoll = (zombieAttack + 8) * 64;

        // Player defense
        int defenseLevel = player.getSkillManager().getLevel(SkillType.DEFENSE);
        int effectiveDefense = defenseLevel + 8;
        int maxDefenseRoll = effectiveDefense * 64;

        // Hit calculation
        int attackRoll = random.nextInt(maxAttackRoll + 1);
        int defenseRoll = random.nextInt(maxDefenseRoll + 1);

        if (attackRoll > defenseRoll) {
            // Hit!
            int damage = zombie.calculateDamage();
            player.damage(damage);
            result.setHit(true);
            result.setDamage(damage);

            // Add defense experience
            player.getSkillManager().addExperience(SkillType.DEFENSE, damage * 4L);

            // Check for infection
            if (zombie.causesInfection()) {
                int infectionAmount = 5 + random.nextInt(10);
                player.addInfection(infectionAmount);
                result.setInfected(true);
                result.setInfectionAmount(infectionAmount);
            }

            // Check if player died
            if (!player.isAlive()) {
                result.setKilled(true);
            }
        } else {
            // Dodged!
            result.setHit(false);
            // Still get some defense XP for dodging
            player.getSkillManager().addExperience(SkillType.DEFENSE, 2L);
        }

        return result;
    }

    /**
     * Calculate max hit based on strength level (RuneScape-style)
     */
    private int calculateMaxHit(int strengthLevel) {
        // Simplified RuneScape formula
        int effectiveStrength = strengthLevel + 8;
        return (int) (1.3 + effectiveStrength / 10.0 + strengthLevel / 80.0 + (effectiveStrength * strengthLevel) / 640.0);
    }

    /**
     * Perform a full combat round (both entities attack)
     */
    public CombatRound performCombatRound(Player player, Zombie zombie) {
        CombatRound round = new CombatRound();

        // Player attacks first
        CombatResult playerResult = playerAttackZombie(player, zombie);
        round.setPlayerResult(playerResult);

        // If zombie is still alive, it attacks back
        if (zombie.isAlive()) {
            CombatResult zombieResult = zombieAttackPlayer(zombie, player);
            round.setZombieResult(zombieResult);
        }

        return round;
    }
}
