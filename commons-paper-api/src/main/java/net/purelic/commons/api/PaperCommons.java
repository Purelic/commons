package net.purelic.commons.api;

import net.purelic.commons.api.map.MapManager;
import net.purelic.commons.api.module.Modules;
import net.purelic.commons.api.player.Purelative;
import net.purelic.commons.api.task.PurelicTaskManager;
import net.purelic.api.discord.DiscordWebhook;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.NotNull;

import java.util.UUID;
import java.util.concurrent.atomic.AtomicReference;

public interface PaperCommons extends Plugin { //TODO: complain when other necessary plugins are missing

    DiscordWebhook getDiscordWebhook();

    Purelative getLicker(UUID uuid);

    Modules getModules();

    MapManager getMapManager();

    PurelicTaskManager getTaskManager();

    AtomicReference<PaperCommons> GLOBAL = new AtomicReference<>(null);

    static void set(final @NotNull PaperCommons paperCommons){
        try{
            get();
            throw new IllegalArgumentException("Commons has already been initialized");
        }catch (IllegalStateException e) {
            GLOBAL.set(paperCommons);
        }
    }

    static PaperCommons get(){ //TODO: IMPORTANT!!! Fix DEPENDENCIES IN OTHER PLUGINS SO COMMONS IS ALWAYS LOADED FIRST
        final PaperCommons paperCommons = GLOBAL.get();
        if(paperCommons == null){
            throw new IllegalStateException("Commons has not been initialized yet!");
        }
        return paperCommons;
    }
}
