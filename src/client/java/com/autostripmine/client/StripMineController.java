package com.autostripmine.client;

import com.mojang.blaze3d.platform.InputConstants;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keymapping.v1.KeyMappingHelper;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.Component;
import net.minecraft.tags.FluidTags;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec2;
import net.minecraft.world.phys.Vec3;
import org.lwjgl.glfw.GLFW;

public class StripMineController {
    public static boolean ACTIVE;

    private static final int SCAN_DISTANCE = 5;
    private static final float STRIP_PITCH = 35.0f;

    private final KeyMapping toggleKey;
    private boolean active;
    private Direction lockedDirection;
    private BlockPos lastTarget;

    public StripMineController() {
        toggleKey = KeyMappingHelper.registerKeyMapping(
                new KeyMapping(
                        "key.autostripmine.toggle",
                        InputConstants.Type.KEYSYM,
                        GLFW.GLFW_KEY_G,
                        KeyMapping.Category.MISC
                )
        );
    }

    public void register() {
        ClientTickEvents.START_CLIENT_TICK.register(this::onTick);
    }

    private void onTick(Minecraft client) {
        if (client.player == null || client.level == null) return;

        while (toggleKey.consumeClick()) {
            active = !active;
            ACTIVE = active;
            if (active) {
                lockedDirection = client.player.getDirection();
                client.player.sendSystemMessage(Component.literal("§a[AutoStripMine] Activated"));
            } else {
                client.player.sendSystemMessage(Component.literal("§c[AutoStripMine] Deactivated"));
                lastTarget = null;
            }
        }

        if (!active) return;

        LocalPlayer player = client.player;
        Level level = client.level;
        Direction facing = lockedDirection;

        player.setXRot(STRIP_PITCH);
        player.setYRot(Direction.getYRot(facing));

        if (isLavaInPath(level, player.blockPosition(), facing)) {
            active = false;
            ACTIVE = false;
            player.sendSystemMessage(Component.literal("§4[AutoStripMine] Lava detected! Mode disabled."));
            lastTarget = null;
            return;
        }

        Vec3 eyePos = player.getEyePosition(1.0f);
        Vec3 lookVec = player.getLookAngle();
        Vec3 to = eyePos.add(lookVec.x * 4.5, lookVec.y * 4.5, lookVec.z * 4.5);
        ClipContext ctx = new ClipContext(eyePos, to, ClipContext.Block.COLLIDER, ClipContext.Fluid.NONE, player);
        BlockHitResult hit = level.clip(ctx);

        BlockPos hitPos;
        Direction hitSide;
        int baseY = player.blockPosition().getY();
        if (hit.getType() == HitResult.Type.BLOCK) {
            hitPos = hit.getBlockPos();
            hitSide = hit.getDirection();
        } else {
            hitPos = player.blockPosition().relative(facing, 1);
            hitSide = facing.getOpposite();
        }

        BlockPos feetTarget = new BlockPos(hitPos.getX(), baseY, hitPos.getZ());
        BlockPos headTarget = feetTarget.above(1);

        if (!level.getBlockState(headTarget).isAir()) {
            updateBlockBreak(client, headTarget, hitSide);
        } else if (!level.getBlockState(feetTarget).isAir()) {
            updateBlockBreak(client, feetTarget, hitSide);
        } else {
            BlockPos next = scanForwardForNextBlock(level, player.blockPosition(), facing);
            if (next != null) {
                updateBlockBreak(client, next, facing.getOpposite());
            } else {
                lastTarget = null;
            }
        }
    }

    private void updateBlockBreak(Minecraft client, BlockPos pos, Direction side) {
        if (!pos.equals(lastTarget)) {
            client.gameMode.startDestroyBlock(pos, side);
            lastTarget = pos;
        }
        client.gameMode.continueDestroyBlock(pos, side);
    }

    private BlockPos scanForwardForNextBlock(Level level, BlockPos origin, Direction facing) {
        for (int d = 1; d <= 4; d++) {
            BlockPos head = origin.relative(facing, d).above(1);
            BlockPos feet = origin.relative(facing, d);
            if (!level.getBlockState(head).isAir()) {
                return head;
            }
            if (!level.getBlockState(feet).isAir()) {
                return feet;
            }
        }
        return null;
    }

    private boolean isLavaInPath(Level level, BlockPos origin, Direction facing) {
        for (int d = 1; d <= SCAN_DISTANCE; d++) {
            BlockPos head = origin.relative(facing, d).above(1);
            BlockPos feet = origin.relative(facing, d);
            BlockPos above = origin.relative(facing, d).above(2);
            if (isLava(level, head) || isLava(level, feet) || isLava(level, above)) {
                return true;
            }
        }
        return false;
    }

    private boolean isLava(Level level, BlockPos pos) {
        FluidState fluid = level.getFluidState(pos);
        return !fluid.isEmpty() && fluid.getType().is(FluidTags.LAVA);
    }
}
