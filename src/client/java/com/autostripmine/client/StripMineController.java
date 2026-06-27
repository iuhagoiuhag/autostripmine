package com.autostripmine.client;

import com.mojang.blaze3d.platform.InputConstants;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.network.chat.Component;

public class StripMineController {
    public static boolean ACTIVE = false;

    private final Minecraft mc = Minecraft.getInstance();
    
    private long ctrlShiftPressStart = 0;
    private static final long HOLD_DURATION_MS = 500;

    public StripMineController() {
    }

    public void register() {
        ClientTickEvents.END_CLIENT_TICK.register(this::onTick);
    }

    private void onTick(Minecraft client) {
        if (client.player == null || client.level == null) return;

        handleToggle();
        
        if (ACTIVE) {
            holdForwardAndMine();
        }
    }

    private void handleToggle() {
        boolean ctrlDown = InputConstants.isKeyDown(mc.getWindow(), InputConstants.KEY_LCONTROL) || 
                           InputConstants.isKeyDown(mc.getWindow(), InputConstants.KEY_RCONTROL);
        boolean shiftDown = InputConstants.isKeyDown(mc.getWindow(), InputConstants.KEY_LSHIFT) || 
                            InputConstants.isKeyDown(mc.getWindow(), InputConstants.KEY_RSHIFT);
        
        boolean bothDown = ctrlDown && shiftDown;
        
        if (bothDown) {
            if (ctrlShiftPressStart == 0) {
                ctrlShiftPressStart = System.currentTimeMillis();
            } else if (System.currentTimeMillis() - ctrlShiftPressStart >= HOLD_DURATION_MS) {
                toggleAutoMine();
                ctrlShiftPressStart = 0;
            }
        } else {
            ctrlShiftPressStart = 0;
        }
    }

    private void toggleAutoMine() {
        ACTIVE = !ACTIVE;
        
        if (ACTIVE) {
            mc.player.sendSystemMessage(Component.literal("§a[AutoStripMine] Auto-mining started"));
        } else {
            releaseKeys();
            mc.player.sendSystemMessage(Component.literal("§c[AutoStripMine] Auto-mining stopped"));
        }
    }

    private void holdForwardAndMine() {
        LocalPlayer player = mc.player;
        if (player == null) return;

        KeyMapping forwardKey = mc.options.keyUp;
        KeyMapping attackKey = mc.options.keyAttack;

        forwardKey.setDown(true);
        attackKey.setDown(true);
    }

    private void releaseKeys() {
        KeyMapping forwardKey = mc.options.keyUp;
        KeyMapping attackKey = mc.options.keyAttack;

        forwardKey.setDown(false);
        attackKey.setDown(false);
    }
}