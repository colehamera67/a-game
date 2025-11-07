package com.zombiemmo.resource;

import com.zombiemmo.item.Item;

/**
 * Result of a gathering attempt
 */
public class GatheringResult {
    private boolean success;
    private String message;
    private Item itemGathered;
    private long experienceGained;
    private boolean leveledUp;

    public GatheringResult() {
        this.success = false;
        this.leveledUp = false;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Item getItemGathered() {
        return itemGathered;
    }

    public void setItemGathered(Item itemGathered) {
        this.itemGathered = itemGathered;
    }

    public long getExperienceGained() {
        return experienceGained;
    }

    public void setExperienceGained(long experienceGained) {
        this.experienceGained = experienceGained;
    }

    public boolean isLeveledUp() {
        return leveledUp;
    }

    public void setLeveledUp(boolean leveledUp) {
        this.leveledUp = leveledUp;
    }
}
