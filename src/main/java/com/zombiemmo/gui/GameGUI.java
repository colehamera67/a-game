package com.zombiemmo.gui;

import com.zombiemmo.combat.CombatRound;
import com.zombiemmo.combat.CombatSystem;
import com.zombiemmo.entity.Player;
import com.zombiemmo.entity.Position;
import com.zombiemmo.entity.Zombie;
import com.zombiemmo.resource.GatheringResult;
import com.zombiemmo.resource.GatheringSystem;
import com.zombiemmo.resource.ResourceNode;
import com.zombiemmo.server.GameServer;
import com.zombiemmo.world.Zone;

import javax.swing.*;
import java.awt.*;
import java.util.List;

/**
 * Main GUI window for the game
 */
public class GameGUI extends JFrame {
    private GameServer server;
    private Player player;
    private CombatSystem combatSystem;
    private GatheringSystem gatheringSystem;

    // Panels
    private GamePanel gamePanel;
    private StatsPanel statsPanel;
    private SkillsPanel skillsPanel;
    private InventoryPanel inventoryPanel;
    private ActionLogPanel logPanel;
    private ControlPanel controlPanel;

    // Game state
    private Timer updateTimer;
    private boolean inCombat = false;
    private Zombie currentTarget = null;

    public GameGUI(GameServer server, String username) {
        this.server = server;
        this.combatSystem = new CombatSystem();
        this.gatheringSystem = new GatheringSystem();

        // Login player
        if (!server.loginPlayer(username)) {
            JOptionPane.showMessageDialog(null,
                "Failed to login. Username may already be in use.",
                "Login Failed",
                JOptionPane.ERROR_MESSAGE);
            System.exit(1);
        }

        this.player = server.getPlayer(username);

        initializeGUI();
        setupEventHandlers();
        startUpdateTimer();

        logPanel.logInfo("Welcome, " + username + "!");
        logPanel.logInfo("You spawn in Safe Haven.");
        logPanel.logInfo("Use WASD or arrow buttons to move around.");
    }

    private void initializeGUI() {
        setTitle("Zombie MMO - " + player.getUsername());
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout(10, 10));

        // Create panels
        gamePanel = new GamePanel(player, server.getWorld());
        statsPanel = new StatsPanel(player, server.getWorld());
        skillsPanel = new SkillsPanel(player);
        inventoryPanel = new InventoryPanel(player);
        logPanel = new ActionLogPanel();
        controlPanel = new ControlPanel();

        // Left side - game view
        JPanel leftPanel = new JPanel(new BorderLayout());
        leftPanel.add(gamePanel, BorderLayout.CENTER);
        leftPanel.add(controlPanel, BorderLayout.SOUTH);

        // Right side - tabs for stats, skills, inventory
        JTabbedPane rightTabs = new JTabbedPane();
        rightTabs.setPreferredSize(new Dimension(300, 0));
        rightTabs.addTab("Stats", statsPanel);
        rightTabs.addTab("Skills", skillsPanel);
        rightTabs.addTab("Inventory", inventoryPanel);

        // Main layout
        add(leftPanel, BorderLayout.CENTER);
        add(rightTabs, BorderLayout.EAST);
        add(logPanel, BorderLayout.SOUTH);

