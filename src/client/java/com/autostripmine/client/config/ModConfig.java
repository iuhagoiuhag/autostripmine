package com.autostripmine.client.config;

import net.fabricmc.loader.api.FabricLoader;
import java.nio.file.Path;

public class ModConfig {
    public String recordingFile = "recordings/stripmine.json";
    public float timeStretch = 1.0f;

    public Path getRecordingPath() {
        return FabricLoader.getInstance().getConfigDir().resolve(recordingFile);
    }
}