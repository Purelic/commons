package net.purelic.commons.commands.nick;

import cloud.commandframework.Command;
import cloud.commandframework.bukkit.BukkitCommandManager;
import net.purelic.commons.commands.parsers.CustomCommand;
import net.purelic.commons.utils.NickUtils;
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
