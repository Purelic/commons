package net.purelic.commons.listeners;

import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import net.purelic.commons.Commons;
import net.purelic.commons.events.OpStatusChangeEvent;
import net.purelic.commons.profile.Profile;
import net.purelic.commons.runnables.IdleTimer;
import net.purelic.commons.utils.*;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class PlayerJoin implements Listener {

    @EventHandler(priority = EventPriority.MONITOR)
    public void onPlayerJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();

        event.setJoinMessage(null);

        TaskUtils.cancelIfRunning(IdleTimer.getInstance());
        DatabaseUtils.updatePlayerCount(Bukkit.getOnlinePlayers().size());
        PermissionUtils.setPermissions(player);

        VersionUtils.Protocol protocol = VersionUtils.getProtocol(player);

        Bukkit.broadcast(
            Fetcher.getFancyName(player),
            new TextComponent(ChatColor.GRAY + " joined "),
            new ComponentBuilder("(" + protocol.getLabel() + ")")
                .color(ChatColor.GRAY)
                .event(new HoverEvent(HoverEvent.Action.SHOW_TEXT,
                    new ComponentBuilder(protocol.getFullLabel() + " (" + protocol.value() + ")")
                        .create()))
                .create()[0]);

        if (Commons.getProfile(player).isStaff() && NickUtils.getNickedPlayers().size() > 0) {
            Bukkit.dispatchCommand(player, "nicks");
        }
    }

    @EventHandler(priority = EventPriority.LOW)
    public void onNickedPlayerJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        Profile profile = Commons.getProfile(player);

        // Force unnick non-donors on join if they're nicked
        if (NickUtils.isNicked(player) && !profile.isStaff() && !profile.isDonator()) {
            PlayerUtils.performCommand(player, "unnick");
            return;
        }

        // Disguise the player if nicked
        if (!NickUtils.disguisePlayer(player)) {
            Bukkit.getOnlinePlayers().forEach(online -> {
                online.hidePlayer(player);
                online.showPlayer(player);
            });
        }
    }

    private boolean firstJoin = true;

    @EventHandler(priority = EventPriority.LOWEST)
    public void onOwnerJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();

        // if the server is private and we don't have an owner id (e.g. localhost),
        // set the server id/owner id to the first player that joins
        if (this.firstJoin && ServerUtils.isPrivate() && !Commons.hasOwner()) {
            ServerUtils.setId(player.getUniqueId().toString());
            ServerUtils.setName(NickUtils.getRealName(player));
        }

        if (ServerUtils.isPrivate() && Commons.isOwner(player) && !CommandUtils.isOp(player)) {
            Commons.callEvent(new OpStatusChangeEvent(player, null, true, true));
        }

        this.firstJoin = false;
    }

}
