# Obsidian Crystal Switcher (Fabric 26.2)

Client-side Fabric mod for Minecraft Java Edition 26.2.

## What it does

When you successfully place obsidian while an End Crystal exists in hotbar slots 1-9, the mod switches your selected hotbar slot to the first End Crystal it finds.

It does not automatically place or attack crystals.

## Requirements

- Minecraft Java Edition 26.2
- Fabric Loader 0.19.3 or newer
- Java 25

Fabric API is not required by this mod.

## Build

This project targets Java 25. With JDK 25 and Gradle installed:

```bash
gradle build
```

The built mod will be in:

`build/libs/obsidian-crystal-switcher-1.0.0.jar`

## Install

Place the built JAR in your `.minecraft/mods` folder and launch Minecraft 26.2 using Fabric Loader.

## Multiplayer

The mod is client-side. After changing the selected slot it sends Minecraft's normal `ServerboundSetCarriedItemPacket` so the server receives the new carried hotbar slot.

Server rules vary; only use client-side mods where they are allowed.
