package net.purelic.commons.commands.op;

import cloud.commandframework.Command;
import cloud.commandframework.bukkit.BukkitCommandManager;
import net.purelic.commons.commands.parsers.CustomCommand;
import net.purelic.commons.commands.parsers.OfflinePlayerArgument;
import net.purelic.commons.commands.parsers.Permission;
import net.purelic.commons.utils.CommandUtils;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class UnbanCommand implements CustomCommand {

    @Override
    public Command.Builder<CommandSender> getCommandBuilder(BukkitCommandManager<CommandSender> mgr) {
        return mgr.commandBuilder("unban")
            .senderType(Player.class)
            .permission(Permission.isMod(true))
            .argument(OfflinePlayerArgument.of("player"))
            .handler(c -> {
                Player player = (Player) c.getSender();
                OfflinePlayer targetArg = c.get("player");

                if (BanCommand.BANNED.remove(targetArg.getName().toLowerCase()) == null) {
                    CommandUtils.sendErrorMessage(player, "Could not find banned player \"" + targetArg.getName() + "\"!");
                } else {
                    CommandUtils.sendSuccessMessage(player, "Successfully unbanned player!");
                }
            });
    }

}
