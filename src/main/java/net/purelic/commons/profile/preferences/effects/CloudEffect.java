package net.purelic.commons.profile.preferences.effects;

import org.bukkit.Effect;
import org.bukkit.Location;

public class CloudEffect implements CustomEffect {

    @Override
    public void play(Location location) {
        CustomEffect.effect(location, Effect.CLOUD, 0F, 0.1F, 10);
        CustomEffect.effect(location, Effect.SPLASH, 0F, 0.1F, 10);
    }

}
