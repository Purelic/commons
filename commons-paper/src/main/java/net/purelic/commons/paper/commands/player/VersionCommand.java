package net.purelic.commons.paper.commands.player;

import cloud.commandframework.Command;
import cloud.commandframework.bukkit.BukkitCommandManager;
import net.purelic.commons.paper.commands.parsers.CustomCommand;
import net.purelic.commons.paper.commands.parsers.PlayerArgument;
import net.purelic.commons.paper.utils.CommandUtils;
import net.purelic.commons.paper.utils.Fetcher;
import net.purelic.commons.paper.utils.VersionUtils;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class VersionCommand implements CustomCommand {

    public static final Map<String, UUID> BANNED = new HashMap<>();

    @Override //TODO version list? viaver list
    public Command.Builder<CommandSender> getCommandBuilder(BukkitCommandManager<CommandSender> mgr) {
        return mgr.commandBuilder("version", "ver")
            .senderType(Player.class)
            .argument(PlayerArgument.of("player"))
            .handler(c -> {
                Player player = (Player) c.getSender();
                Player target = c.get("player");

                VersionUtils.Protocol protocol = VersionUtils.getProtocol(target);

                if (player == target) {
                    CommandUtils.sendAlertMessage(player,
                        "You are currently playing on Minecraft version " + ChatColor.AQUA +
                            protocol.getFullLabel() + ChatColor.GRAY + " (" + protocol.value() + ")");
                } else {
                    CommandUtils.sendAlertMessage(player,
                        Fetcher.getBasicName(target) + " is currently playing on Minecraft version " +
                            ChatColor.AQUA + protocol.getFullLabel() + ChatColor.GRAY + " (" + protocol.value() + ")");
                }
            });
    }

}
