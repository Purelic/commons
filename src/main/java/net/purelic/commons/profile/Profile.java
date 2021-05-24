package net.purelic.commons.profile;

import com.google.cloud.Timestamp;
import com.google.cloud.firestore.annotation.PropertyName;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import net.purelic.commons.utils.CommandUtils;
import net.purelic.commons.utils.DatabaseUtils;
import net.purelic.commons.utils.NickUtils;
import net.purelic.commons.utils.ServerUtils;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.*;

public class Profile {

    private static final Long STARTING_ELO = 250L;

    private UUID uuid;
    private Timestamp joined;
    private Timestamp lastSeen;
    private List<Rank> ranks;
    private Map<String, Object> stats;
    private List<Map<String, Object>> recentMatches;
    private int rating;
    private final Map<Preference, Object> preferences;
    private String sessionId;
    private String nick;
    private boolean discordLinked;
    private String name;
    private String nameLower;
    private long timePlayed;

    public Profile(Timestamp now) {
        this.joined = now;
        this.lastSeen = now;
        this.ranks = new ArrayList<>();
        this.preferences = new HashMap<>();
        this.stats = new HashMap<>();
        this.recentMatches = new ArrayList<>();
        this.rating = STARTING_ELO.intValue();
        this.sessionId = null;
        this.nick = null;
        this.discordLinked = false;
        this.name = null;
        this.nameLower = null;
        this.timePlayed = 0L;
    }

    public UUID getUniqueId() {
        return uuid;
    }

    public void setUniqueId(UUID uuid) {
        this.uuid = uuid;
    }

    public Player getPlayer() {
        return Bukkit.getPlayer(this.uuid);
    }

    public Map<String, Object> getPreferences() {
        Map<String, Object> preferences = new HashMap<>();

        for (Map.Entry<Preference, Object> entry : this.preferences.entrySet()) {
            preferences.put(entry.getKey().name().toLowerCase(), entry.getValue());
        }

        return preferences;
    }

    public void setPreferences(Map<String, Object> preferences) {
        for (Preference preference : Preference.values()) {
            this.preferences.put(
                preference,
                preferences.get(preference.name().toLowerCase()));
        }
    }

    public void updatePreference(Preference preference, Object value) {
        this.preferences.put(preference, value);
        DatabaseUtils.update(this, "preferences." + preference.name().toLowerCase(), value);
    }

    public Object getPreference(Preference preference) {
        return this.preferences.get(preference);
    }

    public Object getPreference(Preference preference, Object defaultValue) {
        if (!this.preferences.containsKey(preference) || this.preferences.get(preference) == null) return defaultValue;
        else return this.preferences.get(preference);
    }

    public List<String> getRanks() {
        List<String> ranks = new ArrayList<>();

        for (Rank rank : this.ranks) {
            ranks.add(rank.getName(false));
        }

        return ranks;
    }

    public void setRanks(List<String> ranks) {
        for (Rank rank : Rank.values()) {
            if (ranks.contains(rank.getName(false))) {
                this.ranks.add(rank);
            }
        }
    }

    public boolean hasRank(Rank... ranks) {
        for (Rank rank : ranks) {
            if (this.ranks.contains(rank)) return true;
        }

        return false;
    }

    public boolean isStaff() {
        return this.hasRank(Rank.ADMIN, Rank.MODERATOR, Rank.HELPER, Rank.DEVELOPER, Rank.MAP_DEVELOPER);
    }

    public boolean isMod() {
        return this.hasRank(Rank.ADMIN, Rank.MODERATOR, Rank.HELPER, Rank.MAP_DEVELOPER);
    }

    public boolean isDonator() {
        return this.isDonator(false);
    }

    public boolean isDonator(boolean includeStaff) {
        return this.hasRank(Rank.ADMIN, Rank.DEVELOPER, Rank.PREMIUM, Rank.CREATOR) || (includeStaff && this.isStaff());
    }

    public boolean isAdmin() {
        return this.hasRank(Rank.ADMIN, Rank.DEVELOPER);
    }

    public boolean isMapDev() {
        return this.hasRank(Rank.ADMIN, Rank.DEVELOPER, Rank.MAP_DEVELOPER);
    }

    public void addRank(Rank rank) {
        if (!this.ranks.contains(rank)) {
            this.ranks.add(rank);
            this.updateRanks();
            DatabaseUtils.arrayUnion(this, "ranks", rank.getName(false));
        }
    }

    public void removeRank(Rank rank) {
        if (this.ranks.contains(rank)) {
            this.ranks.remove(rank);
            this.updateRanks();
            DatabaseUtils.arrayRemove(this, "ranks", rank.getName(false));
        }
    }

    private void updateRanks() {
        List<Rank> ordered = new ArrayList<>();

        for (Rank rank : Rank.values()) {
            if (this.ranks.contains(rank)) {
                ordered.add(rank);
            }
        }

        this.ranks = ordered;
    }

    public String getFlairs() {
        return this.getFlairs(false);
    }

    public String getFlairs(boolean truncate) {
        return this.getFlairs(truncate, false);
    }

