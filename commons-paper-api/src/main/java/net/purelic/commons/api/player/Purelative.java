package net.purelic.commons.api.player;

import net.kyori.adventure.audience.Audience;
import net.kyori.adventure.audience.ForwardingAudience;
import net.kyori.adventure.text.Component;
import net.purelic.api.profile.PlayerDatabaseProfile;
import net.purelic.api.profile.Protocol;
import org.bukkit.entity.Player;

import java.util.Date;

/**
 * We are family
 *
 * <p>Wraps a {@link Player} object</p>
 */
public interface Purelative extends PlayerDatabaseProfile, ForwardingAudience.Single, PurelicPaperPlayerAudience {

    Player getBukkit();

    Date getInstantJoined();

    //List<GameMatch> recentMatches();

    String getSessionId();

    String getNickname();

    long getTimePlayed();

    PurelativeInventory getInventory();

    Protocol getProtocol();

    /**
     * Send a fancy chat message FROM this player to {@code audience}
     * @param prefix the prefix to the message
     * @param message you know it
     * @param audience the receiver of this message
     */
    void sendFancyMessage(Component prefix, Component message, Audience audience);

}
