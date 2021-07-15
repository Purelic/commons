package net.purelic.commons.paper.modules;

import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;

public class TeleportFromVoidModule implements Listener {

    public static final TeleportFromVoidModule INSTANCE = new TeleportFromVoidModule();

    private TeleportFromVoidModule(){}

    @EventHandler
    public void onEntityDamage(EntityDamageEvent event){
        final Entity entity = event.getEntity();

        if(entity instanceof Player && event.getCause() == EntityDamageEvent.DamageCause.VOID) {
            entity.teleport(entity.getWorld().getSpawnLocation());
        }
    }
}
