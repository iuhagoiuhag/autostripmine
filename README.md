# AutoStripMine

Automated strip mining mod for Minecraft (Fabric).

## Features

- Toggle strip mining with the G key
- Automatically mines blocks in a straight line at player feet and head level
- Lava detection: stops automatically when lava is detected in the mining path
- Humanized rotation movement with drift and oscillation
- Randomized pause timing to mimic human behavior
- Blocks mined counter with periodic longer pauses

## Requirements

- Minecraft 26.1.2
- Fabric Loader 0.19.3+
- Fabric API
- Java 25+

## Installation

1. Install Fabric Loader for Minecraft 26.1.2
2. Install Fabric API
3. Place the mod JAR in your `mods` folder
4. Launch the game

## Usage

Press **G** to toggle strip mining on or off. When activated, the mod will:

1. Lock the current facing direction
2. Begin mining blocks directly ahead at foot and head level
3. Scan up to 5 blocks ahead for lava in a 3x3 column
4. Automatically stop if lava is detected

A chat message confirms activation or deactivation.

## Building

```bash
./gradlew build
```

The output JAR will be in `build/libs/`.

## Icon Attribution

Gold icons created by Freepik - Flaticon (https://www.flaticon.com/free-icons/gold)

## License

Apache License 2.0
