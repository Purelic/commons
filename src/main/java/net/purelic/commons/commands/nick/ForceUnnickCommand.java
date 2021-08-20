package net.purelic.commons.commands.nick;

import cloud.commandframework.Command;
import cloud.commandframework.bukkit.BukkitCommandManager;
import net.purelic.commons.Commons;
import net.purelic.commons.commands.parsers.CustomCommand;
import net.purelic.commons.commands.parsers.Permission;
import net.purelic.commons.commands.parsers.ProfileArgument;
import net.purelic.commons.events.NickChangedEvent;
import net.purelic.commons.profile.Profile;
import net.purelic.commons.utils.CommandUtils;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class ForceUnnickCommand implements CustomCommand {

    @Override
    public Command.Builder<CommandSender> getCommandBuilder(BukkitCommandManager<CommandSender> mgr) {
        return mgr.commandBuilder("funnick")
            .senderType(Player.class)
            .permission(Permission.isAdmin())
            .argument(ProfileArgument.of("player"))
            .handler(c -> {
                Profile profile = c.get("player");

                if (!profile.isNicked()) {
                    CommandUtils.sendErrorMessage((Player) c.getSender(), "That player is not currently nicked!");
                    return;
                }

                profile.updateNick(null);

                if (profile.getPlayer() != null) { // player online
                    Player player = profile.getPlayer();
                    Commons.callEvent(new NickChangedEvent(player));
                    player.kickPlayer("You were kicked to have your nickname removed!");
                }
            });
    }

}
