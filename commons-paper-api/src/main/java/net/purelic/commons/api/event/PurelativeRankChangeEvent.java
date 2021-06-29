package net.purelic.commons.api.event;

import net.purelic.commons.api.player.Purelative;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class PurelativeRankChangeEvent extends Event {

    private static final HandlerList HANDLERS = new HandlerList();

    private final Purelative purelative;

    public PurelativeRankChangeEvent(Purelative purelative) {
        this.purelative = purelative;
    }

    public Purelative getPurelative() {
        return purelative;
    }

    public HandlerList getHandlers() {
        return HANDLERS;
    }

    public static HandlerList getHandlerList() {
        return HANDLERS;
    }

}
