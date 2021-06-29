package net.purelic.commons.listeners;

import net.purelic.commons.utils.CommandUtils;
import net.purelic.commons.utils.NickUtils;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockState;
import org.bukkit.block.CommandBlock;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.minecart.CommandMinecart;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockDispenseEvent;
import org.bukkit.event.block.BlockRedstoneEvent;
import org.bukkit.event.player.PlayerChatTabCompleteEvent;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.event.server.ServerCommandEvent;
import org.bukkit.event.vehicle.VehicleCreateEvent;
import org.bukkit.event.vehicle.VehicleUpdateEvent;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ProcessCommand implements Listener {

    private static final List<String> WHITELISTED_COMMANDS = new ArrayList<>(Arrays.asList(
        "minecraft:tp",
        "minecraft:gamemode",
        "gamemode",
        "gamerule",
        "give",
        "summon",
        "effect",
        "enchant"
    ));

    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void onPlayerCommandPreprocess(PlayerCommandPreprocessEvent event) {
        String command = this.getBaseCommand(event.getMessage());

        if (WHITELISTED_COMMANDS.contains(command)) return;

        if (command.contains(":")) {
            event.setCancelled(true);
            CommandUtils.sendErrorMessage(event.getPlayer(), "This command is currently disabled!");
        }
    }

    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void onServerCommand(ServerCommandEvent event) {
        String command = event.getCommand();
        String base = this.getBaseCommand(command);

        if (base.equals("tp")) {
            Bukkit.dispatchCommand(event.getSender(), command.replaceFirst("tp", "minecraft:tp"));
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onBlockRedstone(BlockRedstoneEvent event) {
        Block block = event.getBlock();
        BlockState blockState = block.getState();

        if (!(blockState instanceof CommandBlock)) return;

        CommandBlock commandBlock = (CommandBlock) blockState;

        String command = commandBlock.getCommand();
        String base = this.getBaseCommand(command);

        // Fix tp commands
        if (base.equals("tp")) {
            commandBlock.setCommand(command.replaceFirst("tp", "minecraft:tp"));
            blockState.update(true);
            return;
        }

        // Fix gamemode commands
        if (base.equals("gamemode")) {
            commandBlock.setCommand(command.replaceFirst("gamemode", "minecraft:gamemode"));
            blockState.update(true);
            return;
        }

        if (!WHITELISTED_COMMANDS.contains(base)) {
            event.setNewCurrent(0); // cancel the command block
        }
    }

    @EventHandler
    public void onCommandMinecartUpdate(VehicleUpdateEvent event) {
        if (event.getVehicle().getType() == EntityType.MINECART_COMMAND) {
            this.handleCommandBlockMinecart((CommandMinecart) event.getVehicle());
        }
    }

    @EventHandler
    public void onCommandMinecartCreate(VehicleCreateEvent event) {
        if (event.getVehicle().getType() == EntityType.MINECART_COMMAND) {
            this.handleCommandBlockMinecart((CommandMinecart) event.getVehicle());
        }
    }

    // Don't allow command block minecarts to be dispensed
    @EventHandler
    public void onCommandMinecartDispense(BlockDispenseEvent event) {
        if (event.getItem().getType() == Material.COMMAND_MINECART) event.setCancelled(true);
    }

    private void handleCommandBlockMinecart(CommandMinecart commandMinecart) {
        String command = commandMinecart.getCommand();
        String base = this.getBaseCommand(command);

        // Fix tp commands
        if (base.equals("tp")) {
            commandMinecart.setCommand(command.replaceFirst("tp", "minecraft:tp"));
            return;
        }

        // Fix gamemode commands
        if (base.equals("gamemode")) {
            commandMinecart.setCommand(command.replaceFirst("gamemode", "minecraft:gamemode"));
            return;
        }

        if (!base.isEmpty() && !WHITELISTED_COMMANDS.contains(base)) {
            commandMinecart.remove(); // remove the command block minecart
            Bukkit.broadcastMessage("Minecart with Command Block removed (disabled command): " + command);
        }
    }

    @EventHandler
    public void onPlayerChatTabCompleteEvent(PlayerChatTabCompleteEvent event) {
        List<String> filteredCompletions = NickUtils.filter(event.getTabCompletions());
        List<String> nickCompletions = NickUtils.getNickCompletions(event.getLastToken());

        event.getTabCompletions().clear();
        event.getTabCompletions().addAll(filteredCompletions);
        event.getTabCompletions().addAll(nickCompletions);
    }

    private String getBaseCommand(String command) {
        return command.toLowerCase().split(" ")[0].replaceAll("/", "");
    }

}
