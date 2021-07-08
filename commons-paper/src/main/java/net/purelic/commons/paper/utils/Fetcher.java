package net.purelic.commons.paper.utils;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import net.purelic.commons.paper.Commons;
import net.purelic.commons.paper.profile.Profile;
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
            CACHED_NAMES.put(uuid, name);
            CACHED_NAMES_KEYS.put(name.toLowerCase(), name);
            CACHED_UUIDS.put(name, uuid);
            return name;
        }

        Map<String, Object> offlineProfile = DatabaseUtils.getOfflineProfile(uuid);
        if (offlineProfile == null) return null;

        String name = (String) offlineProfile.get("name");
        CACHED_NAMES.put(uuid, name);
        CACHED_NAMES_KEYS.put(name.toLowerCase(), name);
        CACHED_UUIDS.put(name, uuid);
        return name;
    }

    public static String getNameOf(String name) {
        if (CACHED_NAMES_KEYS.containsKey(name.toLowerCase())) return CACHED_NAMES_KEYS.get(name.toLowerCase());
        return CACHED_NAMES.get(getUUIDOf(name));
    }

    public static String getNameOf(OfflinePlayer offlinePlayer) {
        return getNameOf(offlinePlayer.getUniqueId());
    }

    public static UUID getUUIDOf(String name) {
        if (CACHED_NAMES_KEYS.containsKey(name.toLowerCase()))
            return CACHED_UUIDS.get(CACHED_NAMES_KEYS.get(name.toLowerCase()));

        Player online = Bukkit.getPlayer(name);
        if (online != null) {
            UUID uuid = online.getUniqueId();
            String formattedName = online.getName();
            CACHED_NAMES.put(uuid, formattedName);
            CACHED_NAMES_KEYS.put(formattedName.toLowerCase(), formattedName);
            CACHED_UUIDS.put(formattedName, uuid);
            return uuid;
        }

        Map<String, Object> offlineProfile = DatabaseUtils.getOfflineProfile(name);
        if (offlineProfile == null) return null;

        String formattedName = (String) offlineProfile.get("name");
        UUID uuid = UUID.fromString((String) offlineProfile.get("uuid"));
        CACHED_NAMES.put(uuid, formattedName);
        CACHED_NAMES_KEYS.put(formattedName.toLowerCase(), formattedName);
        CACHED_UUIDS.put(formattedName, uuid);
        return uuid;
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

        if (Commons.getProfile(viewer).isStaff() && NickUtils.isNicked(player)) {
            // text is actually empty here, so this prepends a struck out name before the nick
            name.setText("" + ChatColor.GRAY + ChatColor.STRIKETHROUGH +
                NickUtils.getRealName(player) + " ");
        }

        return name;
    }

    @SuppressWarnings("deprecation")
    public static TextComponent getFancyName(UUID uuid) {
        Player online = Bukkit.getPlayer(uuid);

        if (online == null || !online.isOnline()) {
            Profile profile = Commons.getProfile(uuid);

            ComponentBuilder hover = new ComponentBuilder("Last Seen: ")
                .color(ChatColor.GRAY)
                .append(ChatUtils.format(profile.getLastSeen().toDate()))
                .color(ChatColor.DARK_AQUA);

            TextComponent component = profile.getFancyFlairs();
            component.addExtra(ChatColor.DARK_AQUA + getNameOf(uuid));
            component.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, hover.create()));

            return component;
        }

        return Commons.getProfile(uuid).getFancyName(true);
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

        if (online == null || !online.isOnline()) {
            Profile profile = Commons.getProfile(uuid);
            return profile.getFlairs() + ChatColor.DARK_AQUA + getNameOf(uuid) + ChatColor.RESET;
        }

        return Commons.getProfile(uuid).getFlairs() + NickUtils.getDisplayName(online) + ChatColor.RESET;
    }

    public static JsonObject getMinecraftUserObject(String identifier) throws IOException {
        URL url = new URL("https://api.ashcon.app/mojang/v2/user/" + identifier);
        InputStreamReader reader = new InputStreamReader(url.openStream());
        return new JsonParser().parse(reader).getAsJsonObject();
    }

    public static void cacheMinecraftUser(MinecraftUser user) {
        CACHED_MC_NAMES.put(user.getName(), user);
        CACHED_MC_UUIDS.put(user.getId(), user);
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
