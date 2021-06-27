package net.purelic.commons.utils;

import net.minecraft.server.v1_8_R3.NBTTagCompound;
import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftEntity;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.EulerAngle;

public class EntityUtils {

    public static ArmorStand getInvisibleStand(Location location) {
        return getInvisibleStand(location, null, null);
    }

    public static ArmorStand getInvisibleStand(Location location, String name) {
        return getInvisibleStand(location, name, null);
    }

    public static ArmorStand getInvisibleStand(Location location, ItemStack helmet) {
        return getInvisibleStand(location, null, helmet);
    }

    public static ArmorStand getInvisibleStand(Location location, String name, ItemStack helmet) {
        ArmorStand stand = (ArmorStand) location.getWorld().spawnEntity(location, EntityType.ARMOR_STAND);

        stand.setSmall(true);
        stand.setCanPickupItems(false);
        stand.setMarker(true);
        stand.setGravity(false);
        stand.setVisible(false);
        stand.setRemoveWhenFarAway(false);
        stand.setBasePlate(false);
        stand.setLeftLegPose(new EulerAngle(Math.PI, 0.0D, 0.0D));
        stand.setRightLegPose(new EulerAngle(Math.PI, 0.0D, 0.0D));

        if (name != null) {
            stand.setCustomName(name);
            stand.setCustomNameVisible(true);
        }

        if (helmet != null) {
            stand.setHelmet(helmet);
        }

        return stand;
    }

    public static void setAi(Entity entity, boolean enabled) {
        net.minecraft.server.v1_8_R3.Entity nmsEn = ((CraftEntity) entity).getHandle();
        NBTTagCompound comp = new NBTTagCompound();
        nmsEn.c(comp);
        comp.setByte("NoAI", (byte) 1);
        nmsEn.f(comp);
        nmsEn.b(true);
    }

}
