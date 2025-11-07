package com.zombiemmo.resource;

import com.zombiemmo.entity.Player;
import com.zombiemmo.item.Item;
import com.zombiemmo.item.ItemType;

import java.util.Random;

/**
 * Handles resource gathering mechanics
 */
public class GatheringSystem {
    private final Random random = new Random();

    /**
     * Attempt to gather from a resource node
     * @return Result message
     */
    public GatheringResult gatherResource(Player player, ResourceNode node) {
        GatheringResult result = new GatheringResult();

        // Check if node is depleted
        if (node.isDepleted()) {
            result.setSuccess(false);
            result.setMessage("This resource has been depleted.");
            return result;
        }

        // Check skill level requirement
        int playerLevel = player.getSkillManager().getLevel(node.getType().getRequiredSkill());
        if (playerLevel < node.getType().getRequiredLevel()) {
            result.setSuccess(false);
            result.setMessage(String.format("You need level %d %s to gather this resource.",
                node.getType().getRequiredLevel(), node.getType().getRequiredSkill().getDisplayName()));
            return result;
        }

        // Calculate success chance based on level difference
        int levelDifference = playerLevel - node.getType().getRequiredLevel();
        int successChance = Math.min(95, 50 + levelDifference * 2); // 50-95% success rate

        if (random.nextInt(100) < successChance) {
            // Success! Gather the resource
            if (node.gather()) {
                // Create and add item to inventory
                Item gatheredItem = createResourceItem(node.getType());
                boolean addedToInventory = player.getInventory().addItem(gatheredItem, 1);

                if (addedToInventory) {
                    // Award experience
                    boolean leveledUp = player.getSkillManager().addExperience(
                        node.getType().getRequiredSkill(),
                        node.getType().getExperienceReward()
                    );

                    result.setSuccess(true);
                    result.setItemGathered(gatheredItem);
                    result.setExperienceGained(node.getType().getExperienceReward());
                    result.setLeveledUp(leveledUp);
                    result.setMessage(String.format("You gather %s and gain %d %s XP.",
                        gatheredItem.getName(),
                        node.getType().getExperienceReward(),
                        node.getType().getRequiredSkill().getDisplayName()));

                    if (leveledUp) {
                        int newLevel = player.getSkillManager().getLevel(node.getType().getRequiredSkill());
                        result.setMessage(result.getMessage() + String.format(
                            "\nCongratulations! You've advanced to level %d %s!",
                            newLevel, node.getType().getRequiredSkill().getDisplayName()));
                    }
                } else {
                    result.setSuccess(false);
                    result.setMessage("Your inventory is full.");
                }
            }
        } else {
            result.setSuccess(false);
            result.setMessage("You fail to gather the resource.");
        }

        return result;
    }

    /**
     * Create an item from a resource type
     */
    private Item createResourceItem(ResourceType type) {
        String name = type.getDisplayName().replace(" Spot", "").replace(" Rock", "").replace(" Tree", "");
        String description = "A " + name.toLowerCase() + " gathered from " + type.getDisplayName();

        ItemType itemType;
        switch (type.getRequiredSkill()) {
            case WOODCUTTING:
                name = name + " Logs";
                itemType = ItemType.RESOURCE;
                break;
            case MINING:
                name = name + " Ore";
                itemType = ItemType.RESOURCE;
                break;
            case FISHING:
                name = "Raw " + name;
                itemType = ItemType.FOOD;
                break;
            case FORAGING:
                itemType = ItemType.RESOURCE;
                break;
            default:
                itemType = ItemType.MISC;
        }

        return new Item(type.getItemId(), name, description, itemType, true);
    }
}
