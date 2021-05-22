package net.purelic.commons.commands.nick;

import cloud.commandframework.Command;
import cloud.commandframework.bukkit.BukkitCommandManager;
import net.purelic.commons.Commons;
import net.purelic.commons.commands.parsers.CustomCommand;
import net.purelic.commons.commands.parsers.Permission;
import net.purelic.commons.utils.ChatUtils;
import net.purelic.commons.utils.Fetcher;
import net.purelic.commons.utils.NickUtils;
import net.purelic.commons.utils.text.ListBuilder;
import net.purelic.commons.utils.text.TextBuilder;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class NicksCommand implements CustomCommand {

    @Override
    public Command.Builder<CommandSender> getCommandBuilder(BukkitCommandManager<CommandSender> mgr) {
        return mgr.commandBuilder("nicks")
            .senderType(Player.class)
            .permission(Permission.isStaff())
            .handler(c -> {
                Player player = (Player) c.getSender();

                ListBuilder list = new ListBuilder("Nicked Players", "No nicked players online.");

                for (Player nicked : NickUtils.getNickedPlayers()) {
                    list.add(
                        TextBuilder.bullet(Commons.getProfile(nicked).getFlairs(false, true) + ChatColor.DARK_AQUA +
                            NickUtils.getRealName(nicked) + " " + ChatColor.GRAY + ChatUtils.ARROW + " ").build(),
                        Fetcher.getFancyName(nicked)
                    );
                }

                list.send(player);
            });
    }

}
