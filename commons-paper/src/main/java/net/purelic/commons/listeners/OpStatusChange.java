package net.purelic.commons.listeners;

import net.md_5.bungee.api.chat.TextComponent;
import net.purelic.commons.Commons;
import net.purelic.commons.events.OpStatusChangeEvent;
import net.purelic.commons.events.PlayerRankChangeEvent;
import net.purelic.commons.utils.CommandUtils;
import net.purelic.commons.utils.DatabaseUtils;
import net.purelic.commons.utils.Fetcher;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;

public class OpStatusChange implements Listener {

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onOpStatusChange(OpStatusChangeEvent event) {
        Player player = event.getPlayer();
        Player changedBy = event.getChangedBy();
        boolean op = event.isOp();

        if (event.isOwnerJoin()) {
            DatabaseUtils.unlockServer();
            player.setWhitelisted(true);
            Bukkit.setWhitelist(true);
        }

        CommandUtils.setOp(player, op);
        Commons.callEvent(new PlayerRankChangeEvent(player, Commons.getProfile(player)));

        if (changedBy != null) {
            CommandUtils.broadcastAlertMessage(
                Fetcher.getFancyName(changedBy),
                new TextComponent(" has " + (op ? "opped" : "deopped") + " "),
                Fetcher.getFancyName(player)
            );
        }
    }

}
