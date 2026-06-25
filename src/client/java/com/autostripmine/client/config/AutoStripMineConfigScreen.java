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

        general.addEntry(entryBuilder.startFloatField(Component.literal("Strip Pitch"), config.stripPitch)
                .setDefaultValue(35.0f)
                .setTooltip(Component.literal("Head pitch angle while mining"))
                .setSaveConsumer(v -> config.stripPitch = v)
                .build());

        general.addEntry(entryBuilder.startDoubleField(Component.literal("Reach Distance"), config.reachDistance)
                .setDefaultValue(4.5)
                .setTooltip(Component.literal("Max block reach distance"))
                .setSaveConsumer(v -> config.reachDistance = v)
                .build());

        general.addEntry(entryBuilder.startIntSlider(Component.literal("Short Pause Min"), config.shortPauseMin, 0, 100)
                .setDefaultValue(4)
                .setTooltip(Component.literal("Minimum short pause (ticks)"))
                .setSaveConsumer(v -> config.shortPauseMin = v)
                .build());

        general.addEntry(entryBuilder.startIntSlider(Component.literal("Short Pause Max"), config.shortPauseMax, 0, 100)
                .setDefaultValue(8)
                .setTooltip(Component.literal("Maximum short pause (ticks)"))
                .setSaveConsumer(v -> config.shortPauseMax = v)
                .build());

        general.addEntry(entryBuilder.startFloatField(Component.literal("Pause Chance %"), config.shortPauseChance * 100)
                .setDefaultValue(8.0f)
                .setTooltip(Component.literal("Probability of short pause per block"))
                .setSaveConsumer(v -> config.shortPauseChance = v / 100f)
                .build());

        general.addEntry(entryBuilder.startIntSlider(Component.literal("Long Pause Min"), config.longPauseMin, 0, 200)
                .setDefaultValue(15)
                .setTooltip(Component.literal("Minimum long pause (ticks)"))
                .setSaveConsumer(v -> config.longPauseMin = v)
                .build());

        general.addEntry(entryBuilder.startIntSlider(Component.literal("Long Pause Max"), config.longPauseMax, 0, 200)
                .setDefaultValue(25)
                .setTooltip(Component.literal("Maximum long pause (ticks)"))
                .setSaveConsumer(v -> config.longPauseMax = v)
                .build());

        general.addEntry(entryBuilder.startIntSlider(Component.literal("Blocks / Long Pause Min"), config.blocksPerLongPauseMin, 0, 200)
                .setDefaultValue(35)
                .setTooltip(Component.literal("Min blocks before long pause"))
                .setSaveConsumer(v -> config.blocksPerLongPauseMin = v)
                .build());

        general.addEntry(entryBuilder.startIntSlider(Component.literal("Blocks / Long Pause Max"), config.blocksPerLongPauseMax, 0, 200)
                .setDefaultValue(50)
                .setTooltip(Component.literal("Max blocks before long pause"))
                .setSaveConsumer(v -> config.blocksPerLongPauseMax = v)
                .build());

        general.addEntry(entryBuilder.startIntSlider(Component.literal("Drift Interval Min"), config.driftIntervalMin, 0, 200)
                .setDefaultValue(40)
                .setTooltip(Component.literal("Min ticks between drift changes"))
                .setSaveConsumer(v -> config.driftIntervalMin = v)
                .build());

        general.addEntry(entryBuilder.startIntSlider(Component.literal("Drift Interval Max"), config.driftIntervalMax, 0, 200)
                .setDefaultValue(100)
                .setTooltip(Component.literal("Max ticks between drift changes"))
                .setSaveConsumer(v -> config.driftIntervalMax = v)
                .build());

        general.addEntry(entryBuilder.startFloatField(Component.literal("Pitch Drift Range"), config.pitchDriftRange)
                .setDefaultValue(0.6f)
                .setTooltip(Component.literal("Max pitch offset from drift"))
                .setSaveConsumer(v -> config.pitchDriftRange = v)
                .build());

        general.addEntry(entryBuilder.startFloatField(Component.literal("Yaw Drift Range"), config.yawDriftRange)
                .setDefaultValue(0.25f)
                .setTooltip(Component.literal("Max yaw offset from drift"))
                .setSaveConsumer(v -> config.yawDriftRange = v)
                .build());

        general.addEntry(entryBuilder.startFloatField(Component.literal("Drift Lerp Speed"), config.driftLerpSpeed)
                .setDefaultValue(0.02f)
                .setTooltip(Component.literal("How fast drift targets are approached"))
                .setSaveConsumer(v -> config.driftLerpSpeed = v)
                .build());

        general.addEntry(entryBuilder.startFloatField(Component.literal("Pitch Oscillation"), config.pitchOscillation)
                .setDefaultValue(0.12f)
                .setTooltip(Component.literal("Pitch sine oscillation amplitude"))
                .setSaveConsumer(v -> config.pitchOscillation = v)
                .build());

        general.addEntry(entryBuilder.startFloatField(Component.literal("Yaw Oscillation"), config.yawOscillation)
                .setDefaultValue(0.06f)
                .setTooltip(Component.literal("Yaw sine oscillation amplitude"))
                .setSaveConsumer(v -> config.yawOscillation = v)
                .build());

        general.addEntry(entryBuilder.startFloatField(Component.literal("Phase Speed"), config.rotationPhaseSpeed)
                .setDefaultValue(0.03f)
                .setTooltip(Component.literal("Oscillation phase advance speed"))
                .setSaveConsumer(v -> config.rotationPhaseSpeed = v)
                .build());

        general.addEntry(entryBuilder.startFloatField(Component.literal("Rotation Lerp"), config.rotationLerp)
                .setDefaultValue(0.2f)
                .setTooltip(Component.literal("Rotation smoothing factor"))
                .setSaveConsumer(v -> config.rotationLerp = v)
                .build());

        general.addEntry(entryBuilder.startIntSlider(Component.literal("Scan Forward Range"), config.scanForwardRange, 1, 10)
                .setDefaultValue(4)
                .setTooltip(Component.literal("Blocks to scan ahead for next target"))
                .setSaveConsumer(v -> config.scanForwardRange = v)
                .build());

        builder.setSavingRunnable(ConfigManager::save);

        return builder.build();
    }
}
