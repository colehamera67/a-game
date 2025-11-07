package com.zombiemmo.client;

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

import java.util.List;
import java.util.Scanner;

/**
 * Game client with CLI interface
 */
public class GameClient {
    private final GameServer server;
    private final Scanner scanner;
    private final CombatSystem combatSystem;
    private final GatheringSystem gatheringSystem;
    private Player player;
    private boolean running;

    public GameClient(GameServer server) {
        this.server = server;
        this.scanner = new Scanner(System.in);
        this.combatSystem = new CombatSystem();
        this.gatheringSystem = new GatheringSystem();
        this.running = false;
    }

    /**
     * Start the game client
     */
    public void start() {
        printWelcome();

        // Login
        System.out.print("Enter your username: ");
        String username = scanner.nextLine().trim();

        if (server.loginPlayer(username)) {
            player = server.getPlayer(username);
            System.out.println("\nWelcome to the apocalypse, " + username + "!");
            running = true;
            gameLoop();
        } else {
            System.out.println("Login failed. Username may already be in use.");
        }
    }

    /**
     * Main game loop
     */
    private void gameLoop() {
        while (running && player.isAlive()) {
            displayStatus();
            displayMenu();

            String choice = scanner.nextLine().trim();
            processCommand(choice);

            // Check if player died
            if (!player.isAlive()) {
                System.out.println("\n========================================");
                System.out.println("YOU HAVE DIED!");
                System.out.println("Final Stats:");
                System.out.println(player.displayStats());
                System.out.println("========================================");
                running = false;
            }
        }

        // Logout
        server.logoutPlayer(player.getUsername());
    }

    /**
     * Display current status
     */
    private void displayStatus() {
        System.out.println("\n========================================");
        Zone zone = server.getWorld().getZoneAtPosition(player.getPosition());
        System.out.println("Location: " + (zone != null ? zone.getName() : "Unknown"));
        System.out.printf("HP: %d/%d | Infection: %d%% | Combat Level: %d\n",
            player.getCurrentHealth(), player.getMaxHealth(),
            player.getInfectionLevel(), player.getCombatLevel());
        System.out.println("Position: " + player.getPosition());
        System.out.println("========================================");
    }

    /**
     * Display menu
     */
    private void displayMenu() {
        System.out.println("\nWhat would you like to do?");
        System.out.println("1. Move");
        System.out.println("2. Look around");
        System.out.println("3. Attack zombie");
        System.out.println("4. Gather resources");
        System.out.println("5. View skills");
        System.out.println("6. View inventory");
        System.out.println("7. View stats");
        System.out.println("8. Rest (restore HP)");
        System.out.println("9. Quit");
        System.out.print("\nChoice: ");
    }

    /**
     * Process player command
     */
    private void processCommand(String choice) {
        switch (choice) {
            case "1":
                handleMove();
                break;
            case "2":
                handleLookAround();
                break;
            case "3":
                handleAttack();
                break;
            case "4":
                handleGather();
                break;
            case "5":
                System.out.println("\n" + player.getSkillManager().displaySkills());
                break;
            case "6":
                System.out.println("\n" + player.getInventory().toString());
                break;
            case "7":
                System.out.println("\n" + player.displayStats());
                break;
            case "8":
                handleRest();
                break;
            case "9":
                running = false;
                System.out.println("Thanks for playing!");
                break;
            default:
                System.out.println("Invalid choice.");
        }
    }

    /**
     * Handle player movement
     */
    private void handleMove() {
        System.out.println("\nMove direction (w=north, s=south, a=west, d=east): ");
        String dir = scanner.nextLine().trim().toLowerCase();

        Position currentPos = player.getPosition();
        Position newPos = new Position(currentPos.getX(), currentPos.getY());

        switch (dir) {
            case "w":
                newPos.setY(currentPos.getY() - 5);
                break;
            case "s":
                newPos.setY(currentPos.getY() + 5);
                break;
            case "a":
                newPos.setX(currentPos.getX() - 5);
                break;
            case "d":
                newPos.setX(currentPos.getX() + 5);
                break;
            default:
                System.out.println("Invalid direction.");
                return;
        }

        player.setPosition(newPos);
        Zone zone = server.getWorld().getZoneAtPosition(newPos);
        if (zone != null) {
            System.out.println("You move to " + newPos + " in " + zone.getName());
        } else {
            System.out.println("You move to " + newPos);
        }
    }

    /**
     * Handle looking around
     */
    private void handleLookAround() {
        Zone zone = server.getWorld().getZoneAtPosition(player.getPosition());
        if (zone == null) {
            System.out.println("You are in an unknown area.");
            return;
        }

        System.out.println("\n" + zone.toString());

        List<Zombie> nearbyZombies = zone.getNearbyZombies(player.getPosition(), 10);
        List<ResourceNode> nearbyResources = zone.getNearbyResources(player.getPosition(), 10);

        System.out.println("\nNearby Zombies:");
        if (nearbyZombies.isEmpty()) {
            System.out.println("  None");
        } else {
            for (int i = 0; i < nearbyZombies.size(); i++) {
                System.out.println("  " + (i + 1) + ". " + nearbyZombies.get(i));
            }
        }

        System.out.println("\nNearby Resources:");
        if (nearbyResources.isEmpty()) {
            System.out.println("  None");
        } else {
            for (int i = 0; i < nearbyResources.size(); i++) {
                System.out.println("  " + (i + 1) + ". " + nearbyResources.get(i));
            }
        }
    }

