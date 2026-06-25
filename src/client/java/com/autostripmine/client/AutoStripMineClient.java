package com.autostripmine.client;

import com.autostripmine.client.config.ConfigManager;
import net.fabricmc.api.ClientModInitializer;

public class AutoStripMineClient implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        ConfigManager.init();
        new StripMineController().register();
    }
}
