package net.purelic.commons.commands.nick;

import cloud.commandframework.Command;
import cloud.commandframework.arguments.standard.StringArgument;
import cloud.commandframework.bukkit.BukkitCommandManager;
import net.purelic.commons.commands.parsers.CustomCommand;
import net.purelic.commons.commands.parsers.Permission;
import net.purelic.commons.utils.NickUtils;
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
