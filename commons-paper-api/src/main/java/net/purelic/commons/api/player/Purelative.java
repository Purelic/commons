package net.purelic.commons.api.player;

import net.kyori.adventure.audience.ForwardingAudience;
import net.purelic.api.profile.PlayerDatabaseProfile;
import net.purelic.api.profile.Protocol;
import net.purelic.commons.api.Protocold;
import net.purelic.commons.api.PurelicPaperPlayerAudience;
import org.bukkit.entity.Player;

import java.util.Date;

/**
 * We are family
 *
 * <p>Wraps a {@link Player} object</p>
 */
public interface Purelative extends PlayerDatabaseProfile, ForwardingAudience.Single, PurelicPaperPlayerAudience, Protocold {

    Player getBukkit();

    Date getInstantJoined();

    //List<GameMatch> recentMatches();

    String getSessionId();

    String getNickname();

    long getTimePlayed();

    PurelativeInventory getInventory();

}
