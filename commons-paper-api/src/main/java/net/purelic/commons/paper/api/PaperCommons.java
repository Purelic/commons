package net.purelic.commons.paper.api;

import net.purelic.commons.paper.api.chat.NaughtyMessageManager;
import net.purelic.commons.paper.api.command.CustomCommand;
import net.purelic.commons.paper.api.module.Modules;
import net.purelic.commons.paper.api.player.Purelative;
import net.purelic.commons.paper.api.task.PurelicTaskManager;
import net.purelic.commons.purelic.api.discord.DiscordWebhook;
import org.bukkit.event.Listener;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.NotNull;

import java.util.UUID;
import java.util.concurrent.atomic.AtomicReference;

public interface PaperCommons extends Plugin { //TODO: complain when other necessary plugins are missing

    DiscordWebhook getDiscordWebhook();

    Purelative getLicker(UUID uuid);

    Modules getModules();

    //MapManager getMapManager();

    PurelicTaskManager getTaskManager();

    NaughtyMessageManager getMessageControl();

    void register(Listener listener);

    void unRegister(Listener listener);

    void register(CustomCommand command);

    void unRegister(CustomCommand command);

    void sendSpringMessage(String subchannel, String... data); //TODO: figure out how to implement this in a friendl manner

    AtomicReference<PaperCommons> GLOBAL = new AtomicReference<>(null);

    static void set(final @NotNull PaperCommons paperCommons){
        try{
            get();
            throw new IllegalArgumentException("Commons has already been initialized");
        }catch (IllegalStateException e) {
            GLOBAL.set(paperCommons);
        }
    }

    static @NotNull PaperCommons get(){ //TODO: IMPORTANT!!! Fix DEPENDENCIES IN OTHER PLUGINS SO COMMONS IS ALWAYS LOADED FIRST
        final PaperCommons paperCommons = GLOBAL.get();
        if(paperCommons == null){
            throw new IllegalStateException("Commons has not been initialized yet!");
        }
        return paperCommons;
    }
}
