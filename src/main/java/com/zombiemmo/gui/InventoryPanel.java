package com.zombiemmo.gui;

import com.zombiemmo.entity.Player;
import com.zombiemmo.item.ItemStack;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.util.List;

/**
 * Panel displaying player inventory
 */
public class InventoryPanel extends JPanel {
    private Player player;
    private JPanel itemsPanel;

    public InventoryPanel(Player player) {
        this.player = player;

        setLayout(new BorderLayout());
        setBorder(BorderFactory.createTitledBorder(
            BorderFactory.createEtchedBorder(),
            "Inventory",
            TitledBorder.CENTER,
            TitledBorder.TOP,
            new Font("Arial", Font.BOLD, 14)
        ));

        // Capacity label
        JLabel capacityLabel = new JLabel();
        capacityLabel.setHorizontalAlignment(SwingConstants.CENTER);
        add(capacityLabel, BorderLayout.NORTH);

        // Items grid (4x7 like RuneScape)
        itemsPanel = new JPanel(new GridLayout(7, 4, 2, 2));
        itemsPanel.setBackground(Color.DARK_GRAY);

        JScrollPane scrollPane = new JScrollPane(itemsPanel);
        add(scrollPane, BorderLayout.CENTER);

        updateInventory();
    }

    public void updateInventory() {
        itemsPanel.removeAll();

        List<ItemStack> items = player.getInventory().getItems();

        // Add items
        for (ItemStack stack : items) {
            JPanel itemSlot = createItemSlot(stack);
            itemsPanel.add(itemSlot);
        }

        // Fill empty slots
        int emptySlots = player.getInventory().getCapacity() - items.size();
        for (int i = 0; i < emptySlots; i++) {
            itemsPanel.add(createEmptySlot());
        }

        // Update capacity label
        Component capacityLabel = ((BorderLayout) getLayout()).getLayoutComponent(BorderLayout.NORTH);
        if (capacityLabel instanceof JLabel) {
            ((JLabel) capacityLabel).setText(String.format("Slots: %d / %d",
                items.size(), player.getInventory().getCapacity()));
        }

        itemsPanel.revalidate();
        itemsPanel.repaint();
    }

    private JPanel createItemSlot(ItemStack stack) {
        JPanel slot = new JPanel();
        slot.setLayout(new BorderLayout());
        slot.setBorder(BorderFactory.createLineBorder(Color.GRAY, 2));
        slot.setBackground(new Color(60, 60, 60));

        // Item name
        JLabel nameLabel = new JLabel(stack.getItem().getName());
        nameLabel.setFont(new Font("Arial", Font.PLAIN, 10));
        nameLabel.setForeground(Color.WHITE);
        nameLabel.setHorizontalAlignment(SwingConstants.CENTER);
        slot.add(nameLabel, BorderLayout.CENTER);

        // Quantity (if > 1)
        if (stack.getQuantity() > 1) {
            JLabel qtyLabel = new JLabel("x" + stack.getQuantity());
            qtyLabel.setFont(new Font("Arial", Font.BOLD, 10));
            qtyLabel.setForeground(Color.YELLOW);
            qtyLabel.setHorizontalAlignment(SwingConstants.RIGHT);
            slot.add(qtyLabel, BorderLayout.SOUTH);
        }

        // Tooltip with description
        slot.setToolTipText("<html>" + stack.getItem().getName() + "<br>" +
            stack.getItem().getDescription() + "</html>");

        return slot;
    }

    private JPanel createEmptySlot() {
        JPanel slot = new JPanel();
        slot.setBorder(BorderFactory.createLineBorder(Color.DARK_GRAY, 1));
        slot.setBackground(new Color(40, 40, 40));
        return slot;
    }
}