    /**
     * Handle attacking zombies
     */
    private void handleAttack() {
        Zone zone = server.getWorld().getZoneAtPosition(player.getPosition());
        if (zone == null) return;

        List<Zombie> nearbyZombies = zone.getNearbyZombies(player.getPosition(), 3);
        if (nearbyZombies.isEmpty()) {
            System.out.println("No zombies nearby to attack!");
            return;
        }

        System.out.println("\nSelect zombie to attack:");
        for (int i = 0; i < nearbyZombies.size(); i++) {
            System.out.println((i + 1) + ". " + nearbyZombies.get(i));
        }

        System.out.print("Choice: ");
        try {
            int choice = Integer.parseInt(scanner.nextLine().trim()) - 1;
            if (choice < 0 || choice >= nearbyZombies.size()) {
                System.out.println("Invalid choice.");
                return;
            }

            Zombie zombie = nearbyZombies.get(choice);
            System.out.println("\nYou engage " + zombie.getType().getDisplayName() + "!");

            // Combat rounds until one dies
            while (player.isAlive() && zombie.isAlive()) {
                System.out.println("\n--- Combat Round ---");
                CombatRound round = combatSystem.performCombatRound(player, zombie);

                // Display player attack result
                if (round.getPlayerResult().isHit()) {
                    System.out.printf("You hit for %d damage!\n", round.getPlayerResult().getDamage());
                    if (round.getPlayerResult().isKilled()) {
                        System.out.println("You killed the " + zombie.getType().getDisplayName() + "!");
                        System.out.println("You gained " + zombie.getExperienceReward() + " XP!");
                        zone.removeZombie(zombie);
                    }
                } else {
                    System.out.println("You missed!");
                }

                // Display zombie attack result
                if (zombie.isAlive() && round.getZombieResult() != null) {
                    if (round.getZombieResult().isHit()) {
                        System.out.printf("The zombie hits you for %d damage!\n",
                            round.getZombieResult().getDamage());
                        if (round.getZombieResult().isInfected()) {
                            System.out.printf("You've been infected! (+%d%% infection)\n",
                                round.getZombieResult().getInfectionAmount());
                        }
                    } else {
                        System.out.println("The zombie missed!");
                    }
                }

                // Check if player wants to continue or flee
                if (player.isAlive() && zombie.isAlive()) {
                    System.out.print("\nContinue fighting? (y/n): ");
                    String cont = scanner.nextLine().trim().toLowerCase();
                    if (!cont.equals("y")) {
                        System.out.println("You flee from combat!");
                        break;
                    }
                }
            }
        } catch (NumberFormatException e) {
            System.out.println("Invalid input.");
        }
    }

    /**
     * Handle gathering resources
     */
    private void handleGather() {
        Zone zone = server.getWorld().getZoneAtPosition(player.getPosition());
        if (zone == null) return;

        List<ResourceNode> nearbyResources = zone.getNearbyResources(player.getPosition(), 3);
        if (nearbyResources.isEmpty()) {
            System.out.println("No resources nearby to gather!");
            return;
        }

        System.out.println("\nSelect resource to gather:");
        for (int i = 0; i < nearbyResources.size(); i++) {
            System.out.println((i + 1) + ". " + nearbyResources.get(i));
        }

        System.out.print("Choice: ");
        try {
            int choice = Integer.parseInt(scanner.nextLine().trim()) - 1;
            if (choice < 0 || choice >= nearbyResources.size()) {
                System.out.println("Invalid choice.");
                return;
            }

            ResourceNode resource = nearbyResources.get(choice);
            GatheringResult result = gatheringSystem.gatherResource(player, resource);
            System.out.println("\n" + result.getMessage());
        } catch (NumberFormatException e) {
            System.out.println("Invalid input.");
        }
    }

    /**
     * Handle resting
     */
    private void handleRest() {
        System.out.println("\nYou rest for a moment...");
        int healAmount = player.getMaxHealth() / 4;
        player.heal(healAmount);
        System.out.println("You restored " + healAmount + " HP.");

        Zone zone = server.getWorld().getZoneAtPosition(player.getPosition());
        if (zone != null && !zone.getName().equals("Safe Haven")) {
            System.out.println("\nWhile resting, you hear zombies approaching...");
            // Small chance of zombie attack while resting in dangerous areas
        }
    }

    /**
     * Print welcome message
     */
    private void printWelcome() {
        System.out.println("\n========================================");
        System.out.println("    ZOMBIE MMO - Survive the Apocalypse");
        System.out.println("========================================");
        System.out.println("\nThe world has fallen to a zombie plague.");
        System.out.println("Train your skills, fight zombies, and survive!");
        System.out.println("\nFeatures RuneScape-style skills:");
        System.out.println("- Combat skills: Attack, Strength, Defense");
        System.out.println("- Gathering: Mining, Woodcutting, Fishing");
        System.out.println("- Survival: Medicine, Engineering, and more!");
        System.out.println("========================================\n");
    }
}
