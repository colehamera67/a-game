package com.zombiemmo.game3d;

import com.jme3.app.SimpleApplication;
import com.jme3.font.BitmapText;
import com.jme3.input.KeyInput;
import com.jme3.input.MouseInput;
import com.jme3.input.controls.*;
import com.jme3.light.AmbientLight;
import com.jme3.light.DirectionalLight;
import com.jme3.material.Material;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Vector3f;
import com.jme3.scene.Geometry;
import com.jme3.scene.Node;
import com.jme3.scene.shape.Box;
import com.jme3.scene.shape.Sphere;
import com.jme3.system.AppSettings;
import com.zombiemmo.entity.Player;
import com.zombiemmo.entity.Position;
import com.zombiemmo.entity.Zombie;
import com.zombiemmo.resource.ResourceNode;
import com.zombiemmo.server.GameServer;
import com.zombiemmo.world.Zone;

/**
 * Main 3D game application using jMonkeyEngine
 */
public class Game3D extends SimpleApplication implements ActionListener {
    private GameServer server;
    private Player player;

    // 3D nodes
    private Node playerNode;
    private Node zombiesNode;
    private Node resourcesNode;
    private Node terrainNode;

    // HUD elements
    private BitmapText hudText;
    private BitmapText statsText;
    private BitmapText controlsText;

    // Movement
    private boolean moveForward = false;
    private boolean moveBackward = false;
    private boolean moveLeft = false;
    private boolean moveRight = false;

    private static final float MOVE_SPEED = 10f;
    private static final float TILE_SIZE = 2f;

    public Game3D(GameServer server, String username) {
        this.server = server;

        // Login player
        if (!server.loginPlayer(username)) {
            throw new RuntimeException("Failed to login player");
        }
        this.player = server.getPlayer(username);

        // Configure app settings
        AppSettings settings = new AppSettings(true);
        settings.setTitle("Zombie MMO - 3D Mode");
        settings.setResolution(1280, 720);
        settings.setSamples(4); // Anti-aliasing
        settings.setVSync(true);
        setSettings(settings);
        setShowSettings(false); // Skip settings dialog
    }

    @Override
    public void simpleInitApp() {
        // Initialize input
        initKeys();

        // Setup camera
        setupCamera();

        // Setup lighting
        setupLighting();

        // Create scene nodes
        terrainNode = new Node("Terrain");
        rootNode.attachChild(terrainNode);

        playerNode = new Node("Player");
        rootNode.attachChild(playerNode);

        zombiesNode = new Node("Zombies");
        rootNode.attachChild(zombiesNode);

        resourcesNode = new Node("Resources");
        rootNode.attachChild(resourcesNode);

        // Build world
        buildTerrain();
        createPlayer();

        // Setup HUD
        setupHUD();

        // Hide mouse cursor for FPS controls
        flyCam.setEnabled(true);
        flyCam.setMoveSpeed(20f);
    }

    private void setupCamera() {
        // Position camera behind and above player
        cam.setLocation(new Vector3f(0, 15, 20));
        cam.lookAt(Vector3f.ZERO, Vector3f.UNIT_Y);
    }

    private void setupLighting() {
        // Ambient light
        AmbientLight ambient = new AmbientLight();
        ambient.setColor(ColorRGBA.White.mult(0.3f));
        rootNode.addLight(ambient);

        // Sun light
        DirectionalLight sun = new DirectionalLight();
        sun.setDirection(new Vector3f(-0.5f, -1f, -0.5f).normalizeLocal());
        sun.setColor(ColorRGBA.White.mult(0.9f));
        rootNode.addLight(sun);
    }

    private void buildTerrain() {
        // Create terrain tiles for visible zones
        for (Zone zone : server.getWorld().getZones()) {
            createZoneTerrain(zone);
        }
    }

