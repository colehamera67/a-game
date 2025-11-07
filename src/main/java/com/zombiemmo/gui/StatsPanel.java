package com.zombiemmo.gui;

import com.zombiemmo.entity.Player;
import com.zombiemmo.world.GameWorld;
import com.zombiemmo.world.Zone;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import java.awt.*;

/**
 * Panel displaying player stats
 */
public class StatsPanel extends JPanel {
    private Player player;
    private GameWorld world;

    private JLabel nameLabel;
    private JProgressBar healthBar;
    private JProgressBar infectionBar;
    private JLabel combatLevelLabel;
    private JLabel totalLevelLabel;
    private JLabel positionLabel;
    private JLabel zoneLabel;

    public StatsPanel(Player player, GameWorld world) {
        this.player = player;
        this.world = world;

        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        setBorder(BorderFactory.createTitledBorder(
            BorderFactory.createEtchedBorder(),
            "Player Stats",
            TitledBorder.CENTER,
            TitledBorder.TOP,
            new Font("Arial", Font.BOLD, 14)
        ));
        setPreferredSize(new Dimension(250, 0));

        initComponents();
        updateStats();
    }

    private void initComponents() {
        // Player name
        nameLabel = new JLabel();
        nameLabel.setFont(new Font("Arial", Font.BOLD, 16));
        nameLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        add(nameLabel);
        add(Box.createVerticalStrut(10));

        // Health bar
        JPanel healthPanel = new JPanel(new BorderLayout());
        healthPanel.add(new JLabel("Health:"), BorderLayout.WEST);
        healthBar = new JProgressBar(0, 100);
        healthBar.setStringPainted(true);
        healthBar.setForeground(new Color(50, 200, 50));
        healthPanel.add(healthBar, BorderLayout.CENTER);
        add(healthPanel);
        add(Box.createVerticalStrut(5));

        // Infection bar
        JPanel infectionPanel = new JPanel(new BorderLayout());
        infectionPanel.add(new JLabel("Infection:"), BorderLayout.WEST);
        infectionBar = new JProgressBar(0, 100);
        infectionBar.setStringPainted(true);
        infectionBar.setForeground(new Color(200, 50, 50));
        infectionPanel.add(infectionBar, BorderLayout.CENTER);
        add(infectionPanel);
        add(Box.createVerticalStrut(10));

        // Combat level
        combatLevelLabel = new JLabel();
        combatLevelLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        add(combatLevelLabel);
        add(Box.createVerticalStrut(5));

        // Total level
        totalLevelLabel = new JLabel();
        totalLevelLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        add(totalLevelLabel);
        add(Box.createVerticalStrut(10));

        // Position
        positionLabel = new JLabel();
        positionLabel.setFont(new Font("Arial", Font.PLAIN, 12));
        add(positionLabel);
        add(Box.createVerticalStrut(5));

        // Zone
        zoneLabel = new JLabel();
        zoneLabel.setFont(new Font("Arial", Font.PLAIN, 12));
        add(zoneLabel);

        add(Box.createVerticalGlue());
    }

    public void updateStats() {
        nameLabel.setText(player.getUsername());

        // Health
        int healthPercent = (int) (100.0 * player.getCurrentHealth() / player.getMaxHealth());
        healthBar.setValue(healthPercent);
        healthBar.setString(String.format("%d / %d", player.getCurrentHealth(), player.getMaxHealth()));

        // Infection
        infectionBar.setValue(player.getInfectionLevel());
        infectionBar.setString(player.getInfectionLevel() + "%");

        // Levels
        combatLevelLabel.setText("Combat Level: " + player.getCombatLevel());
        totalLevelLabel.setText("Total Level: " + player.getSkillManager().getTotalLevel());

        // Position
        positionLabel.setText("Position: " + player.getPosition());

        // Zone
        Zone zone = world.getZoneAtPosition(player.getPosition());
        if (zone != null) {
            zoneLabel.setText("Zone: " + zone.getName() + " (Lvl " + zone.getLevel() + ")");
        } else {
            zoneLabel.setText("Zone: Unknown");
        }

        repaint();
    }
}
