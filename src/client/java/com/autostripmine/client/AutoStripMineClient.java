package com.autostripmine.client;

import net.fabricmc.api.ClientModInitializer;

public class AutoStripMineClient implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        new StripMineController().register();
    }
}
