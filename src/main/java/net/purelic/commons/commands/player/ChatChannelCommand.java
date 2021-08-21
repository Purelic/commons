package net.purelic.commons.commands.player;

import cloud.commandframework.Command;
import cloud.commandframework.arguments.standard.EnumArgument;
import cloud.commandframework.bukkit.BukkitCommandManager;
import net.purelic.commons.Commons;
import net.purelic.commons.commands.parsers.CustomCommand;
import net.purelic.commons.profile.preferences.ChatChannel;
import net.purelic.commons.utils.CommandUtils;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class ChatChannelCommand implements CustomCommand {

    @Override
    public Command.Builder<CommandSender> getCommandBuilder(BukkitCommandManager<CommandSender> mgr) {
        return mgr.commandBuilder("chat")
            .senderType(Player.class)
            .argument(EnumArgument.of(ChatChannel.class, "channel"))
            .handler(c -> {
                Player player = (Player) c.getSender();
                ChatChannel channel = c.get("channel");

                if (channel == ChatChannel.STAFF && !Commons.getProfile(player).isStaff()) {
                    CommandUtils.sendErrorMessage(player, "You can't use this chat channel!");
                    return;
                }

                Commons.getProfile(player).setChatChannel(channel);
                CommandUtils.sendSuccessMessage(player, "You've set your chat channel to " + channel.name().toLowerCase() + "!");
            });
    }

}
