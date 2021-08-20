package net.purelic.commons.profile.preferences.effects;

import org.bukkit.Effect;
import org.bukkit.Location;

public class HeartEffect implements CustomEffect {

    @Override
    public void play(Location location) {
        CustomEffect.effect(location, Effect.HEART, 0.5F, 0.1F, 10);
    }

}
