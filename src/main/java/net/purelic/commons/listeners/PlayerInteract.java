package net.purelic.commons.listeners;

import net.purelic.commons.Commons;
import net.purelic.commons.utils.CommandUtils;
import net.purelic.commons.utils.ItemCrafter;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.block.BlockState;
import org.bukkit.block.Skull;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
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

        if (itemCrafter.hasTag("command")) {
            String command = itemCrafter.getTag("command");
            boolean opOnly = Boolean.parseBoolean(itemCrafter.getTag("op_only"));

            if (!opOnly || op) Bukkit.dispatchCommand(player, command);
        }

        if (itemCrafter.hasTag("spring")) {
            String channel = itemCrafter.getTag("spring");
            Commons.sendSpringMessage(player, channel);
        }
    }

    @EventHandler
    public void onPlayerDropItem(PlayerDropItemEvent event) {
        ItemStack item = event.getItemDrop().getItemStack();
        ItemCrafter itemCrafter = new ItemCrafter(item);

        if (itemCrafter.hasTag("command")
            || itemCrafter.hasTag("spring")
            || itemCrafter.hasTag("gui")) {
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        Player player = (Player) event.getWhoClicked();
        boolean op = CommandUtils.isOp(player);
        ItemStack item = event.getCurrentItem();

        if (item == null) return;

        ItemCrafter itemCrafter = new ItemCrafter(item);

        if (itemCrafter.hasTag("gui")) {
            event.setCancelled(true);
            player.updateInventory();

            if (itemCrafter.hasTag("command")) {
                String command = itemCrafter.getTag("command");
                boolean opOnly = Boolean.parseBoolean(itemCrafter.getTag("op_only"));

                if (!opOnly || op) Bukkit.dispatchCommand(player, command);
            }
        }
    }

    @EventHandler
    public void onPlayerInteractHead(PlayerInteractEvent event) {
        if (event.getAction() == Action.RIGHT_CLICK_BLOCK) {
            Player player = event.getPlayer();
            BlockState block = event.getClickedBlock().getState();

            if (block instanceof Skull) {
                Skull skull = (Skull) block;
                String owner = skull.getOwner();
                if (owner != null) CommandUtils.sendAlertMessage(player, "That's the head of " + ChatColor.DARK_AQUA + owner);
            }
        }

    }

}
