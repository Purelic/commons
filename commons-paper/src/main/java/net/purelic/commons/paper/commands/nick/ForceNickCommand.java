package net.purelic.commons.paper.commands.nick;

import cloud.commandframework.Command;
import cloud.commandframework.arguments.standard.StringArgument;
import cloud.commandframework.bukkit.BukkitCommandManager;
import net.purelic.commons.paper.commands.parsers.CustomCommand;
import net.purelic.commons.paper.commands.parsers.Permission;
import net.purelic.commons.paper.utils.NickUtils;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class ForceNickCommand implements CustomCommand {

    @Override
    public Command.Builder<CommandSender> getCommandBuilder(BukkitCommandManager<CommandSender> mgr) {
        return mgr.commandBuilder("fnick")
            .senderType(Player.class)
            .permission(Permission.isAdmin())
            .argument(StringArgument.of("nickname"))
            .handler(c -> {
                Player player = (Player) c.getSender();
                String nick = c.get("nickname");
                NickUtils.setNick(player, nick);
            });
    }

}
