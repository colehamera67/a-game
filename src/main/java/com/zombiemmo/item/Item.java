package com.zombiemmo.item;

/**
 * Represents an item in the game
 */
public class Item {
    private final int id;
    private final String name;
    private final String description;
    private final ItemType type;
    private final boolean stackable;

    public Item(int id, String name, String description, ItemType type, boolean stackable) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.type = type;
        this.stackable = stackable;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public ItemType getType() {
        return type;
    }

    public boolean isStackable() {
        return stackable;
    }

    @Override
    public String toString() {
        return name;
    }
}
