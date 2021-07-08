package net.purelic.commons.paper.utils;

import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;

import java.util.Arrays;
import java.util.Objects;

public class InventoryUtils {

    public static boolean isEmpty(Player player) {
        return isEmpty(player.getInventory());
    }

    public static boolean isEmpty(Inventory inventory) {
        return Arrays.stream(inventory.getContents()).anyMatch(Objects::nonNull);
    }

}