        pack();
        setLocationRelativeTo(null);
        setVisible(true);
    }

    private void setupEventHandlers() {
        // Movement buttons
        controlPanel.getMoveNorthButton().addActionListener(e -> movePlayer(0, -5));
        controlPanel.getMoveSouthButton().addActionListener(e -> movePlayer(0, 5));
        controlPanel.getMoveWestButton().addActionListener(e -> movePlayer(-5, 0));
        controlPanel.getMoveEastButton().addActionListener(e -> movePlayer(5, 0));

        // Action buttons
        controlPanel.getAttackButton().addActionListener(e -> handleAttack());
        controlPanel.getGatherButton().addActionListener(e -> handleGather());
        controlPanel.getRestButton().addActionListener(e -> handleRest());
        controlPanel.getLookAroundButton().addActionListener(e -> handleLookAround());

        // Keyboard shortcuts
        controlPanel.setupKeyboardShortcuts(
            this,
            () -> movePlayer(0, -5),
            () -> movePlayer(0, 5),
            () -> movePlayer(-5, 0),
            () -> movePlayer(5, 0)
        );

        // Window closing
        addWindowListener(new java.awt.event.WindowAdapter() {
            @Override
            public void windowClosing(java.awt.event.WindowEvent e) {
                cleanup();
            }
        });
    }

    private void startUpdateTimer() {
        // Update UI every 100ms
        updateTimer = new Timer(100, e -> updateUI());
        updateTimer.start();
    }

    private void updateUI() {
        if (!player.isAlive()) {
            handleDeath();
            return;
        }

        statsPanel.updateStats();
        gamePanel.repaint();

        // Check infection level
        if (player.getInfectionLevel() >= 80 && player.getInfectionLevel() < 100) {
            // Warn player about high infection
            Zone zone = server.getWorld().getZoneAtPosition(player.getPosition());
            if (zone != null && !zone.getName().equals("Safe Haven")) {
                // Only show warning occasionally
                if (Math.random() < 0.01) {
                    logPanel.logError("Your infection is critical! Return to Safe Haven!");
                }
            }
        }
    }

    private void movePlayer(int dx, int dy) {
        Position currentPos = player.getPosition();
        Position newPos = new Position(currentPos.getX() + dx, currentPos.getY() + dy);

        player.setPosition(newPos);
        gamePanel.clearSelection();

        Zone zone = server.getWorld().getZoneAtPosition(newPos);
        if (zone != null) {
            logPanel.logInfo("Moved to " + newPos + " in " + zone.getName());
        } else {
            logPanel.logInfo("Moved to " + newPos);
        }

        gamePanel.repaint();
        statsPanel.updateStats();
    }

    private void handleAttack() {
        Zone zone = server.getWorld().getZoneAtPosition(player.getPosition());
        if (zone == null) {
            logPanel.logError("No zone found at current position.");
            return;
        }

        List<Zombie> nearbyZombies = zone.getNearbyZombies(player.getPosition(), 3);
        if (nearbyZombies.isEmpty()) {
            logPanel.logError("No zombies nearby to attack!");
            return;
        }

        // Show zombie selection dialog
        Object[] zombieArray = nearbyZombies.toArray();
        Zombie target = (Zombie) JOptionPane.showInputDialog(
            this,
            "Select zombie to attack:",
            "Attack",
            JOptionPane.QUESTION_MESSAGE,
            null,
            zombieArray,
            zombieArray[0]
        );

        if (target != null) {
            performCombat(target, zone);
        }
    }

    private void performCombat(Zombie zombie, Zone zone) {
        logPanel.logCombat("You engage " + zombie.getType().getDisplayName() + " (Lvl " + zombie.getLevel() + ")!");

        // Perform combat round
        CombatRound round = combatSystem.performCombatRound(player, zombie);

        // Log player attack
        if (round.getPlayerResult().isHit()) {
            logPanel.logCombat(String.format("You hit for %d damage!",
                round.getPlayerResult().getDamage()));

            if (round.getPlayerResult().isKilled()) {
                logPanel.logSuccess("You killed the " + zombie.getType().getDisplayName() + "!");
                logPanel.logSkill("Gained " + zombie.getExperienceReward() + " combat XP!");
                zone.removeZombie(zombie);
            }
        } else {
            logPanel.logCombat("You missed!");
        }

        // Log zombie attack
        if (zombie.isAlive() && round.getZombieResult() != null) {
            if (round.getZombieResult().isHit()) {
                logPanel.logCombat(String.format("The zombie hits you for %d damage!",
                    round.getZombieResult().getDamage()));

                if (round.getZombieResult().isInfected()) {
                    logPanel.logError(String.format("You've been infected! (+%d%% infection)",
                        round.getZombieResult().getInfectionAmount()));
                }
            } else {
                logPanel.logCombat("The zombie missed!");
            }
        }

        // Update displays
        skillsPanel.updateSkills();
        gamePanel.repaint();
        statsPanel.updateStats();
    }

    private void handleGather() {
        Zone zone = server.getWorld().getZoneAtPosition(player.getPosition());
        if (zone == null) {
            logPanel.logError("No zone found at current position.");
            return;
        }

        List<ResourceNode> nearbyResources = zone.getNearbyResources(player.getPosition(), 3);
        if (nearbyResources.isEmpty()) {
            logPanel.logError("No resources nearby to gather!");
            return;
        }

        // Show resource selection dialog
        Object[] resourceArray = nearbyResources.toArray();
        ResourceNode resource = (ResourceNode) JOptionPane.showInputDialog(
            this,
            "Select resource to gather:",
            "Gather",
            JOptionPane.QUESTION_MESSAGE,
            null,
            resourceArray,
            resourceArray[0]
        );

        if (resource != null) {
            performGathering(resource);
        }
    }

    private void performGathering(ResourceNode resource) {
        GatheringResult result = gatheringSystem.gatherResource(player, resource);

        if (result.isSuccess()) {
            logPanel.logSuccess(result.getMessage());
            inventoryPanel.updateInventory();
            skillsPanel.updateSkills();
        } else {
            logPanel.logError(result.getMessage());
        }

        gamePanel.repaint();
    }

    private void handleRest() {
        logPanel.logInfo("You rest for a moment...");

        int healAmount = player.getMaxHealth() / 4;
        player.heal(healAmount);
        logPanel.logSuccess("Restored " + healAmount + " HP.");

        Zone zone = server.getWorld().getZoneAtPosition(player.getPosition());
        if (zone != null) {
            if (zone.getName().equals("Safe Haven")) {
                int infectionReduction = 10;
                player.reduceInfection(infectionReduction);
                logPanel.logSuccess("Reduced infection by " + infectionReduction + "%");
            } else {
                logPanel.logInfo("Resting outside Safe Haven is dangerous...");
            }
        }

        statsPanel.updateStats();
    }

    private void handleLookAround() {
        Zone zone = server.getWorld().getZoneAtPosition(player.getPosition());
        if (zone == null) {
            logPanel.logInfo("You are in an unknown area.");
            return;
        }

        logPanel.logInfo("=== " + zone.getName() + " (Level " + zone.getLevel() + ") ===");
        logPanel.logInfo(zone.getDescription());

        List<Zombie> nearbyZombies = zone.getNearbyZombies(player.getPosition(), 10);
        logPanel.logInfo("Nearby zombies: " + nearbyZombies.size());
        for (Zombie zombie : nearbyZombies) {
            logPanel.logInfo("  - " + zombie.toString());
        }

        List<ResourceNode> nearbyResources = zone.getNearbyResources(player.getPosition(), 10);
        logPanel.logInfo("Nearby resources: " + nearbyResources.size());
        for (ResourceNode resource : nearbyResources) {
            logPanel.logInfo("  - " + resource.toString());
        }
    }

    private void handleDeath() {
        updateTimer.stop();

        logPanel.logError("=================================");
        logPanel.logError("YOU HAVE DIED!");
        logPanel.logError("Final Stats:");
        logPanel.logError("Combat Level: " + player.getCombatLevel());
        logPanel.logError("Total Level: " + player.getSkillManager().getTotalLevel());
        logPanel.logError("Total XP: " + player.getSkillManager().getTotalExperience());
        logPanel.logError("=================================");

        int choice = JOptionPane.showConfirmDialog(
            this,
            "You have died!\n\nCombat Level: " + player.getCombatLevel() +
                "\nTotal Level: " + player.getSkillManager().getTotalLevel() +
                "\n\nDo you want to start a new game?",
            "Game Over",
            JOptionPane.YES_NO_OPTION,
            JOptionPane.ERROR_MESSAGE
        );

        if (choice == JOptionPane.YES_OPTION) {
            cleanup();
            // Restart game
            String newUsername = JOptionPane.showInputDialog(this, "Enter your username:");
            if (newUsername != null && !newUsername.trim().isEmpty()) {
                dispose();
                new GameGUI(server, newUsername.trim());
            } else {
                System.exit(0);
            }
        } else {
            cleanup();
            System.exit(0);
        }
    }

    private void cleanup() {
        if (updateTimer != null) {
            updateTimer.stop();
        }
        if (player != null) {
            server.logoutPlayer(player.getUsername());
        }
    }

    public static void main(String[] args) {
        // Set look and feel
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            e.printStackTrace();
        }

        // Start game
        SwingUtilities.invokeLater(() -> {
            String username = JOptionPane.showInputDialog(null, "Enter your username:");
            if (username != null && !username.trim().isEmpty()) {
                GameServer server = new GameServer();
                server.start();

                new GameGUI(server, username.trim());
            } else {
                System.exit(0);
            }
        });
    }
}
