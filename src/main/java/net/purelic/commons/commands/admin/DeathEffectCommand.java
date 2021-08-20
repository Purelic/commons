package net.purelic.commons.commands.admin;

import cloud.commandframework.Command;
import cloud.commandframework.arguments.standard.EnumArgument;
import cloud.commandframework.bukkit.BukkitCommandManager;
import net.purelic.commons.commands.parsers.CustomCommand;
import net.purelic.commons.commands.parsers.Permission;
import net.purelic.commons.profile.preferences.DeathEffect;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class DeathEffectCommand implements CustomCommand {

    @Override
    public Command.Builder<CommandSender> getCommandBuilder(BukkitCommandManager<CommandSender> mgr) {
        return mgr.commandBuilder("deatheffect")
            .senderType(Player.class)
            .permission(Permission.isAdmin())
            .argument(EnumArgument.of(DeathEffect.class, "effect"))
            .handler(context -> mgr.taskRecipe().begin(context).synchronous(c -> {
                Player player = (Player) c.getSender();
                DeathEffect effect = c.get("effect");
                effect.play(player);
            }).execute());
    }

}
