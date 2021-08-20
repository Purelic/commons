package net.purelic.commons.profile.preferences.effects;

import net.purelic.commons.utils.packets.PacketUtils;
import org.bukkit.Location;
import org.bukkit.Material;

public class BloodEffect implements CustomEffect {

    @Override
    public void play(Location location) {
        PacketUtils.itemCrack(location, Material.REDSTONE, 0.2F, 0.1F, 5);
        PacketUtils.itemCrack(location, Material.REDSTONE_BLOCK, 0.2F, 0.1F, 5);
        PacketUtils.itemCrack(location, Material.REDSTONE, 0.2F, 0.1F, 5);
        PacketUtils.itemCrack(location, Material.REDSTONE_BLOCK, 0.2F, 0.1F, 5);
        PacketUtils.itemCrack(location, Material.REDSTONE, 0.2F, 0.1F, 5);
        PacketUtils.itemCrack(location, Material.REDSTONE_BLOCK, 0.2F, 0.1F, 5);
        PacketUtils.itemCrack(location, Material.REDSTONE, 0.2F, 0.1F, 5);
        PacketUtils.itemCrack(location, Material.REDSTONE_BLOCK, 0.2F, 0.1F, 5);
    }

}
