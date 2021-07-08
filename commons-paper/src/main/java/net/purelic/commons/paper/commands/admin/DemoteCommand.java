package net.purelic.commons.paper.commands.admin;

import cloud.commandframework.Command;
import cloud.commandframework.arguments.standard.EnumArgument;
import cloud.commandframework.bukkit.BukkitCommandManager;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.TextComponent;
import net.purelic.commons.paper.Commons;
import net.purelic.commons.paper.commands.parsers.CustomCommand;
import net.purelic.commons.paper.commands.parsers.Permission;
import net.purelic.commons.paper.commands.parsers.ProfileArgument;
import net.purelic.commons.paper.events.PlayerRankChangeEvent;
import net.purelic.commons.paper.profile.Profile;
import net.purelic.commons.paper.profile.Rank;
import net.purelic.commons.paper.utils.CommandUtils;
import net.purelic.commons.paper.utils.Fetcher;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.UUID;

public class DemoteCommand implements CustomCommand {

    @Override
    public Command.Builder<CommandSender> getCommandBuilder(BukkitCommandManager<CommandSender> mgr) {
        return mgr.commandBuilder("demote")
            .senderType(Player.class)
            .permission(Permission.isAdmin())
            .argument(ProfileArgument.of("player"))
            .argument(EnumArgument.newBuilder(Rank.class, "rank"))
            .handler(c -> {
                Player player = (Player) c.getSender();
                Profile targetProfile = c.get("player");
                Rank rank = c.get("rank");

                UUID targetId = targetProfile.getUniqueId();
                Player targetPlayer = Bukkit.getPlayer(targetId);

                if (!targetProfile.hasRank(rank)) {
                    CommandUtils.sendErrorMessage(
                        player,
                        Fetcher.getFancyName(targetId),
                        new TextComponent(ChatColor.RED + " does not have " + rank.getName(true) + ChatColor.RED + " rank!"));

                    return;
                }

                targetProfile.removeRank(rank);

                CommandUtils.sendSuccessMessage(
                    player,
                    new ComponentBuilder("You removed ").color(ChatColor.GREEN).create()[0],
                    new TextComponent(rank.getName(true)),
                    new ComponentBuilder(" rank from ").color(ChatColor.GREEN).create()[0],
                    Fetcher.getFancyName(targetId),
                    new ComponentBuilder("!").color(ChatColor.GREEN).create()[0]);

                if (targetPlayer != null && targetPlayer.isOnline()) {
                    targetPlayer.setPlayerListName(targetProfile.getFlairs() + targetPlayer.getDisplayName());

                    CommandUtils.sendAlertMessage(
                        targetPlayer,
                        Fetcher.getFancyName(player),
                        new ComponentBuilder(" has removed your ").create()[0],
                        new TextComponent(rank.getName(true)),
                        new ComponentBuilder(" rank!").color(ChatColor.WHITE).create()[0]);

                    Commons.callEvent(new PlayerRankChangeEvent(targetPlayer, targetProfile));
                }
            });
    }

}
