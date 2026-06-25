package com.autostripmine.client;

import com.autostripmine.client.config.ConfigManager;
import com.autostripmine.client.config.ModConfig;
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
import net.minecraft.world.phys.Vec3;
import org.lwjgl.glfw.GLFW;

import java.util.Random;

public class StripMineController {
    public static boolean ACTIVE;
    public static final Random RANDOM = new Random();

    private final KeyMapping toggleKey;
    private boolean active;
    private Direction lockedDirection;
    private BlockPos lastTarget;
    private int pauseTicks;
    private int blocksMined;
    private boolean aimReady;

    private float currentPitch;
    private float currentYaw;
    private float pitchDrift;
    private float yawDrift;
    private float pitchDriftTarget;
    private float yawDriftTarget;
    private int driftTicker;
    private float rotationPhase;
    private boolean rotationInitialized;

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
                currentPitch = client.player.getXRot();
                currentYaw = client.player.getYRot();
                pitchDrift = 0f;
                yawDrift = 0f;
                pitchDriftTarget = 0f;
                yawDriftTarget = 0f;
                driftTicker = 40;
                rotationPhase = 0f;
                rotationInitialized = true;
                aimReady = false;
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

        updateRotation(player);

        if (isLavaInPath(level, player.blockPosition(), facing)) {
            active = false;
            ACTIVE = false;
            lastTarget = null;
            player.sendSystemMessage(Component.literal("§4[AutoStripMine] Lava detected! Mode disabled."));
            return;
        }

        if (lastTarget != null && level.getBlockState(lastTarget).isAir()) {
            blocksMined++;
            ModConfig c = ConfigManager.getConfig();
            if (RANDOM.nextFloat() < c.shortPauseChance) {
                pauseTicks = c.shortPauseMin + RANDOM.nextInt(c.shortPauseMax - c.shortPauseMin + 1);
            }
            if (blocksMined % (c.blocksPerLongPauseMin + RANDOM.nextInt(c.blocksPerLongPauseMax - c.blocksPerLongPauseMin + 1)) == 0) {
                pauseTicks = c.longPauseMin + RANDOM.nextInt(c.longPauseMax - c.longPauseMin + 1);
            }
            lastTarget = null;
        }

        if (pauseTicks > 0) {
            pauseTicks--;
            lastTarget = null;
            return;
        }

        double reach = ConfigManager.getConfig().reachDistance;
        Vec3 eyePos = player.getEyePosition(1.0f);
        Vec3 lookVec = player.getLookAngle();
        Vec3 to = eyePos.add(lookVec.x * reach, lookVec.y * reach, lookVec.z * reach);
        ClipContext ctx = new ClipContext(eyePos, to, ClipContext.Block.COLLIDER, ClipContext.Fluid.NONE, player);
        BlockHitResult hit = level.clip(ctx);

        int baseY = player.blockPosition().getY();
        BlockPos target = null;
        Direction targetSide = facing.getOpposite();

        if (hit.getType() == HitResult.Type.BLOCK) {
            BlockPos hitPos = hit.getBlockPos();
            BlockPos feetTarget = new BlockPos(hitPos.getX(), baseY, hitPos.getZ());
            BlockPos headTarget = feetTarget.above(1);

            if (hitPos.equals(headTarget)) {
                aimReady = true;
                target = headTarget;
                targetSide = hit.getDirection();
            } else if (hitPos.equals(feetTarget) && (aimReady || level.getBlockState(headTarget).isAir())) {
                aimReady = true;
                target = feetTarget;
                targetSide = hit.getDirection();
            }
        }

        if (target == null && aimReady) {
            target = scanForwardForNextBlock(level, player.blockPosition(), facing);
        }

        if (target != null) {
            if (!target.equals(lastTarget)) {
                client.gameMode.startDestroyBlock(target, targetSide);
                lastTarget = target;
            }
            client.gameMode.continueDestroyBlock(target, targetSide);
        } else {
            lastTarget = null;
        }
    }

    private void updateRotation(LocalPlayer player) {
        if (!rotationInitialized) {
            currentPitch = player.getXRot();
            currentYaw = player.getYRot();
            pitchDrift = 0f;
            yawDrift = 0f;
            pitchDriftTarget = 0f;
            yawDriftTarget = 0f;
            driftTicker = 40;
            rotationPhase = RANDOM.nextFloat() * 6.28f;
            rotationInitialized = true;
        }

        driftTicker--;
        if (driftTicker <= 0) {
            ModConfig c = ConfigManager.getConfig();
            driftTicker = c.driftIntervalMin + RANDOM.nextInt(c.driftIntervalMax - c.driftIntervalMin + 1);
            pitchDriftTarget = (RANDOM.nextFloat() - 0.5f) * c.pitchDriftRange;
            yawDriftTarget = (RANDOM.nextFloat() - 0.5f) * c.yawDriftRange;
        }
        ModConfig c = ConfigManager.getConfig();
        pitchDrift += (pitchDriftTarget - pitchDrift) * c.driftLerpSpeed;
        yawDrift += (yawDriftTarget - yawDrift) * c.driftLerpSpeed;

        rotationPhase += c.rotationPhaseSpeed;
        float oscPitch = (float) Math.sin(rotationPhase) * c.pitchOscillation;
        float oscYaw = (float) Math.sin(rotationPhase * 0.7f + 1.0f) * c.yawOscillation;

        float baseYaw = lockedDirection != null ? Direction.getYRot(lockedDirection) : player.getYRot();
        float targetPitch = c.stripPitch + pitchDrift + oscPitch;
        float targetYaw = baseYaw + yawDrift + oscYaw;

        float lerp = c.rotationLerp;
        currentPitch += (targetPitch - currentPitch) * lerp;
        currentYaw += (targetYaw - currentYaw) * lerp;

        player.setXRot(currentPitch);
        player.setYRot(currentYaw);
    }

    private BlockPos scanForwardForNextBlock(Level level, BlockPos origin, Direction facing) {
        int range = ConfigManager.getConfig().scanForwardRange;
        for (int d = 1; d <= range; d++) {
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
        Direction left = facing.getClockWise();
        Direction right = facing.getCounterClockWise();

        int scanDistance = ConfigManager.getConfig().scanDistance;
        for (int d = 1; d <= scanDistance; d++) {
            BlockPos forward = origin.relative(facing, d);

            BlockPos feet = forward;
            BlockPos head = forward.above(1);
            BlockPos above = forward.above(2);
            BlockPos below = forward.below(1);

            BlockPos leftFeet = forward.relative(left, 1);
            BlockPos leftHead = forward.relative(left, 1).above(1);
            BlockPos rightFeet = forward.relative(right, 1);
            BlockPos rightHead = forward.relative(right, 1).above(1);

            if (isLava(level, head) || isLava(level, feet) || isLava(level, above) ||
                isLava(level, below) ||
                isLava(level, leftFeet) || isLava(level, leftHead) ||
                isLava(level, rightFeet) || isLava(level, rightHead)) {
                return true;
            }
        }
        return false;
    }

    private boolean isLava(Level level, BlockPos pos) {
        FluidState fluid = level.getFluidState(pos);
        return fluid.is(FluidTags.LAVA);
    }
}
