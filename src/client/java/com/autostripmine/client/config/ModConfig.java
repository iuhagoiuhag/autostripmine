package com.autostripmine.client.config;

public class ModConfig {
    public int scanDistance = clamp(5, 1, 20);
    public int holdDurationMs = clamp(500, 100, 5000);
    public int fluidScanInterval = clamp(10, 1, 100);
    public String toggleKey = "CTRL+SHIFT";
    public boolean autoEatEnabled = true;
    public int hungerThreshold = clamp(12, 1, 20);

    public void validate() {
        scanDistance = clamp(scanDistance, 1, 20);
        holdDurationMs = clamp(holdDurationMs, 100, 5000);
        fluidScanInterval = clamp(fluidScanInterval, 1, 100);
        hungerThreshold = clamp(hungerThreshold, 1, 20);
        if (toggleKey == null || toggleKey.isBlank()) {
            toggleKey = "CTRL+SHIFT";
        }
    }

    private static int clamp(int value, int min, int max) {
        return Math.max(min, Math.min(max, value));
    }
}
