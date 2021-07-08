package net.purelic.commons.utils.packets;

import net.minecraft.server.v1_8_R3.EntityArmorStand;
import net.minecraft.server.v1_8_R3.PacketPlayOutEntityDestroy;
import net.minecraft.server.v1_8_R3.PacketPlayOutSpawnEntityLiving;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.entity.Player;

public class HologramLine {

    private final EntityArmorStand stand;

    public HologramLine(Location location, String name) {
        this.stand = PacketUtils.getArmorStand(location);
        this.update(name);
    }

    public void remove() {
        Bukkit.getOnlinePlayers().forEach(this::remove);
    }

    public void remove(Player player) {
        PacketUtils.sendPackets(player, new PacketPlayOutEntityDestroy(this.stand.getId()));
    }

    public void show() {
        Bukkit.getOnlinePlayers().forEach(this::show);
    }

    public void show(Player player) {
        PacketUtils.sendPackets(player, new PacketPlayOutSpawnEntityLiving(this.stand));
    }

    public void update(String name) {
        if (name == null || name.isEmpty()) {
            this.stand.setCustomNameVisible(false);
        } else if (!name.equals(this.stand.getCustomName())) {
            this.stand.setCustomName(ChatColor.translateAlternateColorCodes('&', name));
            if (!this.stand.getCustomNameVisible()) this.stand.setCustomNameVisible(true);
        }
    }

}