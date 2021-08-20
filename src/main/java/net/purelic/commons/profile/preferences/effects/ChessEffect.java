package net.purelic.commons.profile.preferences.effects;

import net.purelic.commons.utils.packets.PacketUtils;
import org.bukkit.Location;
import org.bukkit.Material;

public class ChessEffect implements CustomEffect {

    @Override
    public void play(Location location) {
        PacketUtils.itemCrack(location, Material.QUARTZ_BLOCK, 5);
        PacketUtils.itemCrack(location, Material.COAL_BLOCK, 5);
    }

}
