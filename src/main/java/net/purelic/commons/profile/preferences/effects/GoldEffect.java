package net.purelic.commons.profile.preferences.effects;

import net.purelic.commons.utils.packets.PacketUtils;
import org.bukkit.Effect;
import org.bukkit.Location;
import org.bukkit.Material;

public class GoldEffect implements CustomEffect {

    @Override
    public void play(Location location) {
        PacketUtils.itemCrack(location, Material.GOLD_INGOT, 10);
        PacketUtils.itemCrack(location, Material.GOLD_NUGGET, 10);
        CustomEffect.effect(location, Effect.FIREWORKS_SPARK, 3);
    }

}
