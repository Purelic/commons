package net.purelic.commons.paper.utils;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

public class PlayerUtils {

    public static void reset(Player player) {
        player.setMaxHealth(20);
        player.setHealth(20);
        player.setFoodLevel(20);
        player.setSaturation(5);
        player.setFlying(false);
        player.setAllowFlight(false);
        player.setSneaking(false);
        player.setSprinting(false);
        player.setFireTicks(0);
        player.setExp(0);
        player.setLevel(0);
        player.setExhaustion(0);
        player.setFallDistance(0);
    }

    public static void performCommand(final Player player, final String command) {
        TaskUtils.run(() -> Bukkit.dispatchCommand(player, command));
    }

}
