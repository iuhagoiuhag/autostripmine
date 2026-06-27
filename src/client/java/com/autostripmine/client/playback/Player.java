package com.autostripmine.client.playback;

import net.minecraft.client.Minecraft;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.player.LocalPlayer;

public class Player {
    private static Recording currentRecording = null;
    private static int frameOffset = 0;
    private static boolean active = false;
    private static final Minecraft mc = Minecraft.getInstance();

    public static void loadRecording(Recording recording) {
        currentRecording = recording;
        frameOffset = 0;
        System.out.println("[AutoStripMine] Loaded recording: " + recording.frameCount() + " frames, " + recording.tickDuration() + " ticks");
    }

    public static void toggle() {
        active = !active;
        if (active && currentRecording != null) {
            frameOffset = (int) (mc.level.getGameTime() % currentRecording.tickDuration());
        }
    }

    public static void onTick() {
        if (!active || currentRecording == null) return;
        
        LocalPlayer player = mc.player;
        if (player == null) return;

        long gameTime = mc.level.getGameTime();
        int frameIndex = (int) ((gameTime + frameOffset) % currentRecording.tickDuration());
        
        Frame frame = currentRecording.getFrame(frameIndex);
        if (frame == null) return;

        KeyMapping forwardKey = mc.options.keyUp;
        KeyMapping attackKey = mc.options.keyAttack;

        forwardKey.setDown(frame.forward());
        attackKey.setDown(frame.attack());

        player.setYRot(frame.yaw());
        player.setXRot(frame.pitch());
    }

    public static Recording getCurrentRecording() {
        return currentRecording;
    }

    public static boolean isActive() {
        return active;
    }
}