package net.purelic.commons.paper.modules;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.LeavesDecayEvent;

public class NoLeavesDecayModule implements Listener {

    public static final NoLeavesDecayModule INSTANCE = new NoLeavesDecayModule();

    private NoLeavesDecayModule(){}

    @EventHandler
    public void onLeavesDecay(LeavesDecayEvent event) {
        event.setCancelled(true);
    }

}
