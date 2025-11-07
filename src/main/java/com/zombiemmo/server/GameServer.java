package com.zombiemmo.server;

import com.zombiemmo.entity.Player;
import com.zombiemmo.entity.Position;
import com.zombiemmo.world.GameWorld;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * Main game server
 */
public class GameServer {
    private static final Logger logger = LoggerFactory.getLogger(GameServer.class);
    private static final int TICK_RATE = 600; // Game updates every 600ms (RuneScape-style)

    private final GameWorld world;
    private final Map<String, Player> players;
    private final ScheduledExecutorService scheduler;
    private boolean running;

    public GameServer() {
        this.world = new GameWorld();
        this.players = new ConcurrentHashMap<>();
        this.scheduler = Executors.newScheduledThreadPool(2);
        this.running = false;
    }

    /**
     * Start the game server
     */
    public void start() {
        logger.info("Starting Zombie MMO Server...");
        running = true;

        // Start game loop
        scheduler.scheduleAtFixedRate(this::gameLoop, 0, TICK_RATE, TimeUnit.MILLISECONDS);

        logger.info("Server started successfully!");
        logger.info("Game world initialized with {} zones", world.getZones().size());
    }

    /**
     * Stop the game server
     */
    public void stop() {
        logger.info("Stopping server...");
        running = false;
        scheduler.shutdown();
        logger.info("Server stopped.");
    }

    /**
     * Main game loop - updates world state
     */
    private void gameLoop() {
        try {
            // Update world (zombies, resources, etc.)
            world.update();

            // Update all players
            for (Player player : players.values()) {
                if (player.isAlive()) {
                    // Regenerate health slowly
                    if (player.getCurrentHealth() < player.getMaxHealth()) {
                        player.heal(1);
                    }

                    // Reduce infection slowly if in safe zone
                    Position pos = player.getPosition();
                    if (world.getZoneAtPosition(pos) != null &&
                        world.getZoneAtPosition(pos).getName().equals("Safe Haven")) {
                        if (player.getInfectionLevel() > 0) {
                            player.reduceInfection(1);
                        }
                    }
                }
            }
        } catch (Exception e) {
            logger.error("Error in game loop", e);
        }
    }

    /**
     * Handle player login
     */
    public boolean loginPlayer(String username) {
        if (players.containsKey(username)) {
            logger.warn("Player {} already logged in", username);
            return false;
        }

        // Create new player in safe zone
        Player player = new Player(username, new Position(25, 25));
        players.put(username, player);
        world.addPlayer(player);

        logger.info("Player {} logged in at {}", username, player.getPosition());
        return true;
    }

    /**
     * Handle player logout
     */
    public void logoutPlayer(String username) {
        Player player = players.remove(username);
        if (player != null) {
            world.removePlayer(player);
            logger.info("Player {} logged out", username);
        }
    }

    /**
     * Get a player by username
     */
    public Player getPlayer(String username) {
        return players.get(username);
    }

    public GameWorld getWorld() {
        return world;
    }

    public boolean isRunning() {
        return running;
    }

    public Map<String, Player> getPlayers() {
        return players;
    }
}
