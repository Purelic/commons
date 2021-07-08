package net.purelic.commons.paper.commands.op;

import cloud.commandframework.Command;
import cloud.commandframework.arguments.standard.StringArgument;
import cloud.commandframework.bukkit.BukkitCommandManager;
import net.purelic.commons.paper.commands.parsers.CustomCommand;
import net.purelic.commons.paper.commands.parsers.Permission;
import net.purelic.commons.paper.utils.ChatUtils;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class AlertCommand implements CustomCommand {

    @Override
    public Command.Builder<CommandSender> getCommandBuilder(BukkitCommandManager<CommandSender> mgr) {
        return mgr.commandBuilder("alert")
            .senderType(Player.class)
            .permission(Permission.isStaff(true))
            .argument(StringArgument.greedy("message"))
            .handler(c -> {
                String message = c.get("message");
                ChatUtils.broadcastAlert(message);
            });
    }

}
