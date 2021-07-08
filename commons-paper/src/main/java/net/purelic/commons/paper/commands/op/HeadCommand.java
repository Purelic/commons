package net.purelic.commons.paper.commands.op;

import cloud.commandframework.Command;
import cloud.commandframework.arguments.standard.StringArgument;
import cloud.commandframework.bukkit.BukkitCommandManager;
import net.purelic.commons.paper.commands.parsers.CustomCommand;
import net.purelic.commons.paper.commands.parsers.Permission;
import org.bukkit.Material;
import org.bukkit.SkullType;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SkullMeta;

public class HeadCommand implements CustomCommand {

    @Override
    public Command.Builder<CommandSender> getCommandBuilder(BukkitCommandManager<CommandSender> mgr) {
        return mgr.commandBuilder("head")
            .senderType(Player.class)
            .permission(Permission.isMapDev(true))
            .argument(StringArgument.of("player"))
            .handler(c -> {
                Player player = (Player) c.getSender();
                String name = c.get("player");

                ItemStack skull = new ItemStack(Material.SKULL_ITEM, 1, (short) SkullType.PLAYER.ordinal());
                SkullMeta meta = (SkullMeta) skull.getItemMeta();
                meta.setOwner(name);
                skull.setItemMeta(meta);

                player.getInventory().addItem(skull);
            });
    }

}
