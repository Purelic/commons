package net.purelic.commons.paper.commands.nick;

import cloud.commandframework.Command;
import cloud.commandframework.bukkit.BukkitCommandManager;
import net.purelic.commons.paper.commands.parsers.CustomCommand;
import net.purelic.commons.paper.commands.parsers.Permission;
import net.purelic.commons.paper.utils.CommandUtils;
import net.purelic.commons.paper.utils.NickUtils;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class NickCommand implements CustomCommand {

    @Override
    public Command.Builder<CommandSender> getCommandBuilder(BukkitCommandManager<CommandSender> mgr) {
        return mgr.commandBuilder("nick")
            .senderType(Player.class)
            .permission(Permission.isStaff())
            .handler(c -> {
                Player player = (Player) c.getSender();

                CommandUtils.sendAlertMessage(player, "Fetching a nickname...");

                String nick = NickUtils.getRandomNick();

                if (nick == null) {
                    CommandUtils.sendSuccessMessage(player, "Nicks are not available at this moment!");
                    return;
                }

                NickUtils.setNick(player, nick);
            });
    }

}
