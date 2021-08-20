package net.purelic.commons.profile.preferences.effects;

import net.purelic.commons.utils.packets.PacketUtils;
import org.bukkit.Effect;
import org.bukkit.Location;
import org.bukkit.Material;

public class SnowEffect implements CustomEffect {

    @Override
    public void play(Location location) {
        PacketUtils.itemCrack(location, Material.SNOW_BLOCK, 10);
        PacketUtils.itemCrack(location, Material.ICE, 5);
        CustomEffect.effect(location, Effect.SNOWBALL_BREAK, 10);
    }

}
