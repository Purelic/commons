package net.purelic.commons.profile.preferences.effects;

import org.bukkit.Effect;
import org.bukkit.Location;

public class AngryEffect implements CustomEffect {

    @Override
    public void play(Location location) {
        CustomEffect.effect(location, Effect.VILLAGER_THUNDERCLOUD, 0.5F, 0.1F, 10);
    }

}
