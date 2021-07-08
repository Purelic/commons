package net.purelic.commons.purelic.api.profile;

import java.util.Date;
import java.util.List;
import java.util.UUID;

/**
 * A player profile stored in the database
 */
public interface PlayerDatabaseProfile {

    UUID getId();

    String getUsername();

    PlayerPreferences getPreferences();

    PlayerStatistics getStats();

    List<Rank> getRanks();

    int getRating();

    Date getWhenLastSeen();

    boolean hasDiscordLinked();

}
