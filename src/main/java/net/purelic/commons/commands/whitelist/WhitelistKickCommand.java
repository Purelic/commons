package net.purelic.commons.commands.whitelist;

import cloud.commandframework.Command;
import cloud.commandframework.bukkit.BukkitCommandManager;
import net.purelic.commons.commands.parsers.CustomCommand;
import net.purelic.commons.commands.parsers.Permission;
import net.purelic.commons.utils.CommandUtils;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class WhitelistKickCommand implements CustomCommand {

    @Override
    public Command.Builder<CommandSender> getCommandBuilder(BukkitCommandManager<CommandSender> mgr) {
        return mgr.commandBuilder("whitelist", "wl")
            .senderType(Player.class)
            .permission(Permission.isAdmin(true))
            .literal("kick")
            .handler(c -> {
                Player sender = (Player) c.getSender();

                for (Player player : Bukkit.getOnlinePlayers()) {
                    if (!(CommandUtils.isOp(player) || player.isWhitelisted())) {
                        player.kickPlayer("This server is now whitelisted!");
                    }
                }

                Bukkit.setWhitelist(true);
                CommandUtils.broadcastAlertMessage("Server now whitelisted and kicked all non-op or whitelisted players");
            });
    }

}
