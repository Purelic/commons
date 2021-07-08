package net.purelic.commons.paper.modules;

import org.bukkit.event.EventHandler;
import org.bukkit.event.block.BlockPhysicsEvent;

public class BlockPhysicsModule implements Module {

    private static boolean enabled = true;

    @EventHandler
    public void onBlockPhysics(BlockPhysicsEvent event) {
        if (!enabled) event.setCancelled(true);
    }

    public static void setBlockPhysics(boolean enabled) {
        BlockPhysicsModule.enabled = enabled;
    }

    public static void toggleBlocKPhysics() {
        BlockPhysicsModule.enabled = !enabled;
    }

}