    private void createZoneTerrain(Zone zone) {
        Position min = zone.getMinBounds();
        Position max = zone.getMaxBounds();

        // Determine zone color
        ColorRGBA zoneColor = getZoneColor(zone);

        // Create terrain tiles (simplified - create larger chunks)
        int chunkSize = 10; // Create 10x10 tile chunks
        for (int x = min.getX(); x < max.getX(); x += chunkSize) {
            for (int z = min.getY(); z < max.getY(); z += chunkSize) {
                Box groundBox = new Box(chunkSize * TILE_SIZE / 2f, 0.1f, chunkSize * TILE_SIZE / 2f);
                Geometry ground = new Geometry("Ground_" + x + "_" + z, groundBox);

                Material mat = new Material(assetManager, "Common/MatDefs/Light/Lighting.j3md");
                mat.setBoolean("UseMaterialColors", true);
                mat.setColor("Diffuse", zoneColor);
                mat.setColor("Ambient", zoneColor);
                mat.setColor("Specular", ColorRGBA.White);
                mat.setFloat("Shininess", 16f);
                ground.setMaterial(mat);

                // Position in 3D space (convert 2D position to 3D)
                float worldX = (x + chunkSize / 2f) * TILE_SIZE;
                float worldZ = (z + chunkSize / 2f) * TILE_SIZE;
                ground.setLocalTranslation(worldX, 0, worldZ);

                terrainNode.attachChild(ground);
            }
        }
    }

    private ColorRGBA getZoneColor(Zone zone) {
        switch (zone.getName()) {
            case "Safe Haven":
                return new ColorRGBA(0.2f, 0.6f, 0.2f, 1f); // Green
            case "The Outskirts":
                return new ColorRGBA(0.5f, 0.5f, 0.3f, 1f); // Yellow-brown
            case "Abandoned Town":
                return new ColorRGBA(0.4f, 0.4f, 0.5f, 1f); // Gray
            case "Industrial Complex":
                return new ColorRGBA(0.5f, 0.3f, 0.3f, 1f); // Dark red
            case "The Hive":
                return new ColorRGBA(0.4f, 0.1f, 0.1f, 1f); // Very dark red
            default:
                return new ColorRGBA(0.3f, 0.3f, 0.3f, 1f);
        }
    }

    private void createPlayer() {
        // Create player model (blue sphere)
        Sphere playerMesh = new Sphere(32, 32, 0.8f);
        Geometry playerGeom = new Geometry("PlayerModel", playerMesh);

        Material mat = new Material(assetManager, "Common/MatDefs/Light/Lighting.j3md");
        mat.setBoolean("UseMaterialColors", true);
        mat.setColor("Diffuse", new ColorRGBA(0.2f, 0.5f, 1f, 1f)); // Blue
        mat.setColor("Ambient", new ColorRGBA(0.2f, 0.5f, 1f, 1f));
        mat.setColor("Specular", ColorRGBA.White);
        mat.setFloat("Shininess", 64f);
        playerGeom.setMaterial(mat);

        playerGeom.setLocalTranslation(0, 1, 0);
        playerNode.attachChild(playerGeom);

        updatePlayerPosition();
    }

    private void updatePlayerPosition() {
        Position pos = player.getPosition();
        float x = pos.getX() * TILE_SIZE;
        float z = pos.getY() * TILE_SIZE;
        playerNode.setLocalTranslation(x, 1, z);

        // Update camera to follow player
        Vector3f playerPos = playerNode.getLocalTranslation();
        cam.setLocation(playerPos.add(0, 15, 20));
        cam.lookAt(playerPos.add(0, 1, 0), Vector3f.UNIT_Y);
    }

