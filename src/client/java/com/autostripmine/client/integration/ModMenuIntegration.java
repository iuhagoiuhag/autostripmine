package com.autostripmine.client.integration;

import com.autostripmine.client.config.AutoStripMineConfigScreen;
import com.terraformersmc.modmenu.api.ConfigScreenFactory;
import com.terraformersmc.modmenu.api.ModMenuApi;

public class ModMenuIntegration implements ModMenuApi {
    @Override
    public ConfigScreenFactory<?> getModConfigScreenFactory() {
        return screen -> AutoStripMineConfigScreen.create(screen);
    }
}
