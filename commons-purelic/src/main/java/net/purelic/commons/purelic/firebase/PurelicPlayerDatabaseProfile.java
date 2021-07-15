package net.purelic.commons.purelic.firebase;

import net.purelic.commons.purelic.api.profile.PlayerDatabaseProfile;
import net.purelic.commons.purelic.api.profile.PlayerPreferences;
import net.purelic.commons.purelic.api.profile.PlayerStatistics;
import net.purelic.commons.purelic.api.utils.Rank;
import org.jetbrains.annotations.Nullable;

import java.util.Date;
import java.util.List;
import java.util.UUID;

public class PurelicPlayerDatabaseProfile implements PlayerDatabaseProfile {

    private final UUID uuid;
    private final String username;
    private final PlayerPreferences preferences;
    private final PlayerStatistics statistics;
    private final List<Rank> ranks;
    private int rating; //TODO default value?
    private Date lastSeen;
    private boolean discordLinked;

    public PurelicPlayerDatabaseProfile(UUID uuid, String username, PlayerPreferences preferences, PlayerStatistics statistics, List<Rank> ranks) {
        this.uuid = uuid;
        this.username = username;
        this.preferences = preferences;
        this.statistics = statistics;
        this.ranks = ranks;
    }

    @Override
    public UUID getId() {
        return this.uuid;
    }

    @Override
    public String getUsername() {
        return this.username;
    }

    @Override
    public @Nullable String getNickname() {
        return null;
    }

    @Override
    public PlayerPreferences getPreferences() {
        return this.preferences;
    }

    @Override
    public PlayerStatistics getStats() {
        return this.statistics;
    }

    @Override
    public List<Rank> getRanks() {
        return this.ranks;
    }

    @Override
    public int getRating() {
        return this.rating;
    }

    @Override
    public Date getWhenLastSeen() {
        return this.lastSeen;
    }

    @Override
    public boolean hasDiscordLinked() {
        return this.discordLinked;
    }
}
