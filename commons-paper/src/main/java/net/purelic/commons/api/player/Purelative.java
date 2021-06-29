package net.purelic.commons.api.player;

import net.kyori.adventure.audience.ForwardingAudience;
import net.purelic.api.profile.PlayerDatabaseProfile;
import net.purelic.api.profile.Protocol;
import net.purelic.api.profile.Rank;
import net.purelic.commons.api.cg.GameMatch;
import org.bukkit.entity.Player;

import java.util.Date;
import java.util.List;
import java.util.UUID;

/**
 * We are family
 *
 * <p>Wraps a {@link Player} object</p>
 */
public interface Purelative extends PlayerDatabaseProfile, ForwardingAudience.Single {

    Protocol getProtocol();

    Player getBukkit();

    Date getInstantJoined();

    Date getInstantLastSeen();

    List<GameMatch> recentMatches();

    String getSessionId();

    String getNickname();
    
    long getTimePlayed();

    PurelativeInventory getInventory();

}
