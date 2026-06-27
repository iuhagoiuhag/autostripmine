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

        general.addEntry(entryBuilder.startIntSlider(Component.literal("Hold Duration (ms)"), config.holdDurationMs, 100, 2000)
                .setDefaultValue(500)
                .setTooltip(Component.literal("Milliseconds to hold the toggle key to activate mining"))
                .setSaveConsumer(v -> config.holdDurationMs = v)
                .build());

        general.addEntry(entryBuilder.startIntSlider(Component.literal("Lava Scan Interval (ticks)"), config.lavaScanInterval, 1, 50)
                .setDefaultValue(10)
                .setTooltip(Component.literal("Ticks between lava detection scans"))
                .setSaveConsumer(v -> config.lavaScanInterval = v)
                .build());

        general.addEntry(entryBuilder.startStrField(Component.literal("Toggle Key"), config.toggleKey)
                .setDefaultValue("CTRL+SHIFT")
                .setTooltip(Component.literal("Key combination to toggle mining (e.g. CTRL+SHIFT, SHIFT+G, G)"))
                .setSaveConsumer(v -> config.toggleKey = v.toUpperCase())
                .build());

        builder.setSavingRunnable(ConfigManager::save);

        return builder.build();
    }
}
