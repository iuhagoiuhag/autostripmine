package com.autostripmine.client.playback;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.annotations.SerializedName;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

public record Recording(
    @SerializedName("tickDuration") int tickDuration,
    @SerializedName("frames") List<Frame> frames,
    @SerializedName("minecraftVersion") String minecraftVersion
) {
    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();

    public static Recording load(Path path) throws IOException {
        String json = Files.readString(path);
        return GSON.fromJson(json, Recording.class);
    }

    public void save(Path path) throws IOException {
        Files.createDirectories(path.getParent());
        String json = GSON.toJson(this);
        Files.writeString(path, json);
    }

    public Frame getFrame(int index) {
        if (frames.isEmpty()) return null;
        return frames.get(index % frames.size());
    }

    public int frameCount() {
        return frames.size();
    }
}