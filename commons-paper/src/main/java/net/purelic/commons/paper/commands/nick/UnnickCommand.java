package net.purelic.commons.paper.commands.nick;

import cloud.commandframework.Command;
import cloud.commandframework.bukkit.BukkitCommandManager;
import net.purelic.commons.paper.commands.parsers.CustomCommand;
import net.purelic.commons.paper.utils.NickUtils;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class UnnickCommand implements CustomCommand {

    @Override
    public Command.Builder<CommandSender> getCommandBuilder(BukkitCommandManager<CommandSender> mgr) {
        return mgr.commandBuilder("unnick")
            .senderType(Player.class)
            .handler(c -> NickUtils.removeNick((Player) c.getSender()));
    }

}
