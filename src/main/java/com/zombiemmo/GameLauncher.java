package com.zombiemmo;

import com.zombiemmo.client.GameClient;
import com.zombiemmo.gui.GameGUI;
import com.zombiemmo.server.GameServer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.swing.*;

/**
 * Main launcher for the Zombie MMO game
 * Supports both GUI and CLI modes
 */
public class GameLauncher {
    private static final Logger logger = LoggerFactory.getLogger(GameLauncher.class);

    public static void main(String[] args) {
        try {
            // Check if user wants GUI or CLI mode
            boolean useGUI = shouldUseGUI(args);

            if (useGUI) {
                launchGUIMode();
            } else {
                launchCLIMode();
            }

        } catch (Exception e) {
            logger.error("Error running game", e);
            System.err.println("Failed to start game: " + e.getMessage());
        }
    }

    private static boolean shouldUseGUI(String[] args) {
        // Check command line arguments
        for (String arg : args) {
            if (arg.equalsIgnoreCase("--cli") || arg.equalsIgnoreCase("-c")) {
                return false;
            }
            if (arg.equalsIgnoreCase("--gui") || arg.equalsIgnoreCase("-g")) {
                return true;
            }
        }

        // Default to GUI if no arguments provided
        // Show selection dialog
        if (GraphicsEnvironment.isHeadless()) {
            return false; // No GUI available
        }

        int choice = JOptionPane.showOptionDialog(
            null,
            "Select game mode:",
            "Zombie MMO - Game Mode",
            JOptionPane.YES_NO_OPTION,
            JOptionPane.QUESTION_MESSAGE,
            null,
            new String[]{"GUI Mode", "CLI Mode"},
            "GUI Mode"
        );

        return choice == 0; // 0 = GUI, 1 = CLI
    }

    private static void launchGUIMode() {
        logger.info("Starting Zombie MMO in GUI mode");

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
