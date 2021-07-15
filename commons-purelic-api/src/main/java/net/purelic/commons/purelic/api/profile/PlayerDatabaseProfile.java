package net.purelic.commons.purelic.api.profile;

import net.purelic.commons.purelic.api.utils.Rank;
import org.jetbrains.annotations.Nullable;

import java.util.Date;
import java.util.List;
import java.util.UUID;

/**
 * A player profile stored in the database
 */
public interface PlayerDatabaseProfile {

    UUID getId();

    String getUsername();

    /**
     *
     * @return the players nickname if they are nicked, {@code null} if not
     */
    @Nullable String getNickname();

    PlayerPreferences getPreferences();

    PlayerStatistics getStats();

    List<Rank> getRanks();

    int getRating();

    Date getWhenLastSeen();

    boolean hasDiscordLinked();

}
