package net.purelic.commons.paper.listeners;

import net.purelic.commons.paper.utils.CommandUtils;
import net.purelic.commons.paper.utils.ItemCrafter;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

public class PlayerInteract implements Listener {

    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        boolean op = CommandUtils.isOp(player);

        ItemStack item = event.getItem();

        if (item == null) return;

        ItemCrafter itemCrafter = new ItemCrafter(item);

        if (!itemCrafter.hasTag("command")) return;

        String command = itemCrafter.getTag("command");
        boolean opOnly = Boolean.parseBoolean(itemCrafter.getTag("op_only"));

        if (opOnly && !op) return;

        Bukkit.dispatchCommand(player, command);
    }

}
