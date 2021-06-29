package net.purelic.commons.commands.admin;

import cloud.commandframework.Command;
import cloud.commandframework.arguments.standard.EnumArgument;
import cloud.commandframework.bukkit.BukkitCommandManager;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.TextComponent;
import net.purelic.commons.Commons;
import net.purelic.commons.commands.parsers.CustomCommand;
import net.purelic.commons.commands.parsers.Permission;
import net.purelic.commons.commands.parsers.ProfileArgument;
import net.purelic.commons.events.PlayerRankChangeEvent;
import net.purelic.commons.profile.Profile;
import net.purelic.commons.profile.Rank;
import net.purelic.commons.utils.CommandUtils;
import net.purelic.commons.utils.Fetcher;
import net.purelic.commons.utils.NickUtils;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.UUID;

public class PromoteCommand implements CustomCommand {

    @Override
    public Command.Builder<CommandSender> getCommandBuilder(BukkitCommandManager<CommandSender> mgr) {
        return mgr.commandBuilder("promote")
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

                if (targetProfile.hasRank(rank)) {
                    CommandUtils.sendErrorMessage(
                        player,
                        Fetcher.getFancyName(targetId),
                        new TextComponent(ChatColor.RED + " already has " +
                            rank.getName(true) + ChatColor.RED + " rank!"));
                    return;
                }

                targetProfile.addRank(rank);

                if (rank.hasChildRank()) targetProfile.removeRank(rank.getChildRank());

                if (targetPlayer != null && targetPlayer.isOnline() && !targetProfile.isNicked() && !NickUtils.isNicked(player)) {
                    targetPlayer.setPlayerListName(targetProfile.getFlairs() + targetPlayer.getDisplayName());

                    Bukkit.broadcast(
                        Fetcher.getFancyName(player),
                        new TextComponent(" has promoted "),
                        Fetcher.getFancyName(targetPlayer),
                        new TextComponent(" to " + rank.getName(true) + "!"));

                    Commons.callEvent(new PlayerRankChangeEvent(targetPlayer, targetProfile));
                } else {
                    CommandUtils.sendSuccessMessage(
                        player,
                        new ComponentBuilder(ChatColor.GREEN + "Successfully promoted ").create()[0],
                        Fetcher.getFancyName(targetId),
                        new ComponentBuilder(" to " + rank.getName(true) + "!").create()[0]);
                }
            });
    }

}
