package net.purelic.commons.paper.commands.op;

import cloud.commandframework.Command;
import cloud.commandframework.bukkit.BukkitCommandManager;
import net.purelic.commons.paper.Commons;
import net.purelic.commons.paper.commands.parsers.CustomCommand;
import net.purelic.commons.paper.commands.parsers.Permission;
import net.purelic.commons.paper.commands.parsers.PlayerArgument;
import net.purelic.commons.paper.events.OpStatusChangeEvent;
import net.purelic.commons.paper.utils.CommandUtils;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class DeopCommand implements CustomCommand {

    @Override
    public Command.Builder<CommandSender> getCommandBuilder(BukkitCommandManager<CommandSender> mgr) {
        return mgr.commandBuilder("deop")
            .senderType(Player.class)
            .permission(Permission.isAdmin(true))
            .argument(PlayerArgument.of("player"))
            .handler(c -> {
                Player player = (Player) c.getSender();
                Player target = c.get("player");

                if (CommandUtils.isOp(target)) {
                    if (Commons.isOwner(target)) {
                        CommandUtils.sendErrorMessage(player, "You cannot deop the server owner!");
                        return;
                    }

                    Commons.callEvent(new OpStatusChangeEvent(target, player, false));
                } else {
                    CommandUtils.sendErrorMessage(player, "That player is not op!");
                }
            });
    }

}
