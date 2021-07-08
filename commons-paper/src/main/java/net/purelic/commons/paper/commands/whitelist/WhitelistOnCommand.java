package net.purelic.commons.paper.commands.whitelist;

import cloud.commandframework.Command;
import cloud.commandframework.bukkit.BukkitCommandManager;
import net.purelic.commons.paper.commands.parsers.CustomCommand;
import net.purelic.commons.paper.commands.parsers.Permission;
import net.purelic.commons.paper.utils.CommandUtils;
import net.purelic.commons.paper.utils.ServerUtils;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class WhitelistOnCommand implements CustomCommand {

    @Override
    public Command.Builder<CommandSender> getCommandBuilder(BukkitCommandManager<CommandSender> mgr) {
        return mgr.commandBuilder("whitelist", "wl")
            .senderType(Player.class)
            .permission(Permission.isAdmin(true))
            .literal("on")
            .handler(c -> {
                Player sender = (Player) c.getSender();

                if (Bukkit.hasWhitelist()) {
                    CommandUtils.sendErrorMessage(sender, "The whitelist is already turned on!");
                } else {
                    ServerUtils.setWhitelisted(true);
                    CommandUtils.sendSuccessMessage(sender, "You turned the whitelist on!");
                }
            });
    }

}
