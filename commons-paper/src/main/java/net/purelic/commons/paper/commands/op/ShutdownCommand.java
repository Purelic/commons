package net.purelic.commons.paper.commands.op;

import cloud.commandframework.Command;
import cloud.commandframework.bukkit.BukkitCommandManager;
import net.purelic.commons.paper.Commons;
import net.purelic.commons.paper.commands.parsers.CustomCommand;
import net.purelic.commons.paper.commands.parsers.Permission;
import net.purelic.commons.paper.utils.CommandUtils;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class ShutdownCommand implements CustomCommand {

    @Override
    public Command.Builder<CommandSender> getCommandBuilder(BukkitCommandManager<CommandSender> mgr) {
        return mgr.commandBuilder("shutdown")
            .senderType(Player.class)
            .permission(Permission.isAdmin(true))
            .handler(c -> {
                Player sender = (Player) c.getSender();

                if (!Commons.isOwner(sender) && !Commons.getProfile(sender).isAdmin()) {
                    CommandUtils.sendErrorMessage(sender, "Only the server owner can /shutdown!");
                    return;
                }

                Bukkit.shutdown();
            });
    }

}
