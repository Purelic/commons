package net.purelic.commons.commands.whitelist;

import cloud.commandframework.Command;
import cloud.commandframework.bukkit.BukkitCommandManager;
import net.purelic.commons.commands.parsers.CustomCommand;
import net.purelic.commons.commands.parsers.Permission;
import net.purelic.commons.utils.CommandUtils;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class WhitelistClearCommand implements CustomCommand {

    @Override
    public Command.Builder<CommandSender> getCommandBuilder(BukkitCommandManager<CommandSender> mgr) {
        return mgr.commandBuilder("whitelist", "wl")
            .senderType(Player.class)
            .permission(Permission.isAdmin(true))
            .literal("clear")
            .handler(c -> {
                Player sender = (Player) c.getSender();

                if (Bukkit.getWhitelistedPlayers().size() == 0) {
                    CommandUtils.sendErrorMessage(sender, "There are currently no players whitelisted!");
                } else {
                    Bukkit.getWhitelistedPlayers().clear();
                    CommandUtils.sendSuccessMessage(sender, "You successfully cleared the whitelist!");
                }
            });
    }

}
