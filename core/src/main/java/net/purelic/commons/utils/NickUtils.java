package net.purelic.commons.utils;

import com.mojang.authlib.GameProfile;
import net.minecraft.server.v1_8_R3.EntityHuman;
import net.minecraft.server.v1_8_R3.PacketPlayOutEntityDestroy;
import net.minecraft.server.v1_8_R3.PacketPlayOutNamedEntitySpawn;
import net.minecraft.server.v1_8_R3.PacketPlayOutPlayerInfo;
import net.purelic.commons.Commons;
import net.purelic.commons.events.NickChangedEvent;
import net.purelic.commons.profile.Profile;
import org.bukkit.Bukkit;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.entity.Player;

import java.io.IOException;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

public class NickUtils {

    public static List<Player> getNickedPlayers() {
        return Bukkit.getOnlinePlayers().stream()
            .filter(NickUtils::isNicked)
            .collect(Collectors.toList());
    }

    public static boolean isNicked(Player player) {
        return Commons.getProfile(player).isNicked();
    }

    public static String getNick(Player player) {
        return isNicked(player) ? Commons.getProfile(player).getNick() : player.getName();
    }

    public static String getRealName(Player player) {
        return Commons.getProfile(player).getName();
    }

    public static String getDisplayName(Player player) {
        return getShownName(player, player.getDisplayName());
    }

    public static String getListName(Player player) {
        return getShownName(player, player.getPlayerListName());
    }

    private static String getShownName(Player player, String displayed) {
        return isNicked(player) ? displayed.replace(getRealName(player), getNick(player)) : displayed;
    }

    public static void setNick(Player player, String nick) {
        if (NickUtils.isNicked(player) && nick != null) {
            CommandUtils.sendErrorMessage(player, "You are already nicked! To remove your nick use /unnick");
            return;
        }

        if (!NickUtils.isNicked(player) && nick == null) {
            CommandUtils.sendErrorMessage(player, "You are not currently nicked!");
            return;
        }

        Commons.getProfile(player).updateNick(nick);
        Commons.callEvent(new NickChangedEvent(player));

        if (nick == null) {
            CommandUtils.sendSuccessMessage(player,
                "You are no longer nicked! Please relog for this to take effect");
        } else {
            CommandUtils.sendSuccessMessage(player,
                String.format("You will now be nicked as \"%s\"! Please relog for your " +
                    "nick to take effect and use /unnick to remove it", nick));
        }
    }

    public static void removeNick(Player player) {
        setNick(player, null);
    }

    public static Player getNickedPlayer(String nick) {
        // Attempt to get a player by matching their nick exactly
        for (Player player : Bukkit.getOnlinePlayers()) {
            if (getNick(player).equalsIgnoreCase(nick)) return player;
        }

        // Attempt to find a player that starts with the provided nick
        for (Player player : Bukkit.getOnlinePlayers()) {
            if (getNick(player).toLowerCase().startsWith(nick.toLowerCase())) return player;
        }

        return null;
    }

    public static List<String> filter(Collection<? extends String> names) {
        List<String> filtered = new ArrayList<>();

        for (String name : names) {
            Profile profile = Commons.getProfile(Fetcher.getUUIDOf(name));
            if (!profile.isNicked()) filtered.add(name);
        }

        return filtered;
    }

    public static List<String> getNickCompletions(String input) {
        List<String> nicks = new ArrayList<>();

        for (Player player : Bukkit.getOnlinePlayers()) {
            Profile profile = Commons.getProfile(player);
            if (profile.isNicked() && profile.getNick().toLowerCase().startsWith(input.toLowerCase())) {
                nicks.add(profile.getNick());
            }
        }

        return nicks;
    }

    public static String getRandomNick() {
        return getRandomNick(1);
    }

    private static String getRandomNick(int attempts) {
        if (attempts > 3) {
            // probably wil never happen but at most we'll attempt to get a nick 3 times
            return null;
        }

        UUID randomId = Fetcher.getRandomId();

        // Couldn't get a random uuid
        if (randomId == null) return null;

        // PuRelic player already exists
        if (Commons.getProfile(randomId) != null) {
            return getRandomNick(attempts + 1);
        }

        try {
            return Fetcher.getMinecraftUser(randomId).getName();
        } catch (IOException e) {
            // Failed to fetch mc user data
            return null;
        }
    }

    // Returns if the player was disguised
    public static boolean disguisePlayer(Player player) {
        // Return if the player is not nicked
        if (!isNicked(player)) return false;

        // Get the player's nick
        String nick = getNick(player);

        // Create the nicked game profile
        CraftPlayer craftPlayer = ((CraftPlayer) player);
        GameProfile profile = new GameProfile(player.getUniqueId(), nick);

        // Set the skin texture
        try {
            profile.getProperties().put("textures", Fetcher.getMinecraftUser(nick).getSkin());
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }

        // Create the nicked entity human
        EntityHuman entityHuman = craftPlayer.getHandle();
        craftPlayer.getHandle().playerConnection.sendPacket(new PacketPlayOutPlayerInfo(PacketPlayOutPlayerInfo.EnumPlayerInfoAction.REMOVE_PLAYER, craftPlayer.getHandle()));

        // Update display names
        player.setDisplayName(player.getDisplayName());
        player.setPlayerListName(player.getPlayerListName());

        // Reflection to replace the game profile
        try {
            Field field = entityHuman.getClass().getSuperclass().getDeclaredField("bH");
            field.setAccessible(true);
            field.set(entityHuman, profile);
            field.setAccessible(false);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }

        // Update the changes for other players
        for (Player online : Bukkit.getOnlinePlayers()) {
            CraftPlayer cp = ((CraftPlayer) online);
            cp.getHandle().playerConnection.sendPacket(new PacketPlayOutEntityDestroy(player.getEntityId()));
            cp.getHandle().playerConnection.sendPacket(new PacketPlayOutNamedEntitySpawn(craftPlayer.getHandle()));

            online.hidePlayer(player);
            online.showPlayer(player);
        }

        return true;
    }

}
