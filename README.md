# Zombie MMO - A RuneScape-Style Survival Game

A Java-based zombie survival MMO with RuneScape-inspired skill progression and gameplay mechanics.

## Features

### Skills System (RuneScape-Style)
Train 18 different skills with experience-based leveling (1-99):

**Combat Skills:**
- Attack - Melee accuracy and damage
- Strength - Maximum melee hit
- Defense - Armor capability and damage reduction
- Hitpoints - Health and survivability
- Ranged - Ranged weapon accuracy and damage
- Magic - Magical attacks and spells

**Gathering Skills:**
- Mining - Extract ores from rocks
- Woodcutting - Chop down trees
- Fishing - Catch fish
- Foraging - Gather plants and herbs

**Production Skills:**
- Smithing - Create weapons and armor
- Crafting - Create various items
- Cooking - Prepare food for healing
- Construction - Build fortifications

**Support Skills:**
- Survival - Resist zombie infections
- Medicine - Healing abilities
- Engineering - Create traps and devices
- Scavenging - Find better loot

### Zombie Types
Battle various zombie types with different difficulty levels:
- **Walker** (Lvl 1-5) - Basic slow zombies
- **Runner** (Lvl 5-8) - Fast, aggressive zombies
- **Tank** (Lvl 10-15) - High health, heavy zombies
- **Spitter** (Lvl 8-12) - Ranged acid attacks
- **Screamer** (Lvl 6-10) - Alerts nearby zombies
- **Hunter** (Lvl 15-20) - Elite fast zombies
- **Bloater** (Lvl 20-25) - Explodes on death
- **Elite** (Lvl 30-40) - Highly dangerous zombies
- **Zombie Lord** (Lvl 50-60) - Boss-level threat

### Game World
Explore 5 distinct zones:
1. **Safe Haven** (Lvl 1) - Protected starting area with basic resources
2. **The Outskirts** (Lvl 5) - Light zombie presence
3. **Abandoned Town** (Lvl 20) - Dangerous but resource-rich
4. **Industrial Complex** (Lvl 40) - Heavy infection, excellent resources
5. **The Hive** (Lvl 70) - Endgame zone, source of the infection

### Combat System
- RuneScape-style combat calculations
- Hit chance based on Attack vs Defense
- Damage based on Strength level
- Experience rewards for combat
- Infection mechanic - zombies can infect you!

### Resource Gathering
- Gather resources from nodes (trees, rocks, fishing spots)
- Level requirements for different resources
- Experience rewards for gathering
- Resources respawn after depletion
- Success rate based on skill level

## Installation & Setup

### Prerequisites
- Java 11 or higher
- Maven 3.6+

### Building the Game

1. Clone the repository:
```bash
git clone <repository-url>
cd a-game
```

2. Build with Maven:
```bash
mvn clean package
```

3. Run the game:
```bash
java -jar target/zombie-mmo-1.0.0.jar
```

Or use Maven directly:
```bash
mvn exec:java -Dexec.mainClass="com.zombiemmo.GameLauncher"
```

## How to Play

### Starting Out
1. Launch the game and enter your username
2. You start in **Safe Haven** at position (25, 25)
3. Check your skills with option 5
4. Look around with option 2 to see nearby resources and zombies

### Basic Commands
- **1. Move** - Move in cardinal directions (W/A/S/D)
- **2. Look Around** - See nearby zombies and resources
- **3. Attack Zombie** - Engage in combat with nearby zombies
- **4. Gather Resources** - Collect resources from nearby nodes
- **5. View Skills** - Display all skills, levels, and experience
- **6. View Inventory** - Check your items (28 slots, RuneScape-style)
- **7. View Stats** - See health, infection level, combat level
- **8. Rest** - Restore health (risky outside Safe Haven)
- **9. Quit** - Exit the game

### Progression Tips

**Early Game (Combat Level 1-10):**
- Stay in Safe Haven and gather basic resources
- Train Woodcutting on Normal Trees
- Train Mining on Copper/Tin rocks
- Train Fishing on Shrimp spots
- When ready, venture to The Outskirts
- Fight Walkers to train combat skills

