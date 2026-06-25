package com.autostripmine.client.config;

public class ModConfig {
    public int scanDistance = 5;
    public float stripPitch = 35.0f;
    public double reachDistance = 4.5;

    public int shortPauseMin = 4;
    public int shortPauseMax = 8;
    public float shortPauseChance = 0.08f;
    public int longPauseMin = 15;
    public int longPauseMax = 25;
    public int blocksPerLongPauseMin = 35;
    public int blocksPerLongPauseMax = 50;

    public int driftIntervalMin = 40;
    public int driftIntervalMax = 100;
    public float pitchDriftRange = 0.6f;
    public float yawDriftRange = 0.25f;
    public float driftLerpSpeed = 0.02f;
    public float pitchOscillation = 0.12f;
    public float yawOscillation = 0.06f;
    public float rotationPhaseSpeed = 0.03f;
    public float rotationLerp = 0.2f;

    public int scanForwardRange = 4;
}
