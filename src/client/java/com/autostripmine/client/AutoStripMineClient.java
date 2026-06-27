package com.autostripmine.client;

import com.autostripmine.client.config.ConfigManager;
import net.fabricmc.api.ClientModInitializer;

public class AutoStripMineClient implements ClientModInitializer {
    private static StripMineController controller;

    @Override
    public void onInitializeClient() {
        ConfigManager.init();
        controller = new StripMineController();
        controller.register();
    }

    public static StripMineController getController() {
        return controller;
    }
}