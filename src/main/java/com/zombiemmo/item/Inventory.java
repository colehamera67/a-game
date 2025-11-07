package com.zombiemmo.item;

import java.util.ArrayList;
import java.util.List;

/**
 * Player inventory system
 */
public class Inventory {
    private final List<ItemStack> items;
    private final int capacity;

    public Inventory(int capacity) {
        this.capacity = capacity;
        this.items = new ArrayList<>();
    }

    /**
     * Add item to inventory
     * @return true if successfully added
     */
    public boolean addItem(Item item, int quantity) {
        // Check if item is stackable and already exists
        if (item.isStackable()) {
            for (ItemStack stack : items) {
                if (stack.getItem().getId() == item.getId()) {
                    stack.addQuantity(quantity);
                    return true;
                }
            }
        }

        // Add new item stack
        if (items.size() < capacity) {
            items.add(new ItemStack(item, quantity));
            return true;
        }

        return false; // Inventory full
    }

    /**
     * Remove item from inventory
     * @return true if successfully removed
     */
    public boolean removeItem(int itemId, int quantity) {
        for (int i = 0; i < items.size(); i++) {
            ItemStack stack = items.get(i);
            if (stack.getItem().getId() == itemId) {
                if (stack.getQuantity() <= quantity) {
                    items.remove(i);
                } else {
                    stack.removeQuantity(quantity);
                }
                return true;
            }
        }
        return false;
    }

    /**
     * Check if inventory contains item
     */
    public boolean hasItem(int itemId, int quantity) {
        int total = 0;
        for (ItemStack stack : items) {
            if (stack.getItem().getId() == itemId) {
                total += stack.getQuantity();
            }
        }
        return total >= quantity;
    }

    /**
     * Get item count
     */
    public int getItemCount(int itemId) {
        int total = 0;
        for (ItemStack stack : items) {
            if (stack.getItem().getId() == itemId) {
                total += stack.getQuantity();
            }
        }
        return total;
    }

    public List<ItemStack> getItems() {
        return new ArrayList<>(items);
    }

    public int getCapacity() {
        return capacity;
    }

    public int getUsedSlots() {
        return items.size();
    }

    public int getFreeSlots() {
        return capacity - items.size();
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("=== INVENTORY ===\n");
        sb.append(String.format("Slots: %d/%d\n\n", items.size(), capacity));

        if (items.isEmpty()) {
            sb.append("Empty\n");
        } else {
            for (int i = 0; i < items.size(); i++) {
                sb.append(String.format("%d. %s\n", i + 1, items.get(i)));
            }
        }

        return sb.toString();
    }
}
