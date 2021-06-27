package net.purelic.commons.listeners;

import net.md_5.bungee.api.chat.TextComponent;
import net.purelic.commons.Commons;
import net.purelic.commons.runnables.IdleTimer;
import net.purelic.commons.utils.DatabaseUtils;
import net.purelic.commons.utils.Fetcher;
import net.purelic.commons.utils.PermissionUtils;
import net.purelic.commons.utils.TaskUtils;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class PlayerQuit implements Listener {

    private static final Map<UUID, TextComponent> NAME_CACHE = new HashMap<>();

    @EventHandler(priority = EventPriority.LOW)
    public void onPlayerQuit(PlayerQuitEvent event) {
        Player player = event.getPlayer();
        event.setQuitMessage(null);

        NAME_CACHE.put(player.getUniqueId(), Fetcher.getFancyName(player));

        DatabaseUtils.updateLastSeen(player);
        DatabaseUtils.updatePlayerCount(Bukkit.getOnlinePlayers().size() - 1);
        PermissionUtils.removeAllPermissions(player);
        Commons.removeProfile(player);

        if (Bukkit.getOnlinePlayers().size() == 1 && Commons.idleTimer > 0) {
            TaskUtils.cancelIfRunning(IdleTimer.getInstance());
            TaskUtils.runLater(new IdleTimer(), Commons.idleTimer);
        }
    }

    public static boolean hasCachedName(UUID uuid) {
        return NAME_CACHE.containsKey(uuid);
    }

    public static TextComponent getCachedName(UUID uuid) {
        return NAME_CACHE.get(uuid);
    }

}
