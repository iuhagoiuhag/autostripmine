package com.autostripmine.client.playback;

public record Frame(
    int tick,
    boolean forward,
    boolean attack,
    float yaw,
    float pitch
) {}