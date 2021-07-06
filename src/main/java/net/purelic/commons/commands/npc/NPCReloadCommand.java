package net.purelic.commons.commands.npc;

import cloud.commandframework.Command;
import cloud.commandframework.bukkit.BukkitCommandManager;
import net.purelic.commons.commands.parsers.CustomCommand;
import net.purelic.commons.commands.parsers.Permission;
import net.purelic.commons.modules.NPCModule;
import net.purelic.commons.utils.TaskUtils;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class NPCReloadCommand implements CustomCommand {

    @Override
    public Command.Builder<CommandSender> getCommandBuilder(BukkitCommandManager<CommandSender> mgr) {
        return mgr.commandBuilder("npc")
            .senderType(Player.class)
            .permission(Permission.isAdmin())
            .literal("reload")
            .handler(c -> TaskUtils.run(NPCModule::reload));
    }

}
