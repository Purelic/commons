package net.purelic.commons.api.player;

import net.purelic.commons.api.cg.GameMatch;
import org.bukkit.entity.Player;

import java.util.Date;
import java.util.List;
import java.util.UUID;

/**
 * We are family
 */
public interface Purelative {

    UUID getId();

    String getName();

    Protocol getProtocol();

    Player getBukkit();

    Date getInstantJoined();

    Date getInstantLastSeen();

    List<Rank> getRanks();

    PlayerStatistics getStats();

    List<GameMatch> recentMatches();

    int getRating();

    PlayerPreferences getPreferences();

    String getSessionId();

    String getNickname();

    boolean hasDiscordLinked();

    long getTimePlayed();

}