    private void setupHUD() {
        // Main HUD text
        hudText = new BitmapText(guiFont);
        hudText.setSize(guiFont.getCharSet().getRenderedSize());
        hudText.setColor(ColorRGBA.White);
        hudText.setLocalTranslation(10, settings.getHeight() - 10, 0);
        guiNode.attachChild(hudText);

        // Stats text (top right)
        statsText = new BitmapText(guiFont);
        statsText.setSize(guiFont.getCharSet().getRenderedSize());
        statsText.setColor(ColorRGBA.Green);
        statsText.setLocalTranslation(settings.getWidth() - 250, settings.getHeight() - 10, 0);
        guiNode.attachChild(statsText);

        // Controls text (bottom left)
        controlsText = new BitmapText(guiFont);
        controlsText.setSize(guiFont.getCharSet().getRenderedSize() * 0.8f);
        controlsText.setColor(ColorRGBA.Yellow);
        controlsText.setText("Controls:\nWASD - Move\nMouse - Look\nSpace - Jump\nESC - Pause");
        controlsText.setLocalTranslation(10, 100, 0);
        guiNode.attachChild(controlsText);
    }

    private void updateHUD() {
        // Update main HUD
        Zone zone = server.getWorld().getZoneAtPosition(player.getPosition());
        String zoneName = zone != null ? zone.getName() : "Unknown";
        hudText.setText("Zombie MMO 3D - " + player.getUsername() + " - " + zoneName);

        // Update stats
        statsText.setText(String.format(
            "HP: %d/%d\n" +
            "Infection: %d%%\n" +
            "Combat Lvl: %d\n" +
            "Total Lvl: %d\n" +
            "Pos: %s",
            player.getCurrentHealth(),
            player.getMaxHealth(),
            player.getInfectionLevel(),
            player.getCombatLevel(),
            player.getSkillManager().getTotalLevel(),
            player.getPosition()
        ));
    }

    private void updateZombies() {
        // Clear existing zombies
        zombiesNode.detachAllChildren();

        // Add zombies from current and nearby zones
        for (Zone zone : server.getWorld().getZones()) {
            for (Zombie zombie : zone.getZombies()) {
                if (!zombie.isAlive()) continue;

                createZombieModel(zombie);
            }
        }
    }

    private void createZombieModel(Zombie zombie) {
        Position pos = zombie.getPosition();

        // Check if zombie is reasonably close to player (render distance)
        if (Math.abs(pos.getX() - player.getPosition().getX()) > 50 ||
            Math.abs(pos.getY() - player.getPosition().getY()) > 50) {
            return; // Too far, don't render
        }

        // Create zombie model (colored sphere based on type)
        Sphere zombieMesh = new Sphere(16, 16, 0.6f);
        Geometry zombieGeom = new Geometry("Zombie_" + System.identityHashCode(zombie), zombieMesh);

        Material mat = new Material(assetManager, "Common/MatDefs/Light/Lighting.j3md");
        mat.setBoolean("UseMaterialColors", true);

        ColorRGBA zombieColor = getZombieColor(zombie);
        mat.setColor("Diffuse", zombieColor);
        mat.setColor("Ambient", zombieColor);
        mat.setColor("Specular", ColorRGBA.White.mult(0.5f));
        mat.setFloat("Shininess", 32f);
        zombieGeom.setMaterial(mat);

        float x = pos.getX() * TILE_SIZE;
        float z = pos.getY() * TILE_SIZE;
        zombieGeom.setLocalTranslation(x, 1, z);

        zombiesNode.attachChild(zombieGeom);
    }

    private ColorRGBA getZombieColor(Zombie zombie) {
        switch (zombie.getType()) {
            case WALKER:
                return new ColorRGBA(0.5f, 0.7f, 0.5f, 1f); // Light green
            case RUNNER:
                return new ColorRGBA(0.8f, 0.8f, 0.3f, 1f); // Yellow
            case TANK:
            case BLOATER:
                return new ColorRGBA(0.7f, 0.5f, 0.5f, 1f); // Red
            case BOSS:
                return new ColorRGBA(1f, 0.2f, 0.2f, 1f); // Bright red
            default:
                return new ColorRGBA(0.6f, 0.6f, 0.4f, 1f); // Brown
        }
    }

    private void updateResources() {
        // Clear existing resources
        resourcesNode.detachAllChildren();

        // Add resources from current and nearby zones
        for (Zone zone : server.getWorld().getZones()) {
            for (ResourceNode resource : zone.getResources()) {
                createResourceModel(resource);
            }
        }
    }

