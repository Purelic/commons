package net.purelic.commons.utils;

import net.purelic.commons.Commons;
import net.purelic.commons.profile.Profile;
import org.bukkit.entity.Player;
import org.bukkit.permissions.PermissionAttachment;

import java.util.HashMap;
import java.util.UUID;

public class PermissionUtils {

    private static final HashMap<UUID, PermissionAttachment> PERMISSIONS = new HashMap<>();

    private static final String[] STAFF_PERMS = new String[] {
        "spartan.*"
    };

    private static final String[] OP_PERMS = new String[] {
        "worldedit.*",
        "fawe.cancel",
        "fawe.voxelbrush",
        "minecraft.command.give",
        "minecraft.command.summon",
        "minecraft.command.tp",
        "minecraft.command.gamemode",
        "minecraft.command.gamerule",
    };

    public static void setPermissions(Player player) {
        Profile profile = Commons.getProfile(player);

        if (profile.isStaff()) addPermissions(player, STAFF_PERMS);
        else removePermissions(player, STAFF_PERMS);

        if (CommandUtils.isOp(player)) addPermissions(player, OP_PERMS);
        else removePermissions(player, OP_PERMS);
    }

    private static void addPermissions(Player player, String[] permissions) {
        for (String permission : permissions) addPermission(player, permission);
    }

    private static void addPermission(Player player, String permission) {
        UUID uuid = player.getUniqueId();

        if (!PERMISSIONS.containsKey(uuid)) {
            PermissionAttachment attachment = player.addAttachment(Commons.getPlugin());
            PERMISSIONS.put(uuid, attachment);
        }

        PERMISSIONS.get(uuid).setPermission(permission, true);
    }

    public static void removeAllPermissions(Player player) {
        UUID uuid = player.getUniqueId();

        if (!PERMISSIONS.containsKey(uuid)) return;

        removePermissions(uuid, STAFF_PERMS);
        removePermissions(uuid, OP_PERMS);
        PERMISSIONS.remove(uuid);
    }

    private static void removePermissions(Player player, String[] permissions) {
        removePermissions(player.getUniqueId(), permissions);
    }

    private static void removePermissions(UUID uuid, String[] permissions) {
        if (!PERMISSIONS.containsKey(uuid)) return;
        for (String permission : permissions) removePermission(uuid, permission);
    }

    private static void removePermission(UUID uuid, String permission) {
        PERMISSIONS.get(uuid).unsetPermission(permission);
    }

}
