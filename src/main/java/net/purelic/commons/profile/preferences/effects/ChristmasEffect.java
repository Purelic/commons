package net.purelic.commons.profile.preferences.effects;

import net.purelic.commons.utils.packets.PacketUtils;
import org.bukkit.Location;
import org.bukkit.Material;

public class ChristmasEffect implements CustomEffect {

    @Override
    public void play(Location location) {
        PacketUtils.itemCrack(location, Material.SNOW_BLOCK, 5);
        PacketUtils.itemCrack(location, Material.APPLE, 5);
        PacketUtils.itemCrack(location, Material.WHEAT, 5);
        PacketUtils.itemCrack(location, Material.EMERALD, 5);
    }

}
