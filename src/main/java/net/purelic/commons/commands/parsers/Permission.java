package net.purelic.commons.commands.parsers;

import cloud.commandframework.context.CommandContext;
import cloud.commandframework.keys.SimpleCloudKey;
import cloud.commandframework.permission.PredicatePermission;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.purelic.commons.Commons;
import net.purelic.commons.profile.Profile;
import net.purelic.commons.profile.Rank;
import net.purelic.commons.utils.CommandUtils;
import net.purelic.commons.utils.text.TextBuilder;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.checkerframework.checker.nullness.qual.NonNull;

public class Permission {

    public static PredicatePermission<CommandSender> of(boolean opEnabled, Rank... ranks) {
        return PredicatePermission.of(
            SimpleCloudKey.of("rank"), sender -> {
                if (!(sender instanceof Player)) return false;

                Player player = (Player) sender;
                Profile profile = Commons.getProfile(player);

                return (opEnabled && CommandUtils.isOp(player)) || profile.isAdmin() || profile.hasRank(ranks);
            }
        );
    }

    public static PredicatePermission<CommandSender> isAdmin() {
        return isAdmin(false);
    }

    public static PredicatePermission<CommandSender> isAdmin(boolean opEnabled) {
        return of(opEnabled);
    }

    public static PredicatePermission<CommandSender> isStaff() {
        return isStaff(false);
    }

    public static PredicatePermission<CommandSender> isStaff(boolean opEnabled) {
        return of(opEnabled, Rank.MAP_DEVELOPER, Rank.MODERATOR, Rank.HELPER);
    }

    public static PredicatePermission<CommandSender> isMod() {
        return isMod(false);
    }

    public static PredicatePermission<CommandSender> isMod(boolean opEnabled) {
        return of(opEnabled, Rank.MAP_DEVELOPER, Rank.MODERATOR, Rank.HELPER);
    }

    public static PredicatePermission<CommandSender> isMapDev() {
        return isMapDev(false);
    }

    public static PredicatePermission<CommandSender> isMapDev(boolean opEnabled) {
        return of(opEnabled, Rank.MAP_DEVELOPER);
    }

    @Deprecated
    public static PredicatePermission<CommandSender> isPremium() {
        return isPremium(false);
    }

    @Deprecated
    public static PredicatePermission<CommandSender> isPremium(boolean opEnabled) {
        return of(opEnabled, Rank.PREMIUM, Rank.CREATOR, Rank.MAP_DEVELOPER, Rank.MODERATOR, Rank.HELPER);
    }

    public static boolean notPremium(@NonNull CommandContext<CommandSender> context) {
        return notPremium(context, null);
    }

    public static boolean notPremium(@NonNull CommandContext<CommandSender> context, String message) {
        Player player = (Player) context.getSender();
        Profile profile = Commons.getProfile(player);
        boolean hasPermission = profile.isDonator(true);

        if (!hasPermission) {
            CommandUtils.sendErrorMessage(player,
                new ComponentBuilder(message != null ? message : "Only Premium players can use this feature!")
                    .color(ChatColor.RED)
                    .append(" Consider donating at ")
                    .color(ChatColor.RED)
                    .append("purelic.net/donate")
                    .color(ChatColor.AQUA)
                    .underlined(true)
                    .event(TextBuilder.hover("Click to Open"))
                    .event(new ClickEvent(ClickEvent.Action.OPEN_URL, "https://purelic.net/donate"))
                    .create()
            );
        }

        return !hasPermission;
    }


    public static PredicatePermission<CommandSender> isCreator() {
        return isCreator(false);
    }

    public static PredicatePermission<CommandSender> isCreator(boolean opEnabled) {
        return of(opEnabled, Rank.CREATOR);
    }

}
