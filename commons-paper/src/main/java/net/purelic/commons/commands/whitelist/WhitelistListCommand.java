package net.purelic.commons.commands.whitelist;

import cloud.commandframework.Command;
import cloud.commandframework.bukkit.BukkitCommandManager;
import net.purelic.commons.commands.parsers.CustomCommand;
import net.purelic.commons.commands.parsers.Permission;
import net.purelic.commons.utils.CommandUtils;
import net.purelic.commons.utils.Fetcher;
import net.purelic.commons.utils.text.ListBuilder;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class WhitelistListCommand implements CustomCommand {

    @Override
    public Command.Builder<CommandSender> getCommandBuilder(BukkitCommandManager<CommandSender> mgr) {
        return mgr.commandBuilder("whitelist", "wl")
            .senderType(Player.class)
            .permission(Permission.isAdmin(true))
            .literal("list")
            .handler(c -> {
                Player sender = (Player) c.getSender();

                if (!Bukkit.hasWhitelist()) {
                    CommandUtils.sendAlertMessage(sender, "The whitelist is currently turned off");
                    return;
                }

                ListBuilder list = new ListBuilder("Whitelisted Players", "There are no players on the whitelist");

                for (OfflinePlayer offlinePlayer : Bukkit.getWhitelistedPlayers()) {
                    list.add(Fetcher.getFancyName(offlinePlayer.getUniqueId()));
                }

                list.send(sender);
            });
    }

}
