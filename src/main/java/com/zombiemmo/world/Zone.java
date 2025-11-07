package com.zombiemmo.world;

import com.zombiemmo.entity.Position;
import com.zombiemmo.entity.Zombie;
import com.zombiemmo.resource.ResourceNode;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents a zone in the game world
 */
public class Zone {
    private final String name;
    private final String description;
    private final int level; // Recommended level for this zone
    private final Position minBounds;
    private final Position maxBounds;
    private final List<Zombie> zombies;
    private final List<ResourceNode> resources;

    public Zone(String name, String description, int level, Position minBounds, Position maxBounds) {
        this.name = name;
        this.description = description;
        this.level = level;
        this.minBounds = minBounds;
        this.maxBounds = maxBounds;
        this.zombies = new ArrayList<>();
        this.resources = new ArrayList<>();
    }

    /**
     * Check if a position is within this zone
     */
    public boolean containsPosition(Position pos) {
        return pos.getX() >= minBounds.getX() && pos.getX() <= maxBounds.getX() &&
               pos.getY() >= minBounds.getY() && pos.getY() <= maxBounds.getY();
    }

    /**
     * Add a zombie to this zone
     */
    public void addZombie(Zombie zombie) {
        zombies.add(zombie);
    }

    /**
     * Remove a zombie from this zone
     */
    public void removeZombie(Zombie zombie) {
        zombies.remove(zombie);
    }

    /**
     * Add a resource node to this zone
     */
    public void addResource(ResourceNode resource) {
        resources.add(resource);
    }

    /**
     * Get nearby zombies within range of a position
     */
    public List<Zombie> getNearbyZombies(Position pos, int range) {
        List<Zombie> nearby = new ArrayList<>();
        for (Zombie zombie : zombies) {
            if (zombie.isAlive() && zombie.getPosition().withinRange(pos, range)) {
                nearby.add(zombie);
            }
        }
        return nearby;
    }

    /**
     * Get nearby resources within range of a position
     */
    public List<ResourceNode> getNearbyResources(Position pos, int range) {
        List<ResourceNode> nearby = new ArrayList<>();
        for (ResourceNode resource : resources) {
            if (resource.getPosition().withinRange(pos, range)) {
                nearby.add(resource);
            }
        }
        return nearby;
    }

    /**
     * Update all entities and resources in this zone
     */
    public void update() {
        // Remove dead zombies
        zombies.removeIf(zombie -> !zombie.isAlive());

        // Update resource nodes
        for (ResourceNode resource : resources) {
            resource.update();
        }
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public int getLevel() {
        return level;
    }

    public Position getMinBounds() {
        return minBounds;
    }

    public Position getMaxBounds() {
        return maxBounds;
    }

    public List<Zombie> getZombies() {
        return new ArrayList<>(zombies);
    }

    public List<ResourceNode> getResources() {
        return new ArrayList<>(resources);
    }

    @Override
    public String toString() {
        return String.format("%s (Level %d) - %s\nZombies: %d | Resources: %d",
            name, level, description, zombies.size(), resources.size());
    }
}
