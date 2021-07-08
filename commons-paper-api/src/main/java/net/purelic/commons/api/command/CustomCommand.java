package net.purelic.commons.api.command;

import cloud.commandframework.Command;
import cloud.commandframework.CommandManager;
import net.purelic.commons.api.player.Purelative;

public interface CustomCommand { //TODO check how including cloud slows down build

    Command.Builder<Purelative> getCommandBuilder(CommandManager<Purelative> commandManager);

}
