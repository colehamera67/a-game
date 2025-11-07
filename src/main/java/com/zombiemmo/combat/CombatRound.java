package com.zombiemmo.combat;

/**
 * Represents a full round of combat (both player and zombie attacking)
 */
public class CombatRound {
    private CombatResult playerResult;
    private CombatResult zombieResult;

    public CombatRound() {
    }

    public CombatResult getPlayerResult() {
        return playerResult;
    }

    public void setPlayerResult(CombatResult playerResult) {
        this.playerResult = playerResult;
    }

    public CombatResult getZombieResult() {
        return zombieResult;
    }

    public void setZombieResult(CombatResult zombieResult) {
        this.zombieResult = zombieResult;
    }
}
