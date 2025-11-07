package com.zombiemmo.gui;

import com.zombiemmo.entity.Player;
import com.zombiemmo.entity.Position;
import com.zombiemmo.entity.Zombie;
import com.zombiemmo.resource.ResourceNode;
import com.zombiemmo.world.GameWorld;
import com.zombiemmo.world.Zone;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.List;

/**
 * Main game map panel showing the world grid
 */
public class GamePanel extends JPanel {
    private static final int TILE_SIZE = 32;
    private static final int VIEW_WIDTH = 25;
    private static final int VIEW_HEIGHT = 20;

    private Player player;
    private GameWorld world;
    private Position selectedPosition;

    public GamePanel(Player player, GameWorld world) {
        this.player = player;
        this.world = world;

        setPreferredSize(new Dimension(VIEW_WIDTH * TILE_SIZE, VIEW_HEIGHT * TILE_SIZE));
        setBackground(Color.BLACK);

        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                handleClick(e.getX(), e.getY());
            }
        });
    }

    private void handleClick(int screenX, int screenY) {
        // Convert screen coordinates to world coordinates
        Position playerPos = player.getPosition();
        int worldX = playerPos.getX() - VIEW_WIDTH / 2 + screenX / TILE_SIZE;
        int worldY = playerPos.getY() - VIEW_HEIGHT / 2 + screenY / TILE_SIZE;

        selectedPosition = new Position(worldX, worldY);
        repaint();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        // Center view on player
        Position playerPos = player.getPosition();
        int startX = playerPos.getX() - VIEW_WIDTH / 2;
        int startY = playerPos.getY() - VIEW_HEIGHT / 2;

        // Draw grid and zones
        for (int y = 0; y < VIEW_HEIGHT; y++) {
            for (int x = 0; x < VIEW_WIDTH; x++) {
                int worldX = startX + x;
                int worldY = startY + y;
                Position pos = new Position(worldX, worldY);

                // Get zone at this position
                Zone zone = world.getZoneAtPosition(pos);
                Color tileColor = getZoneColor(zone);

                // Draw tile
                g2d.setColor(tileColor);
                g2d.fillRect(x * TILE_SIZE, y * TILE_SIZE, TILE_SIZE, TILE_SIZE);

                // Draw grid lines
                g2d.setColor(new Color(0, 0, 0, 50));
                g2d.drawRect(x * TILE_SIZE, y * TILE_SIZE, TILE_SIZE, TILE_SIZE);
            }
        }

        // Draw resources
        for (Zone zone : world.getZones()) {
            for (ResourceNode resource : zone.getResources()) {
                Position resPos = resource.getPosition();
                int screenX = (resPos.getX() - startX) * TILE_SIZE;
                int screenY = (resPos.getY() - startY) * TILE_SIZE;

                if (screenX >= 0 && screenX < VIEW_WIDTH * TILE_SIZE &&
                    screenY >= 0 && screenY < VIEW_HEIGHT * TILE_SIZE) {
                    drawResource(g2d, screenX, screenY, resource);
                }
            }
        }

        // Draw zombies
        for (Zone zone : world.getZones()) {
            for (Zombie zombie : zone.getZombies()) {
                if (!zombie.isAlive()) continue;

                Position zombiePos = zombie.getPosition();
                int screenX = (zombiePos.getX() - startX) * TILE_SIZE;
                int screenY = (zombiePos.getY() - startY) * TILE_SIZE;

                if (screenX >= 0 && screenX < VIEW_WIDTH * TILE_SIZE &&
                    screenY >= 0 && screenY < VIEW_HEIGHT * TILE_SIZE) {
                    drawZombie(g2d, screenX, screenY, zombie);
                }
            }
        }

        // Draw player (always centered)
        int playerScreenX = (VIEW_WIDTH / 2) * TILE_SIZE;
        int playerScreenY = (VIEW_HEIGHT / 2) * TILE_SIZE;
        drawPlayer(g2d, playerScreenX, playerScreenY);

        // Draw selected position highlight
        if (selectedPosition != null) {
            int selX = (selectedPosition.getX() - startX) * TILE_SIZE;
            int selY = (selectedPosition.getY() - startY) * TILE_SIZE;
            g2d.setColor(new Color(255, 255, 0, 100));
            g2d.fillRect(selX, selY, TILE_SIZE, TILE_SIZE);
            g2d.setColor(Color.YELLOW);
            g2d.setStroke(new BasicStroke(2));
            g2d.drawRect(selX, selY, TILE_SIZE, TILE_SIZE);
        }
    }

    private Color getZoneColor(Zone zone) {
        if (zone == null) {
            return new Color(30, 30, 30); // Dark gray for unknown
        }

        switch (zone.getName()) {
            case "Safe Haven":
                return new Color(50, 100, 50); // Green
            case "The Outskirts":
                return new Color(80, 80, 60); // Yellow-brown
            case "Abandoned Town":
                return new Color(60, 60, 70); // Gray-blue
            case "Industrial Complex":
                return new Color(70, 50, 50); // Dark red
            case "The Hive":
                return new Color(50, 20, 20); // Very dark red
            default:
                return new Color(40, 40, 40);
        }
    }

    private void drawPlayer(Graphics2D g2d, int x, int y) {
        // Draw player as blue circle with white outline
        g2d.setColor(Color.WHITE);
        g2d.fillOval(x + 6, y + 6, TILE_SIZE - 12, TILE_SIZE - 12);
        g2d.setColor(new Color(0, 150, 255));
        g2d.fillOval(x + 8, y + 8, TILE_SIZE - 16, TILE_SIZE - 16);

        // Draw health bar above player
        drawHealthBar(g2d, x, y - 8, TILE_SIZE,
            player.getCurrentHealth(), player.getMaxHealth(), Color.GREEN);
    }

    private void drawZombie(Graphics2D g2d, int x, int y, Zombie zombie) {
        // Different colors for different zombie types
        Color zombieColor;
        switch (zombie.getType()) {
            case WALKER:
                zombieColor = new Color(100, 150, 100);
                break;
            case RUNNER:
                zombieColor = new Color(150, 150, 50);
                break;
            case TANK:
                zombieColor = new Color(150, 100, 100);
                break;
            case BOSS:
                zombieColor = new Color(200, 50, 50);
                break;
            default:
                zombieColor = new Color(120, 120, 80);
        }

        // Draw zombie as skull-like shape
        g2d.setColor(zombieColor);
        g2d.fillOval(x + 8, y + 8, TILE_SIZE - 16, TILE_SIZE - 16);
        g2d.setColor(Color.BLACK);
        g2d.fillOval(x + 11, y + 12, 4, 4); // Eye
        g2d.fillOval(x + 17, y + 12, 4, 4); // Eye

        // Draw health bar
        drawHealthBar(g2d, x, y - 8, TILE_SIZE,
            zombie.getCurrentHealth(), zombie.getMaxHealth(), Color.RED);
    }

    private void drawResource(Graphics2D g2d, int x, int y, ResourceNode resource) {
        if (resource.isDepleted()) {
            g2d.setColor(new Color(80, 80, 80));
        } else {
            // Different colors for different resource types
            switch (resource.getType().getRequiredSkill()) {
                case WOODCUTTING:
                    g2d.setColor(new Color(101, 67, 33)); // Brown for trees
                    break;
                case MINING:
                    g2d.setColor(new Color(150, 150, 150)); // Gray for rocks
                    break;
                case FISHING:
                    g2d.setColor(new Color(50, 150, 255)); // Blue for water
                    break;
                case FORAGING:
                    g2d.setColor(new Color(100, 200, 100)); // Light green for plants
                    break;
                default:
                    g2d.setColor(Color.GRAY);
            }
        }

        // Draw resource as a square
        g2d.fillRect(x + 10, y + 10, TILE_SIZE - 20, TILE_SIZE - 20);
        g2d.setColor(Color.BLACK);
        g2d.drawRect(x + 10, y + 10, TILE_SIZE - 20, TILE_SIZE - 20);
    }

    private void drawHealthBar(Graphics2D g2d, int x, int y, int width, int current, int max, Color color) {
        if (max <= 0) return;

        int barWidth = width - 4;
        int barHeight = 4;
        double healthPercent = (double) current / max;

        // Background
        g2d.setColor(Color.BLACK);
        g2d.fillRect(x + 2, y, barWidth, barHeight);

        // Health bar
        g2d.setColor(color);
        g2d.fillRect(x + 2, y, (int) (barWidth * healthPercent), barHeight);

        // Border
        g2d.setColor(Color.WHITE);
        g2d.drawRect(x + 2, y, barWidth, barHeight);
    }

    public Position getSelectedPosition() {
        return selectedPosition;
    }

    public void clearSelection() {
        selectedPosition = null;
        repaint();
    }
}
