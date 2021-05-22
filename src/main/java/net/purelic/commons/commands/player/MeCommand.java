package net.purelic.commons.commands.player;

import cloud.commandframework.Command;
import cloud.commandframework.arguments.standard.StringArgument;
import cloud.commandframework.bukkit.BukkitCommandManager;
import net.md_5.bungee.api.chat.TextComponent;
import net.purelic.commons.commands.parsers.CustomCommand;
import net.purelic.commons.commands.parsers.Permission;
import net.purelic.commons.utils.Fetcher;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class MeCommand implements CustomCommand {

    @Override
    public Command.Builder<CommandSender> getCommandBuilder(BukkitCommandManager<CommandSender> mgr) {
        return mgr.commandBuilder("me")
            .senderType(Player.class)
            .argument(StringArgument.greedy("message"))
            .handler(c -> {
                if (Permission.notPremium(c)) return;

                Player player = (Player) c.getSender();
                String message = c.get("message");

                Bukkit.broadcast(
                    new TextComponent(" * "),
                    Fetcher.getFancyName(player),
                    new TextComponent(" " + message)
                );
            });
    }

}
