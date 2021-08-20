package net.purelic.commons.utils;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import net.purelic.commons.Commons;
import net.purelic.commons.profile.Profile;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.UUID;

public class Fetcher {

    public static final UUID PURELIC_UUID = UUID.fromString("57014d5f-1d26-4986-832b-a0e7a4e41088");

    private static final Map<UUID, String> CACHED_NAMES = new HashMap<>();
    private static final Map<String, String> CACHED_NAMES_KEYS = new HashMap<>();
    private static final Map<String, UUID> CACHED_UUIDS = new HashMap<>();

    private static final Map<String, MinecraftUser> CACHED_MC_NAMES = new HashMap<>();
    private static final Map<UUID, MinecraftUser> CACHED_MC_UUIDS = new HashMap<>();

    private static JsonArray nicks;

    public static String getNameOf(UUID uuid) {
        if (CACHED_NAMES.containsKey(uuid)) return CACHED_NAMES.get(uuid);

        Player online = Bukkit.getPlayer(uuid);
        if (online != null) {
            String name = online.getName();
            cache(uuid, name);
            return name;
        }

        Map<String, Object> offlineProfile = DatabaseUtils.getOfflineProfile(uuid);
        if (offlineProfile != null) {
            String name = (String) offlineProfile.get("name");
            cache(uuid, name);
            return name;
        }

        try {
            MinecraftUser user = getMinecraftUser(uuid);
            String name = user.getName();
            cacheMinecraftUser(user);
            cache(user.getId(), name);
            return name;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static String getNameOf(String name) {
        if (CACHED_NAMES_KEYS.containsKey(name.toLowerCase())) return CACHED_NAMES_KEYS.get(name.toLowerCase());
        return CACHED_NAMES.get(getUUIDOf(name));
    }

    public static String getNameOf(OfflinePlayer offlinePlayer) {
        return getNameOf(offlinePlayer.getUniqueId());
    }

    private static void cache(UUID uuid, String name) {
        CACHED_NAMES.put(uuid, name);
        CACHED_NAMES_KEYS.put(name.toLowerCase(), name);
        CACHED_UUIDS.put(name, uuid);
    }

    public static UUID getUUIDOf(String name) {
        if (CACHED_NAMES_KEYS.containsKey(name.toLowerCase()))
            return CACHED_UUIDS.get(CACHED_NAMES_KEYS.get(name.toLowerCase()));

        Player online = Bukkit.getPlayer(name);
        if (online != null) {
            UUID uuid = online.getUniqueId();
            String formattedName = online.getName();
            cache(uuid, formattedName);
            return uuid;
        }

        Map<String, Object> offlineProfile = DatabaseUtils.getOfflineProfile(name);
        if (offlineProfile != null) {
            String formattedName = (String) offlineProfile.get("name");
            UUID uuid = UUID.fromString((String) offlineProfile.get("uuid"));
            cache(uuid, formattedName);
            return uuid;
        }

        try {
            MinecraftUser user = getMinecraftUser(name);
            UUID uuid = user.getId();
            cacheMinecraftUser(user);
            cache(uuid, user.getName());
            return uuid;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static TextComponent getFancyName(Profile profile) {
        return getFancyName(profile.getUniqueId());
    }

    public static TextComponent getFancyName(Player player) {
        return getFancyName(player.getUniqueId());
    }

    public static TextComponent getFancyName(String uuid) {
        return getFancyName(UUID.fromString(uuid));
    }

    public static TextComponent getFancyName(Player player, Player viewer) {
        TextComponent name = getFancyName(player.getUniqueId());

        if (NickUtils.canSeeRealName(player, viewer) && NickUtils.isNicked(player)) {
            // text is actually empty here, so this prepends a struck out name before the nick
            name.setText("" + ChatColor.GRAY + ChatColor.STRIKETHROUGH +
                NickUtils.getRealName(player) + " ");
        }

        return name;
    }

    @SuppressWarnings("deprecation")
    public static TextComponent getFancyName(UUID uuid) {
        Player online = Bukkit.getPlayer(uuid);
        Profile profile = Commons.getProfile(uuid);

        if (profile == null) {
            return new TextComponent(ChatColor.DARK_AQUA + getNameOf(uuid) + ChatColor.RESET);
        }

        if (online == null || !online.isOnline()) {
            ComponentBuilder hover = new ComponentBuilder("Last Seen: ")
                .color(ChatColor.GRAY)
                .append(ChatUtils.format(profile.getLastSeen().toDate()))
                .color(ChatColor.DARK_AQUA);

            TextComponent component = profile.getFancyFlairs();
            component.addExtra(ChatColor.DARK_AQUA + getNameOf(uuid));
            component.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, hover.create()));

            return component;
        }

        return profile.getFancyName(true);
    }

    public static String getBasicName(Profile profile) {
        return getBasicName(profile.getUniqueId());
    }

    public static String getBasicName(Player player) {
        return getBasicName(player.getUniqueId());
    }

    public static String getBasicName(String uuid) {
        return getBasicName(UUID.fromString(uuid));
    }

    public static String getBasicName(UUID uuid) {
        Player online = Bukkit.getPlayer(uuid);
        Profile profile = Commons.getProfile(uuid);

        if (profile == null) {
            return ChatColor.DARK_AQUA + getNameOf(uuid) + ChatColor.RESET;
        }

        if (online == null || !online.isOnline()) {
            return profile.getFlairs() + ChatColor.DARK_AQUA + getNameOf(uuid) + ChatColor.RESET;
        }

        return profile.getFlairs() + NickUtils.getDisplayName(online) + ChatColor.RESET;
    }

    public static JsonObject getMinecraftUserObject(String identifier) throws IOException {
        URL url = new URL("https://api.ashcon.app/mojang/v2/user/" + identifier);
        InputStreamReader reader = new InputStreamReader(url.openStream());
        return new JsonParser().parse(reader).getAsJsonObject();
    }

    public static void cacheMinecraftUser(MinecraftUser user) {
        CACHED_MC_NAMES.put(user.getName(), user);
        CACHED_MC_UUIDS.put(user.getId(), user);
        cache(user.getId(), user.getName());
    }

    public static MinecraftUser getMinecraftUser(String name) throws IOException {
        if (CACHED_MC_NAMES.containsKey(name)) return CACHED_MC_NAMES.get(name);
        else return new MinecraftUser(name);
    }

    public static MinecraftUser getMinecraftUser(UUID uuid) throws IOException {
        if (CACHED_MC_UUIDS.containsKey(uuid)) return CACHED_MC_UUIDS.get(uuid);
        else return new MinecraftUser(uuid);
    }

    public static void cacheNicks() {
        try {
            nicks = new JsonParser().parse(new FileReader(
                Commons.getPlugin().getDataFolder() + "/nicks.json")).getAsJsonArray();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    public static UUID getRandomId() {
        if (nicks == null) return null;
        return UUID.fromString(nicks.get(new Random().nextInt(nicks.size())).getAsString());
    }

}
