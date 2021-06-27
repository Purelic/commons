package net.purelic.commons.modules;

import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;

public class NoPlayerDamageModule implements Module {

    private final boolean teleportVoidToSpawn;

    public NoPlayerDamageModule(boolean teleportVoidToSpawn) {
        this.teleportVoidToSpawn = teleportVoidToSpawn;
    }

    // Prevent players from taking damage
    @EventHandler
    public void onEntityDamage(EntityDamageEvent event) {
        Entity entity = event.getEntity();

        if (entity instanceof Player) {
            event.setCancelled(true);
            entity.setFireTicks(0);

            // Teleport players to world spawn if they take void damage
            if (this.teleportVoidToSpawn && event.getCause() == EntityDamageEvent.DamageCause.VOID) {
                entity.teleport(entity.getWorld().getSpawnLocation());
            }
        }
    }

    // Prevent non-player entities from damaging each other
    @EventHandler
    public void onEntityDamageByEntity(EntityDamageByEntityEvent event) {
        Entity damager = event.getDamager();
        if (!(damager instanceof Player)) event.setCancelled(true);
    }

}
