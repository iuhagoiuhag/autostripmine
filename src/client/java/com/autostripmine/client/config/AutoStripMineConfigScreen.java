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

        general.addEntry(entryBuilder.startIntSlider(Component.literal("Scan Distance"), config.scanDistance, 1, 10)
                .setDefaultValue(5)
                .setTooltip(Component.literal("Blocks to scan ahead for lava"))
                .setSaveConsumer(v -> config.scanDistance = v)
                .build());

        builder.setSavingRunnable(ConfigManager::save);

        return builder.build();
    }
}
