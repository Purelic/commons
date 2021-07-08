package net.purelic.commons.paper.commands.op;

import cloud.commandframework.Command;
import cloud.commandframework.bukkit.BukkitCommandManager;
import net.purelic.commons.paper.Commons;
import net.purelic.commons.paper.commands.parsers.CustomCommand;
import net.purelic.commons.paper.commands.parsers.Permission;
import net.purelic.commons.paper.commands.parsers.PlayerArgument;
import net.purelic.commons.paper.profile.Rank;
import net.purelic.commons.paper.utils.CommandUtils;
import net.purelic.commons.paper.utils.NickUtils;
import net.purelic.commons.paper.utils.TaskUtils;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class KickCommand implements CustomCommand {

    @Override
    public Command.Builder<CommandSender> getCommandBuilder(BukkitCommandManager<CommandSender> mgr) {
        return mgr.commandBuilder("kick")
            .senderType(Player.class)
            .permission(Permission.isMod(true))
            .argument(PlayerArgument.of("player"))
            .handler(c -> {
                Player player = (Player) c.getSender();
                Player target = c.get("player");

                if (CommandUtils.isOp(target)) {
                    CommandUtils.sendErrorMessage(player, "You can't kick op players!");
                    return;
                }

                if (Commons.getProfile(target).hasRank(Rank.ADMIN)) {
                    CommandUtils.sendErrorMessage(player, "You can't kick Admins!");
                    return;
                }

                CommandUtils.sendSuccessMessage(player, "You kicked " + NickUtils.getNick(target) + "!");
                TaskUtils.run(() -> target.kickPlayer("You were kicked from this server!"));
            });
    }

}
