package com.zombiemmo.world;

import com.zombiemmo.entity.Player;
import com.zombiemmo.entity.Position;
import com.zombiemmo.entity.Zombie;
import com.zombiemmo.resource.ResourceNode;
import com.zombiemmo.resource.ResourceType;

import java.util.*;

/**
 * Main game world containing all zones
 */
public class GameWorld {
    private final Map<String, Zone> zones;
    private final List<Player> players;
    private final Random random;

    public GameWorld() {
        this.zones = new HashMap<>();
        this.players = new ArrayList<>();
        this.random = new Random();
        initializeWorld();
    }

    /**
     * Initialize the game world with zones and resources
     */
    private void initializeWorld() {
        // Safe Zone - Starting area
        Zone safeZone = new Zone(
            "Safe Haven",
            "A fortified settlement protected from zombies. A good place to gather basic resources.",
            1,
            new Position(0, 0),
            new Position(50, 50)
        );
        // Add basic resources to safe zone
        safeZone.addResource(new ResourceNode(ResourceType.NORMAL_TREE, new Position(10, 10)));
        safeZone.addResource(new ResourceNode(ResourceType.NORMAL_TREE, new Position(15, 12)));
        safeZone.addResource(new ResourceNode(ResourceType.COPPER_ROCK, new Position(20, 20)));
        safeZone.addResource(new ResourceNode(ResourceType.TIN_ROCK, new Position(22, 22)));
        safeZone.addResource(new ResourceNode(ResourceType.SHRIMP_SPOT, new Position(5, 40)));
        safeZone.addResource(new ResourceNode(ResourceType.HERBS, new Position(30, 30)));
        zones.put("safe_haven", safeZone);

        // Outskirts - Low level area
        Zone outskirts = new Zone(
            "The Outskirts",
            "The edge of civilization. Light zombie presence.",
            5,
            new Position(51, 0),
            new Position(150, 100)
        );
        // Add resources
        outskirts.addResource(new ResourceNode(ResourceType.OAK_TREE, new Position(80, 50)));
        outskirts.addResource(new ResourceNode(ResourceType.OAK_TREE, new Position(90, 55)));
        outskirts.addResource(new ResourceNode(ResourceType.IRON_ROCK, new Position(100, 40)));
        outskirts.addResource(new ResourceNode(ResourceType.BERRIES, new Position(70, 70)));
        // Spawn some zombies
        for (int i = 0; i < 5; i++) {
            Position pos = new Position(
                51 + random.nextInt(100),
                random.nextInt(100)
            );
            outskirts.addZombie(Zombie.createRandom(5, pos));
        }
        zones.put("outskirts", outskirts);

        // Abandoned Town - Medium level area
        Zone town = new Zone(
            "Abandoned Town",
            "A once-thriving town now overrun with zombies. Rich in resources but dangerous.",
            20,
            new Position(151, 0),
            new Position(300, 150)
        );
        // Add resources
        town.addResource(new ResourceNode(ResourceType.WILLOW_TREE, new Position(200, 50)));
        town.addResource(new ResourceNode(ResourceType.COAL_ROCK, new Position(210, 60)));
        town.addResource(new ResourceNode(ResourceType.GOLD_ROCK, new Position(250, 100)));
        town.addResource(new ResourceNode(ResourceType.TROUT_SPOT, new Position(180, 120)));
        town.addResource(new ResourceNode(ResourceType.MUSHROOMS, new Position(220, 80)));
        // Spawn zombies
        for (int i = 0; i < 15; i++) {
            Position pos = new Position(
                151 + random.nextInt(150),
                random.nextInt(150)
            );
            town.addZombie(Zombie.createRandom(20, pos));
        }
        zones.put("town", town);

        // Industrial Zone - High level area
        Zone industrial = new Zone(
            "Industrial Complex",
            "A heavily infected industrial area. Very dangerous but excellent resources.",
            40,
            new Position(301, 0),
            new Position(500, 200)
        );
        // Add high-level resources
        industrial.addResource(new ResourceNode(ResourceType.MAPLE_TREE, new Position(350, 100)));
        industrial.addResource(new ResourceNode(ResourceType.MITHRIL_ROCK, new Position(400, 80)));
        industrial.addResource(new ResourceNode(ResourceType.LOBSTER_SPOT, new Position(320, 150)));
        industrial.addResource(new ResourceNode(ResourceType.RARE_HERBS, new Position(450, 120)));
        // Spawn strong zombies
        for (int i = 0; i < 20; i++) {
            Position pos = new Position(
                301 + random.nextInt(200),
                random.nextInt(200)
            );
            industrial.addZombie(Zombie.createRandom(40, pos));
        }
        zones.put("industrial", industrial);

        // Endgame Zone
        Zone endgame = new Zone(
            "The Hive",
            "The source of the infection. Only the strongest survivors dare enter.",
            70,
            new Position(501, 0),
            new Position(700, 250)
        );
        // Add best resources
        endgame.addResource(new ResourceNode(ResourceType.YEW_TREE, new Position(550, 100)));
        endgame.addResource(new ResourceNode(ResourceType.MAGIC_TREE, new Position(600, 120)));
        endgame.addResource(new ResourceNode(ResourceType.ADAMANT_ROCK, new Position(620, 80)));
        endgame.addResource(new ResourceNode(ResourceType.RUNE_ROCK, new Position(650, 150)));
        endgame.addResource(new ResourceNode(ResourceType.SHARK_SPOT, new Position(580, 200)));
        // Spawn elite zombies
        for (int i = 0; i < 30; i++) {
            Position pos = new Position(
                501 + random.nextInt(200),
                random.nextInt(250)
            );
            endgame.addZombie(Zombie.createRandom(70, pos));
        }
        zones.put("hive", endgame);
    }

    /**
     * Get the zone containing a position
     */
    public Zone getZoneAtPosition(Position pos) {
        for (Zone zone : zones.values()) {
            if (zone.containsPosition(pos)) {
                return zone;
            }
        }
        return null;
    }

    /**
     * Add a player to the world
     */
    public void addPlayer(Player player) {
        players.add(player);
    }

    /**
     * Remove a player from the world
     */
    public void removePlayer(Player player) {
        players.remove(player);
    }

    /**
     * Update all zones
     */
    public void update() {
        for (Zone zone : zones.values()) {
            zone.update();
        }
    }

    public Collection<Zone> getZones() {
        return zones.values();
    }

    public Zone getZone(String id) {
        return zones.get(id);
    }

    public List<Player> getPlayers() {
        return new ArrayList<>(players);
    }
}
