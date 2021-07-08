package net.purelic.commons.paper.utils;

import org.apache.commons.lang.WordUtils;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;

public class YamlUtils {

    public static String locationToString(Location loc) {
        return
            (loc.getBlockX() + 0.5) + "," +
            loc.getY() + "," +
            (loc.getBlockZ() + 0.5) + "," +
            roundYaw(loc);
    }

    public static String locationToCoords(Location loc, boolean yaw) {
        return
            loc.getBlockX() + "," +
            loc.getBlockY() + "," +
            loc.getBlockZ() +
            (yaw ? "," + roundYaw(loc) : "");
    }

    public static String formatCoords(String coords, boolean comma, boolean compact) {
        if (compact) {
            String[] args = coords.split(",");
            return args[0] + (comma ? ", ": " ") + args[2];
        } else {
            return String.join(comma ? ", " : " ", coords.split(","));
        }
    }

    public static void teleportToCoords(Player player, String coords) {
        teleportToCoords(player, player.getWorld(), coords);
    }

    public static void teleportToCoords(Player player, World world, String coords) {
        player.teleport(getLocationFromCoords(world, coords));
    }

    public static Location getLocationFromCoords(Player player, String coords) {
        return getLocationFromCoords(player.getWorld(), coords);
    }

    public static Location getLocationFromCoords(World world, String coords) {
        String[] args = coords.split(",");
        return new Location(
                world,
                Double.parseDouble(args[0]),
                Double.parseDouble(args[1]),
                Double.parseDouble(args[2]),
                args.length == 4 ? Float.parseFloat(args[3]) : 0,
                0);
    }

    public static String formatEnumString(String value) {
        return WordUtils.capitalizeFully(value.replaceAll("_", " "));
    }

    public static int roundYaw(Location loc) {
        return roundYaw(loc, 15);
    }

    public static int roundYaw(Location loc, double nearest) {
        return (int) (Math.round(loc.getYaw() / nearest) * nearest);
    }

    public static String toKey(String value) {
        return value.toLowerCase().replaceAll(" ", "_").replaceAll("\\.", "");
    }

}
