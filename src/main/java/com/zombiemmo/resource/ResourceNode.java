package com.zombiemmo.resource;

import com.zombiemmo.entity.Position;

/**
 * Represents a gatherable resource node in the world
 */
public class ResourceNode {
    private final ResourceType type;
    private final Position position;
    private boolean depleted;
    private long respawnTime;

    public ResourceNode(ResourceType type, Position position) {
        this.type = type;
        this.position = position;
        this.depleted = false;
        this.respawnTime = 0;
    }

    /**
     * Attempt to gather from this resource
     * @return true if successfully gathered
     */
    public boolean gather() {
        if (depleted) {
            return false;
        }

        // Deplete the resource
        depleted = true;
        // Resources respawn after 30-60 seconds
        respawnTime = System.currentTimeMillis() + (30000 + (long)(Math.random() * 30000));

        return true;
    }

    /**
     * Update resource state (check for respawn)
     */
    public void update() {
        if (depleted && System.currentTimeMillis() >= respawnTime) {
            depleted = false;
        }
    }

    public ResourceType getType() {
        return type;
    }

    public Position getPosition() {
        return position;
    }

    public boolean isDepleted() {
        return depleted;
    }

    @Override
    public String toString() {
        String status = depleted ? " (Depleted)" : "";
        return type.getDisplayName() + status + " at " + position;
    }
}
