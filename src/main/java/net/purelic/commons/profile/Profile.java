package net.purelic.commons.profile;

import com.google.cloud.Timestamp;
import com.google.cloud.firestore.FieldValue;
import com.google.cloud.firestore.annotation.PropertyName;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import net.purelic.commons.profile.preferences.ChatChannel;
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
    private long mapSlots;
    private long npcEggs;
    private long gameModeSlots;
    private long relicsCurrent;
    private long relicsLifetime;
    private long relicsGifted;
    private long relicsReceived;
    private long goldCurrent;
    private long goldLifetime;
    private Timestamp premiumExpiration;
    private boolean premiumSubscribed;
    private long premiumPurchased;
    private long premiumGifted;
    private long premiumReceived;

    public Profile() {
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
        this.mapSlots = 0L;
        this.npcEggs = 0L;
        this.gameModeSlots = 0L;
        this.relicsCurrent = 0L;
        this.relicsLifetime = 0L;
        this.relicsGifted = 0L;
        this.relicsReceived = 0L;
        this.goldCurrent = 0L;
        this.goldLifetime = 0L;
        this.premiumExpiration = null;
        this.premiumSubscribed = false;
        this.premiumPurchased = 0L;
        this.premiumGifted = 0L;
        this.premiumReceived = 0L;
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

    public void setChatChannel(ChatChannel channel) {
        this.updatePreference(Preference.CHAT_CHANNEL, channel.name());
        DatabaseUtils.update(this, "preferences.chat_channel", channel.name());
    }

    public ChatChannel getChatChannel() {
        String preference = (String) this.getPreference(Preference.CHAT_CHANNEL, ChatChannel.ALL.name());

        if (ChatChannel.contains(preference)) {
            return ChatChannel.valueOf(preference.toUpperCase());
        } else {
            return ChatChannel.ALL;
        }
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

        boolean staffFlairAdded = false;
        boolean creatorFlairAdded = false;

        if (!this.isNicked() || force) {
            for (Rank rank : this.ranks) {
                if (staffFlairAdded && (rank.isStaff() || rank == Rank.PREMIUM || rank == Rank.CREATOR)) continue;
                if (creatorFlairAdded && rank == Rank.PREMIUM) continue;

                if (i == 4 & truncate) break;

                if (rank == Rank.PREMIUM && this.premiumSubscribed) {
                    flairs.append(Rank.PREMIUM_PLUS.getFlair());
                } else {
                    flairs.append(rank.getFlair());
                }

                if (rank.isStaff()) staffFlairAdded = true;
                if (rank == Rank.CREATOR) creatorFlairAdded = true;

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

        boolean staffFlairAdded = false;
        boolean creatorFlairAdded = false;

        if (!this.isNicked()) {
            for (Rank rank : this.ranks) {
                if (staffFlairAdded && (rank.isStaff() || rank == Rank.PREMIUM || rank == Rank.CREATOR)) continue;
                if (creatorFlairAdded && rank == Rank.PREMIUM) continue;

                if (rank == Rank.PREMIUM && this.premiumSubscribed) {
                    flairs.addExtra(Rank.PREMIUM_PLUS.getFancyFlair());
                } else {
                    flairs.addExtra(rank.getFancyFlair());
                }

                if (rank.isStaff()) staffFlairAdded = true;
                if (rank == Rank.CREATOR) creatorFlairAdded = true;
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
        else if (this.rating < 1000) return Rank.GOLD_LEAGUE;
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

    @PropertyName("map_slots")
    public void setMapSlots(long mapSlots) {
        this.mapSlots = mapSlots;
    }

    @PropertyName("map_slots")
    public long getMapSlots() {
        return this.mapSlots;
    }

    @PropertyName("npc_eggs")
    public void setNpcEggs(long npcEggs) {
        this.npcEggs = npcEggs;
    }

    public void addNPCEggs(int eggs) {
        this.npcEggs += eggs;
        DatabaseUtils.update(this, "npc_eggs", FieldValue.increment(eggs));
    }

    @PropertyName("npc_eggs")
    public long getNpcEggs() {
        return this.npcEggs;
    }

    public void useNPCEgg() {
        this.npcEggs--;
        DatabaseUtils.update(this, "npc_eggs", FieldValue.increment(-1));
    }

    @PropertyName("game_mode_slots")
    public void setGameModeSlots(long gameModeSlots) {
        this.gameModeSlots = gameModeSlots;
    }

    @PropertyName("game_mode_slots")
    public long getGameModeSlots() {
        return this.gameModeSlots;
    }

    @PropertyName("relics_current")
    public void setRelicsCurrent(long relicsCurrent) {
        this.relicsCurrent = relicsCurrent;
    }

    @PropertyName("relics_current")
    public long getRelicsCurrent() {
        return this.relicsCurrent;
    }

    @PropertyName("relics_lifetime")
    public void setRelicsLifetime(long relicsLifetime) {
        this.relicsLifetime = relicsLifetime;
    }

    @PropertyName("relics_lifetime")
    public long getRelicsLifetime() {
        return this.relicsLifetime;
    }

    @PropertyName("relics_gifted")
    public void setRelicsGifted(long relicsGifted) {
        this.relicsGifted = relicsGifted;
    }

    @PropertyName("relics_gifted")
    public long getRelicsGifted() {
        return this.relicsGifted;
    }

    @PropertyName("relics_received")
    public void setRelicsReceived(long relicsReceived) {
        this.relicsReceived = relicsReceived;
    }

    @PropertyName("relics_received")
    public long getRelicsReceived() {
        return this.relicsReceived;
    }

    @PropertyName("gold_current")
    public void setGoldCurrent(long goldCurrent) {
        this.goldCurrent = goldCurrent;
    }

    @PropertyName("gold_current")
    public long getGoldCurrent() {
        return this.goldCurrent;
    }

    @PropertyName("gold_lifetime")
    public void setGoldLifetime(long goldLifetime) {
        this.goldLifetime = goldLifetime;
    }

    @PropertyName("gold_lifetime")
    public long getGoldLifetime() {
        return this.goldLifetime;
    }

    @PropertyName("premium_expiration")
    public void setPremiumExpiration(Timestamp premiumExpiration) {
        this.premiumExpiration = premiumExpiration;
    }

    @PropertyName("premium_expiration")
    public Timestamp getPremiumExpiration() {
        return premiumExpiration;
    }

    @PropertyName("premium_subscribed")
    public void setPremiumSubscribed(boolean premiumSubscribed) {
        this.premiumSubscribed = premiumSubscribed;
    }

    @PropertyName("premium_subscribed")
    public boolean isPremiumSubscribed() {
        return premiumSubscribed;
    }

    @PropertyName("premium_purchased")
    public void setPremiumPurchased(long premiumPurchased) {
        this.premiumPurchased = premiumPurchased;
    }

    @PropertyName("premium_purchased")
    public long getPremiumPurchased() {
        return premiumPurchased;
    }

    @PropertyName("premium_gifted")
    public void setPremiumGifted(long premiumGifted) {
        this.premiumGifted = premiumGifted;
    }

    @PropertyName("premium_gifted")
    public long getPremiumGifted() {
        return premiumGifted;
    }

    @PropertyName("premium_received")
    public void setPremiumReceived(long premiumReceived) {
        this.premiumReceived = premiumReceived;
    }

    @PropertyName("premium_received")
    public long getPremiumReceived() {
        return this.premiumReceived;
    }

}
