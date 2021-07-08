package net.purelic.commons.paper.commands.whitelist;

import cloud.commandframework.Command;
import cloud.commandframework.bukkit.BukkitCommandManager;
import net.purelic.commons.paper.commands.parsers.CustomCommand;
import net.purelic.commons.paper.commands.parsers.OfflinePlayerArgument;
import net.purelic.commons.paper.commands.parsers.Permission;
import net.purelic.commons.paper.utils.CommandUtils;
import net.purelic.commons.paper.utils.Fetcher;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class WhitelistAddCommand implements CustomCommand {

    @Override
    public Command.Builder<CommandSender> getCommandBuilder(BukkitCommandManager<CommandSender> mgr) {
        return mgr.commandBuilder("whitelist", "wl")
            .senderType(Player.class)
            .permission(Permission.isAdmin(true))
            .literal("add")
            .argument(OfflinePlayerArgument.of("player"))
            .handler(c -> {
                Player sender = (Player) c.getSender();
                OfflinePlayer player = c.get("player");

                if (Bukkit.getWhitelistedPlayers().contains(player)) {
                    CommandUtils.sendErrorMessage(sender, "That player is already whitelisted!");
                } else {
                    player.setWhitelisted(true);
                    CommandUtils.sendSuccessMessage(sender, "Added \"" + Fetcher.getNameOf(player) + "\" to the whitelist!");
                }
            });
    }

}
