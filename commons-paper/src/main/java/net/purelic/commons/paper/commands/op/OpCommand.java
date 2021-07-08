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

public class OpCommand implements CustomCommand {

    @Override
    public Command.Builder<CommandSender> getCommandBuilder(BukkitCommandManager<CommandSender> mgr) {
        return mgr.commandBuilder("op")
            .senderType(Player.class)
            .permission(Permission.isAdmin(true))
            .argument(PlayerArgument.of("player"))
            .handler(c -> {
                Player player = (Player) c.getSender();
                Player target = c.get("player");

                if (!CommandUtils.isOp(target)) {
                    Commons.callEvent(new OpStatusChangeEvent(target, player, true));
                } else {
                    CommandUtils.sendErrorMessage(player, "That player is already Op!");
                }
            });
    }

}
