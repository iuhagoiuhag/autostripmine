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
                .setTooltip(Component.literal("Blocks to scan ahead for fluid"))
                .setSaveConsumer(v -> config.scanDistance = v)
                .build());

        general.addEntry(entryBuilder.startIntSlider(Component.literal("Hold Duration (ms)"), config.holdDurationMs, 100, 2000)
                .setDefaultValue(500)
                .setTooltip(Component.literal("Milliseconds to hold the toggle key to activate mining"))
                .setSaveConsumer(v -> config.holdDurationMs = v)
                .build());

        general.addEntry(entryBuilder.startIntSlider(Component.literal("Fluid Scan Interval (ticks)"), config.fluidScanInterval, 1, 50)
                .setDefaultValue(10)
                .setTooltip(Component.literal("Ticks between fluid detection scans"))
                .setSaveConsumer(v -> config.fluidScanInterval = v)
                .build());

        general.addEntry(entryBuilder.startStrField(Component.literal("Toggle Key"), config.toggleKey)
                .setDefaultValue("CTRL+SHIFT")
                .setTooltip(Component.literal("Key combination to toggle mining (e.g. CTRL+SHIFT, SHIFT+G, G)"))
                .setSaveConsumer(v -> config.toggleKey = v.toUpperCase())
                .build());

        general.addEntry(entryBuilder.startBooleanToggle(Component.literal("Auto Eat Enabled"), config.autoEatEnabled)
                .setDefaultValue(true)
                .setTooltip(Component.literal("Automatically eat from offhand when hunger is low while mining"))
                .setSaveConsumer(v -> config.autoEatEnabled = v)
                .build());

        general.addEntry(entryBuilder.startIntSlider(Component.literal("Hunger Threshold"), config.hungerThreshold, 1, 19)
                .setDefaultValue(12)
                .setTooltip(Component.literal("Eat when hunger drops below this level (1-19, where 20 is full)"))
                .setSaveConsumer(v -> config.hungerThreshold = v)
                .build());

        builder.setSavingRunnable(ConfigManager::save);

        return builder.build();
    }
}
