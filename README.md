# AutoStripMine

Automated strip mining mod for Minecraft (Fabric).

## Features

* Toggle strip mining with the G key
* Mines blocks in a straight line at player feet and head level
* Configurable lava detection with configurable scan distance
* Humanized rotation movement with smooth drift and sinusoidal oscillation
* Randomized short and long pause timing to mimic human behavior
* Blocks mined counter with periodic longer pauses
* Aim ready state machine for natural targeting adjustment
* Configurable via `config/autostripmine.json` (auto created on first run)
* ModMenu integration for in game configuration (requires Cloth Config)

## Requirements

* Minecraft 26.2
* Fabric Loader 0.19.3+
* Fabric API
* Java 25+

## Installation

1. Install Fabric Loader for Minecraft 26.2
2. Install Fabric API
3. Place the mod JAR in your `mods` folder
4. Launch the game

## Usage

Press **G** to toggle strip mining on or off. When activated, the mod will:

1. Lock the current facing direction
2. Begin mining blocks directly ahead at foot and head level
3. Scan up to 5 blocks ahead for lava in a 3x3 column (configurable)
4. Automatically stop if lava is detected

A chat message confirms activation or deactivation.

## Configuration

All settings are stored in `config/autostripmine.json` and can be edited manually or via ModMenu (if Cloth Config is installed). Configurable parameters include scan distance, pitch angle, reach distance, pause timing, drift parameters, and oscillation amplitudes.

## Building

```bash
./gradlew build
```

The output JAR will be in `build/libs/`.

## Icon Attribution

Gold icons created by Freepik from Flaticon (https://www.flaticon.com/free-icons/gold)

## License

Apache License 2.0
