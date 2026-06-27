package com.autostripmine.client;

import net.fabricmc.api.ClientModInitializer;

public class AutoStripMineClient implements ClientModInitializer {
    private static StripMineController controller;

    @Override
    public void onInitializeClient() {
        controller = new StripMineController();
        controller.register();
    }

    public static StripMineController getController() {
        return controller;
    }
}