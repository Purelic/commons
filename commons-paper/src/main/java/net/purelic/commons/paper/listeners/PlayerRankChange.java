package net.purelic.commons.paper.listeners;

import net.purelic.commons.paper.events.PlayerRankChangeEvent;
import net.purelic.commons.paper.utils.PermissionUtils;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

public class PlayerRankChange implements Listener {

    @EventHandler
    public void onPlayerRankChange(PlayerRankChangeEvent event) {
        PermissionUtils.setPermissions(event.getPlayer());
    }

}
