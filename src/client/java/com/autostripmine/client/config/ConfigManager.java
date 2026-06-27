package com.autostripmine.client.config;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import net.fabricmc.loader.api.FabricLoader;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class ConfigManager {
    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();
    private static final Path CONFIG_PATH = FabricLoader.getInstance().getConfigDir().resolve("autostripmine.json");
    private static ModConfig config;

    public static void init() {
        if (Files.exists(CONFIG_PATH)) {
            try {
                config = GSON.fromJson(Files.readString(CONFIG_PATH), ModConfig.class);
            } catch (IOException e) {
                config = new ModConfig();
            }
        }
        if (config == null) {
            config = new ModConfig();
        }
        save();
    }

    public static ModConfig getConfig() {
        if (config == null) {
            config = new ModConfig();
        }
        return config;
    }

    public static void save() {
        try {
            Files.writeString(CONFIG_PATH, GSON.toJson(getConfig()));
        } catch (IOException e) {
            // silently fail
        }
    }

    public static void save(ModConfig newConfig) {
        config = newConfig;
        save();
    }
}