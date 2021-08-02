package net.purelic.commons.utils;

import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.DocumentSnapshot;
import net.purelic.commons.Commons;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class ServerUtils {

    private static Map<String, Object> documentData;
    private static String serverNameCache = null;
    private static String serverIdCache = null;
    private static boolean isPrivateCached = false;
    private static boolean isPrivate = true; // default
    private static boolean isRankedCached = false;
    private static boolean isRanked = false; // default
    private static String rankedSeasonCache = null;
    private static String playlistCache = null;

    private static Object get(String field, Object defaultValue) {
        if (DatabaseUtils.serverDoc == null) return defaultValue;

        if (documentData != null) {
            return documentData.getOrDefault(field, defaultValue);
        }

        ApiFuture<DocumentSnapshot> future = DatabaseUtils.serverDoc.get();

        try {
            DocumentSnapshot document = future.get();

            if (document.exists() && document.getData() != null) {
                documentData = document.getData();
            } else {
                documentData = new HashMap<>();
            }

            return documentData.getOrDefault(field, defaultValue);
        } catch (Exception e) {
            documentData = new HashMap<>();
            return defaultValue;
        }
    }

    public static String getId() {
        if (serverIdCache != null) return serverIdCache;
        serverIdCache = (String) get("id", "");
        return serverIdCache;
    }

    public static void setId(String id) {
        serverIdCache = id;
    }

    public static String getName() {
        if (serverNameCache != null) return serverNameCache;
        serverNameCache = (String) get("name", "Unknown Server / Hub");
        return serverNameCache;
    }

    public static void setName(String name) {
        serverNameCache = name;
    }

    public static void setPrivate(boolean isPrivate) {
        isPrivateCached = true;
        ServerUtils.isPrivate = isPrivate;
    }

    public static boolean isPrivate() {
        if (isPrivateCached) return isPrivate;
        isPrivate = (boolean) get("private", isPrivate);
        isPrivateCached = true;
        return isPrivate;
    }

    public static boolean isRanked() {
        if (isRankedCached) return isRanked;
        isRanked = (boolean) get("ranked", isRanked);
        isRankedCached = true;
        return isRanked;
    }

    public static String getRankedSeason() {
        if (rankedSeasonCache != null) return rankedSeasonCache;
        rankedSeasonCache = (String) get("ranked_season", "beta");
        return rankedSeasonCache;
    }

    public static String getPlaylist() {
        if (playlistCache != null) return playlistCache;
        playlistCache = (String) get("playlist", null);
        return playlistCache;
    }

    public static String getPlaylistId() {
        String playlist = getPlaylist();
        if (playlist == null) return null;
        else return playlist.replaceAll(" ", "_").toLowerCase();
    }

    public static void setWhitelisted(boolean whitelisted) {
        if (Bukkit.hasWhitelist() == whitelisted) return;

        Bukkit.setWhitelist(whitelisted);

        if (DatabaseUtils.serverDoc != null) {
            DatabaseUtils.serverDoc.update("whitelisted", whitelisted);
        }
    }

    public static void update(Map<String, Object> data) {
        if (DatabaseUtils.serverDoc == null) return;
        DatabaseUtils.serverDoc.update(data);
    }

    public static List<Player> getOnlineStaff() {
        return Bukkit.getOnlinePlayers().stream()
            .filter(player -> Commons.getProfile(player).isStaff())
            .collect(Collectors.toList());
    }

}
