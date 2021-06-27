package net.purelic.commons.api;

import net.purelic.commons.api.discord.DiscordWebhook;
import net.purelic.commons.api.event.CommonsReadyEvent;
import net.purelic.commons.api.map.MapManager;
import net.purelic.commons.api.module.Modules;
import net.purelic.commons.api.player.Purelative;
import net.purelic.commons.api.task.PurelicTaskManager;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.NotNull;

import java.util.UUID;
import java.util.concurrent.atomic.AtomicReference;

public interface Commons extends Plugin { //TODO: complain when other necessary plugins are missing

    DiscordWebhook getDiscordLink();

    Purelative getLicker(UUID uuid);

    Modules getModules();

    MapManager getMapManager();

    PurelicTaskManager getTaskManager();

    AtomicReference<Commons> GLOBAL = new AtomicReference<>(null);

    static void set(final @NotNull Commons commons){
        try{
            get();
            throw new IllegalArgumentException("Commons has already been initialized");
        }catch (IllegalStateException e) {
            GLOBAL.set(commons);
        }
    }

    static Commons get(){ //TODO: IMPORTANT!!! Fix DEPENDENCIES IN OTHER PLUGINS SO COMMONS IS ALWAYS LOADED FIRST
        final Commons commons = GLOBAL.get();
        if(commons == null){
            throw new IllegalStateException("Commons has not been initialized yet!");
        }
        return commons;
    }
}
