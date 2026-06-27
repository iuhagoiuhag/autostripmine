package com.autostripmine.client.config;

import com.google.gson.GsonBuilder;
import com.google.gson.Gson;
import net.fabricmc.loader.api.FabricLoader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class ConfigManager {
    private static final Logger LOGGER = LoggerFactory.getLogger("AutoStripMine");
    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();
    private static final Path CONFIG_PATH = FabricLoader.getInstance().getConfigDir().resolve("autostripmine.json");
    private static ModConfig config;

    public static void init() {
        if (Files.exists(CONFIG_PATH)) {
            try {
                config = GSON.fromJson(Files.readString(CONFIG_PATH), ModConfig.class);
                if (config != null) config.validate();
            } catch (IOException e) {
                LOGGER.warn("Failed to load config, using defaults", e);
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
            LOGGER.error("Failed to save config", e);
        }
    }

    public static void save(ModConfig newConfig) {
        config = newConfig;
        save();
    }
}
