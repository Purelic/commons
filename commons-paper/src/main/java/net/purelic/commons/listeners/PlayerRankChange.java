package net.purelic.commons.listeners;

import net.purelic.commons.events.PlayerRankChangeEvent;
import net.purelic.commons.utils.PermissionUtils;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

public class PlayerRankChange implements Listener {

    @EventHandler
    public void onPlayerRankChange(PlayerRankChangeEvent event) {
        PermissionUtils.setPermissions(event.getPlayer());
    }

}
