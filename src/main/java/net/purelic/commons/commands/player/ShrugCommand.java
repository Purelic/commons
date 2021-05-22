package net.purelic.commons.commands.player;

import cloud.commandframework.Command;
import cloud.commandframework.arguments.standard.StringArgument;
import cloud.commandframework.bukkit.BukkitCommandManager;
import net.purelic.commons.commands.parsers.CustomCommand;
import net.purelic.commons.commands.parsers.Permission;
import net.purelic.commons.utils.ChatUtils;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class ShrugCommand implements CustomCommand {

    @Override
    public Command.Builder<CommandSender> getCommandBuilder(BukkitCommandManager<CommandSender> mgr) {
        return mgr.commandBuilder("shrug")
                .senderType(Player.class)
                .argument(StringArgument.greedy("message"))
                .handler(c -> {
                    if (Permission.notPremium(c)) return;

                    Player player = (Player) c.getSender();
                    String message = c.get("message");

                    ChatUtils.sendFancyChatMessage(player, message + " ¯\\_(ツ)_/¯");
                });
    }

}
