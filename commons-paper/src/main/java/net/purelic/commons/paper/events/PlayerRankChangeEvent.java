package net.purelic.commons.paper.events;

import net.purelic.commons.paper.profile.Profile;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class PlayerRankChangeEvent extends Event {

    private static final HandlerList HANDLERS = new HandlerList();

    private final Player player;
    private final Profile profile;

    public PlayerRankChangeEvent(Player player, Profile profile) {
        this.player = player;
        this.profile = profile;
    }

    public Player getPlayer() {
        return player;
    }

    public Profile getProfile() {
        return profile;
    }

    public HandlerList getHandlers() {
        return HANDLERS;
    }

    public static HandlerList getHandlerList() {
        return HANDLERS;
    }

}
