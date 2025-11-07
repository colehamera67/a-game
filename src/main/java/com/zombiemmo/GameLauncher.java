package com.zombiemmo;

import com.zombiemmo.client.GameClient;
import com.zombiemmo.server.GameServer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Main launcher for the Zombie MMO game
 */
public class GameLauncher {
    private static final Logger logger = LoggerFactory.getLogger(GameLauncher.class);

    public static void main(String[] args) {
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
            logger.error("Error running game", e);
            System.err.println("Failed to start game: " + e.getMessage());
        }
    }
}