**Mid Game (Combat Level 10-30):**
- Explore Abandoned Town
- Fight Runners, Tanks, and Spitters
- Gather better resources (Oak trees, Iron/Coal ore)
- Keep infection level low with Medicine skill
- Build up your inventory with useful items

**Late Game (Combat Level 30+):**
- Challenge the Industrial Complex
- Face Hunters, Bloaters, and Elite zombies
- Gather high-tier resources (Mithril, Adamant)
- Prepare for The Hive

**Endgame (Combat Level 50+):**
- Enter The Hive
- Face the Zombie Lord boss
- Gather the rarest resources (Rune ore, Magic trees)
- Master all skills to level 99

### Combat Tips
- Your hit chance is based on Attack level vs enemy Defense
- Damage dealt is based on Strength level
- You gain Defense XP when enemies attack you
- Each successful hit gives 4 XP per damage to Attack, Strength, and Hitpoints
- Killing zombies grants bonus experience
- Watch your infection level - reaching 100% means death!
- Flee from combat if you're low on health

### Gathering Tips
- Higher level resources give more experience
- Success rate improves with skill level
- Resources respawn after 30-60 seconds
- Gather in Safe Haven to avoid zombie interruptions
- Keep inventory space free for gathering

### Survival Tips
- Monitor your infection level
- Rest in Safe Haven to reduce infection
- Health regenerates slowly over time
- Higher Defense reduces damage taken
- Balance combat with gathering for well-rounded progression

## Game Mechanics

### Experience & Leveling
Uses RuneScape's experience formula:
- Levels range from 1 to 99
- Experience required increases exponentially
- Level 1: 0 XP
- Level 50: ~101,333 XP
- Level 99: 13,034,431 XP

### Combat Level Calculation
```
Base = 0.25 × (Defense + Hitpoints)
Melee = 0.325 × (Attack + Strength)
Ranged = 0.325 × (Ranged × 1.5)
Magic = 0.325 × (Magic × 1.5)
Combat Level = Base + Max(Melee, Ranged, Magic)
```

### Infection System
- Zombies have a chance to infect on hit
- Infection ranges from 0-100%
- At 100% infection, you die
- Reduce infection by resting in Safe Haven
- Higher Survival skill reduces infection rate

### Inventory System
- 28 item slots (RuneScape-style)
- Stackable items (resources, ammunition)
- Non-stackable items (weapons, armor)
- Full inventory prevents gathering

## Project Structure

```
src/main/java/com/zombiemmo/
├── combat/          - Combat system and calculations
├── client/          - Game client and UI
├── entity/          - Players, zombies, and entities
├── item/            - Items and inventory system
├── network/         - Network messages (for future multiplayer)
├── resource/        - Resource gathering system
├── server/          - Game server and world management
├── skill/           - Skill system and progression
├── world/           - Game world and zones
└── GameLauncher.java - Main entry point
```

## Future Enhancements

Potential features for future development:
- True multiplayer support with socket networking
- Player vs Player combat
- Trading system
- Crafting and item creation
- Quest system
- Base building with Construction skill
- Parties and clans
- Global leaderboards
- Save/load game progress
- More zombie types and bosses
- Additional zones and areas
- Graphical UI (Swing/JavaFX)

## Technical Details

- **Language:** Java 11
- **Build Tool:** Maven
- **Logging:** SLF4J with Logback
- **Architecture:** Client-Server (currently single-player)
- **Game Loop:** 600ms tick rate (RuneScape-style)

## Development

### Running Tests
```bash
mvn test
```

### Building without Tests
```bash
mvn clean package -DskipTests
```

### Generating Javadocs
```bash
mvn javadoc:javadoc
```

## Credits

Inspired by:
- **RuneScape** - Skill system and progression mechanics
- **Project Zomboid** - Survival and zombie mechanics
- **DayZ** - Post-apocalyptic setting

## License

This project is open source and available for educational purposes.

## Contact

For questions, issues, or contributions, please open an issue on the repository.

---

**Survive the apocalypse. Train your skills. Become a legend.**
