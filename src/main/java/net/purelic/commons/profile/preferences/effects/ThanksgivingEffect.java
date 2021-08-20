package net.purelic.commons.profile.preferences.effects;

import net.purelic.commons.utils.packets.PacketUtils;
import org.bukkit.Location;
import org.bukkit.Material;

public class ThanksgivingEffect implements CustomEffect {

    @Override
    public void play(Location location) {
        PacketUtils.itemCrack(location, Material.COOKED_CHICKEN, 10);
        PacketUtils.itemCrack(location, Material.PUMPKIN_PIE, 10);
    }

}
