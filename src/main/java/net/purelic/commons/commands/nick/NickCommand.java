package net.purelic.commons.commands.nick;

import cloud.commandframework.Command;
import cloud.commandframework.bukkit.BukkitCommandManager;
import net.purelic.commons.Commons;
import net.purelic.commons.commands.parsers.CustomCommand;
import net.purelic.commons.utils.CommandUtils;
import net.purelic.commons.utils.NickUtils;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class NickCommand implements CustomCommand {

    @Override
    public Command.Builder<CommandSender> getCommandBuilder(BukkitCommandManager<CommandSender> mgr) {
        return mgr.commandBuilder("nick")
            .senderType(Player.class)
            .handler(c -> {
                Player player = (Player) c.getSender();

                if (!Commons.getProfile(player).isDonator(true)) {
                    CommandUtils.sendErrorMessage(player, "Only Premium players can use /nick! Consider donating to support the server at purelic.net/donate");
                    return;
                }

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
