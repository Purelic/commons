package net.purelic.commons.paper.listeners;

import net.purelic.commons.paper.Commons;
import net.purelic.commons.paper.commands.op.BanCommand;
import net.purelic.commons.paper.profile.Profile;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerPreLoginEvent;

import java.util.UUID;

public class PlayerPreLogin implements Listener {

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onPlayerPreLogin(AsyncPlayerPreLoginEvent event) {
        if (!Commons.isReady()) {
            event.disallow(
                AsyncPlayerPreLoginEvent.Result.KICK_OTHER,
                ChatColor.RED + "The server is still starting up! Trying joining again in a few seconds.");
        } else {
            UUID uuid = event.getUniqueId();

            if (BanCommand.BANNED.containsValue(uuid)) {
                event.disallow(
                    AsyncPlayerPreLoginEvent.Result.KICK_OTHER,
                    "You've been banned on this server!");
                return;
            }

            Profile profile = Commons.getProfile(uuid, event.getName(), true);

            if (profile.isStaff()) {
                if (Bukkit.hasWhitelist()) Bukkit.getOfflinePlayer(uuid).setWhitelisted(true);
                event.allow();
            }
        }
    }

}
