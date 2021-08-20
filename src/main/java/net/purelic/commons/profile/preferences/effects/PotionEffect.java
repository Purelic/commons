package net.purelic.commons.profile.preferences.effects;

import org.bukkit.Effect;
import org.bukkit.Location;
import org.bukkit.entity.Player;

public class PotionEffect implements CustomEffect {

    @Override
    public void play(Player player) {
        this.play(player.getLocation());
    }

    @Override
    public void play(Location location) {
        CustomEffect.effect(location, Effect.POTION_SWIRL, 0F, 0.1F, 30);
    }

}
