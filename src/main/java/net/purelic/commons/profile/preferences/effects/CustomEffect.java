package net.purelic.commons.profile.preferences.effects;

import org.bukkit.Effect;
import org.bukkit.Location;
import org.bukkit.entity.Player;

public interface CustomEffect {

    default void play(Player player) {
        this.play(player.getLocation().clone().add(0, 1, 0));
    }

    void play(Location location);

    static void effect(Location location, Effect effect, int amount) {
        effect(location, effect, 0.3F, 0.1F, amount);
    }

    static void effect(Location location, Effect effect, float offset, float speed, int amount) {
        location.getWorld().spigot().playEffect(location, effect, 0, 0, offset, offset, offset, speed, amount, 64);
    }

}
