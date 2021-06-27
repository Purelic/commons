package net.purelic.commons.commands.whitelist;

import cloud.commandframework.Command;
import cloud.commandframework.bukkit.BukkitCommandManager;
import cloud.commandframework.bukkit.parsers.OfflinePlayerArgument;
import net.purelic.commons.commands.parsers.CustomCommand;
import net.purelic.commons.commands.parsers.Permission;
import net.purelic.commons.utils.CommandUtils;
import net.purelic.commons.utils.Fetcher;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class WhitelistRemoveCommand implements CustomCommand {

    @Override
    public Command.Builder<CommandSender> getCommandBuilder(BukkitCommandManager<CommandSender> mgr) {
        return mgr.commandBuilder("whitelist", "wl")
            .senderType(Player.class)
            .permission(Permission.isAdmin(true))
            .literal("remove")
            .argument(OfflinePlayerArgument.of("player"))
            .handler(c -> {
                Player sender = (Player) c.getSender();
                OfflinePlayer player = c.get("player");

                if (!Bukkit.getWhitelistedPlayers().contains(player)) {
                    CommandUtils.sendErrorMessage(sender, "That player not whitelisted!");
                } else {
                    player.setWhitelisted(false);
                    CommandUtils.sendSuccessMessage(sender, "Removed \"" + Fetcher.getNameOf(player) + "\" from the whitelist!");
                }
            });
    }

}
