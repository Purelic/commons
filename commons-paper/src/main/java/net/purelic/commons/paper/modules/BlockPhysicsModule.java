package net.purelic.commons.paper.modules;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPhysicsEvent;

public class BlockPhysicsModule implements Listener {

    public static final BlockPhysicsModule INSTANCE = new BlockPhysicsModule();

    private BlockPhysicsModule(){}

    @EventHandler
    public void onBlockPhysics(BlockPhysicsEvent event) {
        event.setCancelled(true);
    }

}
