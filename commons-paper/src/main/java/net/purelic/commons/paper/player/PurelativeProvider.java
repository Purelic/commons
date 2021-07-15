package net.purelic.commons.paper.player;

import com.google.common.base.Preconditions;
import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import net.kyori.adventure.platform.AudienceProvider;
import net.purelic.commons.paper.PaperCommonsImpl;
import net.purelic.commons.paper.api.player.Purelative;
import net.purelic.commons.purelic.api.profile.PlayerDatabaseProfile;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.function.Function;

public class PurelativeProvider implements Listener {

    private final Function<UUID, PlayerDatabaseProfile> databaseProfileFunction;
    private final AudienceProvider audienceProvider;
    private final Map<UUID, Purelative> purelativeLoadingCache;

    public PurelativeProvider(Function<UUID, PlayerDatabaseProfile> databaseProfileFunction, AudienceProvider audienceProvider){
        this.databaseProfileFunction = databaseProfileFunction;
        this.audienceProvider = audienceProvider;
        this.purelativeLoadingCache = new HashMap<>();
    }

    public Purelative get(UUID uuid){
        final @Nullable Purelative purelative = this.purelativeLoadingCache.get(uuid);
        if(purelative != null) return purelative;
        return this.purelativeLoadingCache.put(uuid, new ActualPurelative(this.databaseProfileFunction.apply(uuid), null, this.audienceProvider));
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event){
        this.purelativeLoadingCache.remove(event.getPlayer().getUniqueId());
    }

    @EventHandler(ignoreCancelled = true)
    public void onPlayerJoin(PlayerJoinEvent event){
        final UUID id = event.getPlayer().getUniqueId();
        this.purelativeLoadingCache.remove(id);
        this.purelativeLoadingCache.put(id, new ActualPurelative(this.databaseProfileFunction.apply(id), event.getPlayer(), this.audienceProvider));
    }
}
