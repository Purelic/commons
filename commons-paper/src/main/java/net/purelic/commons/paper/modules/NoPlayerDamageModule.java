package net.purelic.commons.paper.modules;

import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;

public class NoPlayerDamageModule implements Listener {

    public static final NoPlayerDamageModule INSTANCE = new NoPlayerDamageModule();

    private NoPlayerDamageModule(){}

    // Prevent players from taking damage
    @EventHandler
    public void onEntityDamage(EntityDamageEvent event) {
        Entity entity = event.getEntity();

        if (entity instanceof Player) {
            event.setCancelled(true);
            entity.setFireTicks(0);
        }
    }

    // Prevent non-player entities from damaging each other
    @EventHandler
    public void onEntityDamageByEntity(EntityDamageByEntityEvent event) {
        Entity damager = event.getDamager();
        if (!(damager instanceof Player)) event.setCancelled(true);
    }

}
