package net.purelic.commons.api.command;

import cloud.commandframework.Command;
import cloud.commandframework.CommandManager;
import org.bukkit.command.CommandSender;

public interface CustomCommand { //TODO check how including cloud slows down build

    Command.Builder<CommandSender> getCommandBuilder(CommandManager<CommandSender> commandManager);

}
