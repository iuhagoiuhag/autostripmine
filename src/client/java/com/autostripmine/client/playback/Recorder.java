package com.autostripmine.client.playback;

import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.minecraft.client.Minecraft;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.player.LocalPlayer;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

public class Recorder {
    private static final int RECORD_TICKS = 200; // 10 seconds at 20 TPS
    
    private boolean recording = false;
    private int currentTick = 0;
    private final List<Frame> frames = new ArrayList<>();
    private Consumer<Recording> onComplete;
    private final Minecraft mc = Minecraft.getInstance();

    public void startRecording(Consumer<Recording> onComplete) {
        if (recording) return;
        this.onComplete = onComplete;
        this.frames.clear();
        this.currentTick = 0;
        this.recording = true;
        System.out.println("[AutoStripMine] Recording started (10 seconds)...");
    }

    public void stopRecording() {
        if (!recording) return;
        recording = false;
        System.out.println("[AutoStripMine] Recording stopped early at tick " + currentTick);
    }

    public void onTick() {
        if (!recording) return;

        LocalPlayer player = mc.player;
        if (player == null) return;

        KeyMapping forwardKey = mc.options.keyUp;
        KeyMapping attackKey = mc.options.keyAttack;

        boolean forward = forwardKey.isDown();
        boolean attack = attackKey.isDown();
        float yaw = player.getYRot();
        float pitch = player.getXRot();

        frames.add(new Frame(currentTick, forward, attack, yaw, pitch));
        currentTick++;

        if (currentTick >= RECORD_TICKS) {
            finishRecording();
        }
    }

    private void finishRecording() {
        recording = false;
        
        Recording recording = new Recording(
            RECORD_TICKS,
            List.copyOf(frames),
            "26.2"
        );

        System.out.println("[AutoStripMine] Recording complete: " + frames.size() + " frames");
        
        if (onComplete != null) {
            onComplete.accept(recording);
        }
    }

    public boolean isRecording() {
        return recording;
    }

    public int getCurrentTick() {
        return currentTick;
    }

    public static void registerTickListener(Recorder recorder) {
        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            recorder.onTick();
        });
    }
}