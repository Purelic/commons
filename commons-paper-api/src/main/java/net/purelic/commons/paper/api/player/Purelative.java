package net.purelic.commons.paper.api.player;

import net.kyori.adventure.audience.Audience;
import net.kyori.adventure.audience.ForwardingAudience;
import net.kyori.adventure.text.Component;
import net.purelic.commons.purelic.api.profile.PlayerDatabaseProfile;
import net.purelic.commons.purelic.api.utils.Protocol;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.Nullable;

import java.util.Collection;
import java.util.Collections;
import java.util.Date;

/**
 * We are family
 *
 * <p>Wraps a {@link Player} object</p>
 */
public interface Purelative extends PlayerDatabaseProfile, ForwardingAudience.Single, PurelicPaperPlayerAudience {

    /**
     *
     * @return true if and only if this player is online on the server this plugin is running on
     */
    boolean isOnline();

    /**
     *
     * @return the wrapped player object only if {@link #isOnline()} is {@code true}, if {@link #isOnline()} is {@code false}
     * {@code null} is returned.
     */
    @Nullable Player getBukkit();

    Date getInstantJoined();

    //List<GameMatch> recentMatches();

    String getSessionId();

    long getTimePlayed();

    PurelativeInventory getInventory();

    Protocol getProtocol();

    /**
     * Send a fancy chat message FROM this player to {@code recipient}
     * @param prefix the prefix to the message
     * @param message you know it
     * @param recipient the receivers of this message
     */
    void sendFancyMessage(Component prefix, Component message, Collection<Purelative> recipient);

    /**
     * Send a fancy chat message FROM this player to {@code recipient}
     * @param prefix the prefix to the message
     * @param message you know it
     * @param recipient the receiver of this message
     */
    default void sendFancyMessage(Component prefix, Component message, Purelative recipient){
        this.sendFancyMessage(prefix, message, Collections.singleton(recipient));
    }
}
