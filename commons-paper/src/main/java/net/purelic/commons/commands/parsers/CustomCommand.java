package net.purelic.commons.commands.parsers;

import cloud.commandframework.Command;
import cloud.commandframework.bukkit.BukkitCommandManager;
import org.bukkit.command.CommandSender;

public interface CustomCommand {

    Command.Builder<CommandSender> getCommandBuilder(BukkitCommandManager<CommandSender> commandManager);

}
