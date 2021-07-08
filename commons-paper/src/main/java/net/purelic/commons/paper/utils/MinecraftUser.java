package net.purelic.commons.paper.utils;

import com.google.gson.JsonObject;
import com.mojang.authlib.properties.Property;

import java.io.IOException;
import java.util.UUID;

public class MinecraftUser {

    private final String name;
    private final UUID uuid;
    private final Property skin;

    public MinecraftUser(UUID uuid) throws IOException {
        this(uuid.toString());
    }

    public MinecraftUser(String identifier) throws IOException {
        JsonObject data = Fetcher.getMinecraftUserObject(identifier);

        this.name = data.get("username").getAsString();
        this.uuid = UUID.fromString(data.get("uuid").getAsString());

        JsonObject textures = data
            .get("textures").getAsJsonObject()
            .get("raw").getAsJsonObject();

        String texture = textures.get("value").getAsString();
        String signature = textures.get("signature").getAsString();

        this.skin = new Property("textures", texture, signature);

        Fetcher.cacheMinecraftUser(this);
    }

    public String getName() {
        return this.name;
    }

    public UUID getId() {
        return this.uuid;
    }

    public Property getSkin() {
        return this.skin;
    }

}
