package com.autostripmine.client.config;

import me.shedaniel.clothconfig2.api.ConfigBuilder;
import me.shedaniel.clothconfig2.api.ConfigCategory;
import me.shedaniel.clothconfig2.api.ConfigEntryBuilder;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;

public class AutoStripMineConfigScreen {
    public static Screen create(Screen parent) {
        ModConfig config = ConfigManager.getConfig();

        ConfigBuilder builder = ConfigBuilder.create()
                .setParentScreen(parent)
                .setTitle(Component.literal("AutoStripMine Config"));

        ConfigEntryBuilder entryBuilder = builder.entryBuilder();
        ConfigCategory general = builder.getOrCreateCategory(Component.literal("General"));

        general.addEntry(entryBuilder.startStrField(Component.literal("Recording File"), config.recordingFile)
                .setDefaultValue("recordings/stripmine.json")
                .setTooltip(Component.literal("Path to recording file (relative to config dir)"))
                .setSaveConsumer(v -> config.recordingFile = v)
                .build());

        builder.setSavingRunnable(ConfigManager::save);

        return builder.build();
    }
}