    public String getFlairs(boolean truncate, boolean force) {
        StringBuilder flairs = new StringBuilder();

        int i = 0;

        if (ServerUtils.isRanked()) {
            flairs.append(this.getLeagueRank().getFlair());
            i++;
        } else if (CommandUtils.isOp(this.getPlayer())) {
            flairs.append(Rank.getOperatorFlair());
            i++;
        }

        if (!this.isNicked() || force) {
            for (Rank rank : this.ranks) {
                if (i == 4 & truncate) break;
                flairs.append(rank.getFlair());
                i++;
            }
        }

        return flairs.toString();
    }

    public TextComponent getFancyFlairs() {
        TextComponent flairs = new TextComponent("");

        if (ServerUtils.isRanked()) {
            flairs.addExtra(this.getLeagueRank().getFancyFlair());
        } else if (CommandUtils.isOp(this.getPlayer())) {
            flairs.addExtra(Rank.getFancyOperatorFlair());
        }

        if (!this.isNicked()) {
            for (Rank rank : this.ranks) {
                flairs.addExtra(rank.getFancyFlair());
            }
        }

        return flairs;
    }

    @Deprecated
    public TextComponent getFancyName(boolean withFlairs) {
        return this.getFancyName(withFlairs, "Click to PM", "/msg %PLAYER% ");
    }

    @Deprecated
    public TextComponent getFancyName(boolean withFlairs, String hover, String click) {
        Player player = this.getPlayer();
        TextComponent name = new TextComponent("");

        if (withFlairs) {
            name.addExtra(this.getFancyFlairs());
        }

        name.addExtra(NickUtils.getDisplayName(player));
        name.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder(hover).create()));
        name.setClickEvent(new ClickEvent(ClickEvent.Action.SUGGEST_COMMAND, click.replace("%PLAYER%", NickUtils.getNick(player))));

        return name;
    }

    public void setJoined(Timestamp joined) {
        this.joined = joined;
    }

    public Timestamp getJoined() {
        return this.joined;
    }

    @PropertyName("last_seen")
    public void setLastSeen(Timestamp lastSeen) {
        this.lastSeen = lastSeen;
    }

    @PropertyName("last_seen")
    public Timestamp getLastSeen() {
        return this.lastSeen;
    }

    public String getPartyId() {
        return DatabaseUtils.getPartyId(this.uuid);
    }

    @PropertyName("stats")
    public void setStats(Map<String, Object> stats) {
        this.stats = stats;

        Map<String, Object> rankedStats = (Map<String, Object>) stats.getOrDefault("ranked", new HashMap<>());
        Map<String, Object> seasonStats = (Map<String, Object>) rankedStats.getOrDefault(ServerUtils.getRankedSeason(), new HashMap<>());
        Map<String, Object> playlistStats = (Map<String, Object>) seasonStats.getOrDefault(ServerUtils.getPlaylistId(), new HashMap<>());
        Long rating = (Long) playlistStats.getOrDefault("rating", STARTING_ELO);

        this.rating = rating.intValue();
    }

    @PropertyName("stats")
    public Map<String, Object> getStats() {
        return this.stats;
    }

    public int getRating() {
        return this.rating;
    }

    public void setRating(int rating) {
        this.rating = rating;
    }

    public Rank getLeagueRank() {
        if (this.rating < 500) return Rank.IRON;
        else if (this.rating < 1000) return Rank.GOLD;
        else if (this.rating < 1500) return Rank.DIAMOND;
        else if (this.rating < 2000) return Rank.EMERALD;
        else return Rank.QUARTZ;
    }

    @PropertyName("recent_matches")
    public void setRecentMatches(List<Map<String, Object>> matches) {
        this.recentMatches = matches;
    }

    @PropertyName("recent_matches")
    public List<Map<String, Object>> getRecentMatches() {
        return this.recentMatches;
    }

    public String getSessionId() {
        return this.sessionId;
    }

    public void setSessionId(String sessionId) {
        this.sessionId = sessionId;
    }

    public void setNick(String nick) {
        this.nick = nick;
    }

    public void updateNick(String nick) {
        DatabaseUtils.update(this, "nick", nick);
    }

    public String getNick() {
        return this.nick;
    }

    public boolean isNicked() {
        return this.nick != null;
    }

    @PropertyName("discord_linked")
    public void setDiscordLinked(boolean discordLinked) {
        this.discordLinked = discordLinked;
    }

    @PropertyName("discord_linked")
    public boolean isDiscordLinked() {
        return this.discordLinked;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return this.name;
    }

    @PropertyName("name_lower")
    public void setNameLower(String nameLower) {
        this.nameLower = nameLower;
    }

    @PropertyName("name_lower")
    public String getNameLower() {
        return this.nameLower;
    }

    @PropertyName("time_played")
    public void setTimePlayed(long timePlayed) {
        this.timePlayed = timePlayed;
    }

    @PropertyName("time_played")
    public long getTimePlayed() {
        return this.timePlayed;
    }

}
