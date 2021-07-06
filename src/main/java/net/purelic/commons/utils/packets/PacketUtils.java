package net.purelic.commons.utils.packets;

import com.mojang.authlib.GameProfile;
import com.mojang.authlib.properties.Property;
import net.minecraft.server.v1_8_R3.*;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_8_R3.CraftServer;
import org.bukkit.craftbukkit.v1_8_R3.CraftWorld;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.entity.Player;

import java.util.UUID;

public class PacketUtils {

    public static void sendPackets(Player player, Packet<?>... packets) {
        PlayerConnection connection = ((CraftPlayer) player).getHandle().playerConnection;
        for (Packet<?> packet : packets) connection.sendPacket(packet);
    }

    public static EntityPlayer getEntityPlayer(Location location, GameProfile gameProfile) {
        WorldServer nmsWorld = ((CraftWorld) location.getWorld()).getHandle();

        return new EntityPlayer(
            ((CraftServer) Bukkit.getServer()).getServer(),
            nmsWorld,
            gameProfile,
            new PlayerInteractManager(nmsWorld)
        );
    }

    public static GameProfile getGameProfile(UUID uuid, String name, String skin, String signature) {
        return getGameProfile(uuid, name, new Property("textures", skin, signature));
    }

    public static GameProfile getGameProfile(UUID uuid, String name, Player player) {
        Property property = ((CraftPlayer) player).getHandle().getProfile().getProperties().get("textures").iterator().next();
        return getGameProfile(uuid, name, new Property("textures", property.getValue(), property.getSignature()));
    }

    public static GameProfile getGameProfile(UUID uuid, String name, Property skin) {
        GameProfile profile = new GameProfile(uuid, name);
        profile.getProperties().put("textures", skin);
        return profile;
    }

    public static EntityArmorStand getArmorStand(Location location) {
        WorldServer s = ((CraftWorld) location.getWorld()).getHandle();
        EntityArmorStand stand = new EntityArmorStand(s);

        stand.setLocation(location.getX(), location.getY(), location.getZ(), 0, 0);
        stand.setGravity(false);
        stand.setInvisible(true);
        stand.setBasePlate(false);
        stand.setArms(false);
        stand.setSmall(true);
        stand.n(true); // marker

        return stand;
    }

}
