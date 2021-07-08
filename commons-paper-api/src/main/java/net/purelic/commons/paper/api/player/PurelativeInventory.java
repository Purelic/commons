package net.purelic.commons.paper.api.player;

import org.bukkit.inventory.PlayerInventory;

public interface PurelativeInventory extends PlayerInventory {

    /**
     * Does this inventory contain any items(does not count air)
     * @return if the inventory contains any items
     */
    boolean isEmpty();
}
