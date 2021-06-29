package net.purelic.commons.modules;

import org.bukkit.event.EventHandler;
import org.bukkit.event.block.LeavesDecayEvent;

public class NoLeavesDecayModule implements Module {

    @EventHandler
    public void onLeavesDecay(LeavesDecayEvent event) {
        event.setCancelled(true);
    }

}
