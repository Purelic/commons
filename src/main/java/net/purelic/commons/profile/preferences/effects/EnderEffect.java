package net.purelic.commons.profile.preferences.effects;

import net.purelic.commons.utils.packets.PacketUtils;
import org.bukkit.Effect;
import org.bukkit.Location;
import org.bukkit.Material;

public class EnderEffect implements CustomEffect {

    @Override
    public void play(Location location) {
        CustomEffect.effect(location, Effect.PORTAL, 0.1F, 0.1F, 12);
        PacketUtils.itemCrack(location, Material.ENDER_STONE, 6);
    }

}
