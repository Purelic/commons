package net.purelic.commons.profile.preferences.effects;

import net.purelic.commons.utils.packets.PacketUtils;
import org.bukkit.Location;
import org.bukkit.Material;

public class BlackFridayEffect implements CustomEffect {

    @Override
    public void play(Location location) {
        PacketUtils.itemCrack(location, Material.OBSIDIAN);
        PacketUtils.itemCrack(location, Material.DOUBLE_PLANT);
    }

}
