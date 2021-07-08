package net.purelic.commons.paper.modules;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.FoodLevelChangeEvent;

public class NoHungerModule implements Module {

    @EventHandler
    public void onFoodLevelChange(FoodLevelChangeEvent event) {
        ((Player) event.getEntity()).setSaturation(5);
        event.setCancelled(true);
    }

}
