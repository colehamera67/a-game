package com.zombiemmo;

import com.zombiemmo.client.GameClient;
import com.zombiemmo.game3d.Game3D;
import com.zombiemmo.gui.GameGUI;
import com.zombiemmo.server.GameServer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.swing.*;
import java.awt.GraphicsEnvironment;

/**
 * Main launcher for the Zombie MMO game
 * Supports 3D, 2D GUI, and CLI modes
 */
public class GameLauncher {
    private static final Logger logger = LoggerFactory.getLogger(GameLauncher.class);

    private enum GameMode {
        MODE_3D, MODE_2D_GUI, MODE_CLI
    }

    public static void main(String[] args) {
        try {
            // Determine game mode
            GameMode mode = selectGameMode(args);

            switch (mode) {
                case MODE_3D:
                    launch3DMode();
                    break;
                case MODE_2D_GUI:
                    launch2DGUIMode();
                    break;
                case MODE_CLI:
                    launchCLIMode();
                    break;
            }

        } catch (Exception e) {
            logger.error("Error running game", e);
            System.err.println("Failed to start game: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private static GameMode selectGameMode(String[] args) {
        // Check command line arguments
        for (String arg : args) {
            if (arg.equalsIgnoreCase("--cli") || arg.equalsIgnoreCase("-c")) {
                return GameMode.MODE_CLI;
            }
            if (arg.equalsIgnoreCase("--gui") || arg.equalsIgnoreCase("-g") ||
                arg.equalsIgnoreCase("--2d")) {
                return GameMode.MODE_2D_GUI;
            }
            if (arg.equalsIgnoreCase("--3d") || arg.equalsIgnoreCase("-3")) {
                return GameMode.MODE_3D;
            }
        }

        // Show selection dialog
        if (GraphicsEnvironment.isHeadless()) {
            return GameMode.MODE_CLI; // No GUI available
        }

        int choice = JOptionPane.showOptionDialog(
            null,
            "Select game mode:",
            "Zombie MMO - Game Mode Selection",
            JOptionPane.DEFAULT_OPTION,
            JOptionPane.QUESTION_MESSAGE,
            null,
            new String[]{"3D Mode (NEW!)", "2D GUI Mode", "CLI Mode"},
            "3D Mode (NEW!)"
        );

        switch (choice) {
            case 0:
                return GameMode.MODE_3D;
            case 1:
                return GameMode.MODE_2D_GUI;
            case 2:
                return GameMode.MODE_CLI;
            default:
                return GameMode.MODE_3D; // Default to 3D
        }
    }

    private static void launch3DMode() {
        logger.info("Starting Zombie MMO in 3D mode");

        String username = JOptionPane.showInputDialog(null, "Enter your username:");
        if (username == null || username.trim().isEmpty()) {
            System.exit(0);
            return;
        }

        // Start game server
        GameServer server = new GameServer();
        server.start();

        // Launch 3D game
        Game3D game = new Game3D(server, username.trim());
        game.start();
    }

    private static void launch2DGUIMode() {
        logger.info("Starting Zombie MMO in 2D GUI mode");

        // Set look and feel
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            logger.warn("Failed to set system look and feel", e);
        }

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

    private static void launchCLIMode() {
        logger.info("Starting Zombie MMO in CLI mode");

        try {
            // Start game server
            GameServer server = new GameServer();
            server.start();

            // Give server a moment to initialize
            Thread.sleep(500);

            // Start game client
            GameClient client = new GameClient(server);
            client.start();

            // Shutdown server when client exits
            server.stop();

        } catch (Exception e) {
            logger.error("Error running CLI game", e);
            System.err.println("Failed to start game: " + e.getMessage());
        }
    }
}