    private void createResourceModel(ResourceNode resource) {
        Position pos = resource.getPosition();

        // Check render distance
        if (Math.abs(pos.getX() - player.getPosition().getX()) > 50 ||
            Math.abs(pos.getY() - player.getPosition().getY()) > 50) {
            return;
        }

        // Create resource model (small box)
        Box resourceBox = new Box(0.5f, 0.5f, 0.5f);
        Geometry resourceGeom = new Geometry("Resource_" + System.identityHashCode(resource), resourceBox);

        Material mat = new Material(assetManager, "Common/MatDefs/Light/Lighting.j3md");
        mat.setBoolean("UseMaterialColors", true);

        ColorRGBA resourceColor = getResourceColor(resource);
        mat.setColor("Diffuse", resourceColor);
        mat.setColor("Ambient", resourceColor);
        resourceGeom.setMaterial(mat);

        float x = pos.getX() * TILE_SIZE;
        float z = pos.getY() * TILE_SIZE;
        resourceGeom.setLocalTranslation(x, 0.5f, z);

        resourcesNode.attachChild(resourceGeom);
    }

    private ColorRGBA getResourceColor(ResourceNode resource) {
        if (resource.isDepleted()) {
            return new ColorRGBA(0.4f, 0.4f, 0.4f, 1f); // Gray
        }

        switch (resource.getType().getRequiredSkill()) {
            case WOODCUTTING:
                return new ColorRGBA(0.6f, 0.4f, 0.2f, 1f); // Brown
            case MINING:
                return new ColorRGBA(0.7f, 0.7f, 0.7f, 1f); // Light gray
            case FISHING:
                return new ColorRGBA(0.3f, 0.5f, 0.9f, 1f); // Blue
            case FORAGING:
                return new ColorRGBA(0.5f, 0.9f, 0.5f, 1f); // Light green
            default:
                return ColorRGBA.Gray;
        }
    }

    private void initKeys() {
        // Movement keys
        inputManager.addMapping("Forward", new KeyTrigger(KeyInput.KEY_W));
        inputManager.addMapping("Backward", new KeyTrigger(KeyInput.KEY_S));
        inputManager.addMapping("Left", new KeyTrigger(KeyInput.KEY_A));
        inputManager.addMapping("Right", new KeyTrigger(KeyInput.KEY_D));

        inputManager.addListener(this, "Forward", "Backward", "Left", "Right");
    }

    @Override
    public void onAction(String binding, boolean isPressed, float tpf) {
        switch (binding) {
            case "Forward":
                moveForward = isPressed;
                break;
            case "Backward":
                moveBackward = isPressed;
                break;
            case "Left":
                moveLeft = isPressed;
                break;
            case "Right":
                moveRight = isPressed;
                break;
        }
    }

    @Override
    public void simpleUpdate(float tpf) {
        // Update player movement
        if (moveForward || moveBackward || moveLeft || moveRight) {
            handleMovement(tpf);
        }

        // Update game world
        server.getWorld().update();

        // Update 3D scene
        updateZombies();
        updateResources();
        updatePlayerPosition();
        updateHUD();
    }

    private void handleMovement(float tpf) {
        Position currentPos = player.getPosition();
        int newX = currentPos.getX();
        int newY = currentPos.getY();

        float moveAmount = MOVE_SPEED * tpf;

        if (moveForward) {
            newY -= (int) moveAmount;
        }
        if (moveBackward) {
            newY += (int) moveAmount;
        }
        if (moveLeft) {
            newX -= (int) moveAmount;
        }
        if (moveRight) {
            newX += (int) moveAmount;
        }

        if (newX != currentPos.getX() || newY != currentPos.getY()) {
            player.setPosition(new Position(newX, newY));
        }
    }

    @Override
    public void destroy() {
        // Cleanup
        if (player != null) {
            server.logoutPlayer(player.getUsername());
        }
        super.destroy();
    }
}
