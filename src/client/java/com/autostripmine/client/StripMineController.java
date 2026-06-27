package com.autostripmine.client;

import com.autostripmine.client.config.ConfigManager;
import com.autostripmine.client.config.ModConfig;
import com.mojang.blaze3d.platform.InputConstants;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;

public class StripMineController {
    public static boolean ACTIVE = false;

    private final Minecraft mc = Minecraft.getInstance();
    
    private long ctrlShiftPressStart = 0;
    private int tickCounter = 0;
    private boolean lavaDetected = false;

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
            scanForLava();
        }
    }

    private void handleToggle() {
        ModConfig config = ConfigManager.getConfig();
        String toggleKey = config.toggleKey;
        
        boolean requiredKeysDown = isToggleKeyComboDown(toggleKey);
        
        if (requiredKeysDown) {
            if (ctrlShiftPressStart == 0) {
                ctrlShiftPressStart = System.currentTimeMillis();
            } else if (System.currentTimeMillis() - ctrlShiftPressStart >= config.holdDurationMs) {
                toggleAutoMine();
                ctrlShiftPressStart = 0;
            }
        } else {
            ctrlShiftPressStart = 0;
        }
    }

    private boolean isToggleKeyComboDown(String keyCombo) {
        String[] parts = keyCombo.split("\\+");
        boolean allDown = true;
        
        for (String part : parts) {
            String key = part.trim();
            int keyCode = getKeyCode(key);
            if (keyCode == -1 || !InputConstants.isKeyDown(mc.getWindow(), keyCode)) {
                allDown = false;
                break;
            }
        }
        
        return allDown;
    }

    private int getKeyCode(String key) {
        return switch (key) {
            case "CTRL" -> InputConstants.KEY_LCONTROL;
            case "SHIFT" -> InputConstants.KEY_LSHIFT;
            case "ALT" -> InputConstants.KEY_LALT;
            case "G" -> InputConstants.KEY_G;
            case "F" -> InputConstants.KEY_F;
            case "R" -> InputConstants.KEY_R;
            case "E" -> InputConstants.KEY_E;
            case "Q" -> InputConstants.KEY_Q;
            case "SPACE" -> InputConstants.KEY_SPACE;
            default -> -1;
        };
    }

    private void toggleAutoMine() {
        ACTIVE = !ACTIVE;
        lavaDetected = false;
        
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

    private void scanForLava() {
        ModConfig config = ConfigManager.getConfig();
        tickCounter++;
        if (tickCounter < config.lavaScanInterval) return;
        tickCounter = 0;

        LocalPlayer player = mc.player;
        Level level = mc.level;
        if (player == null || level == null) return;

        BlockPos playerPos = player.blockPosition();
        double yaw = Math.toRadians(player.getYRot());

        // Calculate forward direction vector
        double forwardX = -Math.sin(yaw);
        double forwardZ = Math.cos(yaw);

        // Check multiple blocks ahead in the mining path (2x1 area for each position)
        int scanDistance = config.scanDistance;
        for (int i = 0; i <= scanDistance; i++) {
            int offsetX = (int)Math.round(forwardX * i);
            int offsetZ = (int)Math.round(forwardZ * i);
            
            BlockPos[] minePositions = new BlockPos[]{
                new BlockPos(playerPos.getX() + offsetX, playerPos.getY(), playerPos.getZ() + offsetZ),  // feet level
                new BlockPos(playerPos.getX() + offsetX, playerPos.getY() + 1, playerPos.getZ() + offsetZ)  // head level
            };

            // Check all 6 sides + inside for each mining position (2x1 area)
            for (BlockPos minePos : minePositions) {
                // Check the block itself
                if (isLava(level.getBlockState(minePos))) {
                    onLavaDetected();
                    return;
                }
                // Check all 6 adjacent blocks
                for (var direction : net.minecraft.core.Direction.values()) {
                    BlockPos adjacent = minePos.relative(direction);
                    if (isLava(level.getBlockState(adjacent))) {
                        onLavaDetected();
                        return;
                    }
                }
            }
        }
    }

    private boolean isLava(BlockState state) {
        return state.is(Blocks.LAVA);
    }

    private void onLavaDetected() {
        if (!lavaDetected) {
            lavaDetected = true;
            ACTIVE = false;
            releaseKeys();
            mc.player.sendSystemMessage(Component.literal("§c[AutoStripMine] LAVA DETECTED! Auto-mining stopped."));
        }
    }
}