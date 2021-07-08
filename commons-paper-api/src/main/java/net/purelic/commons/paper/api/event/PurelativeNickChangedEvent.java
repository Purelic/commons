package net.purelic.commons.paper.api.event;

import net.purelic.commons.paper.api.player.Purelative;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class PurelativeNickChangedEvent extends Event {

    private static final HandlerList HANDLERS = new HandlerList();

    private final Purelative purelative;

    public PurelativeNickChangedEvent(Purelative purelative) {
        this.purelative = purelative;
    }

    public Purelative getPurelative() {
        return this.purelative;
    }

    public HandlerList getHandlers() {
        return HANDLERS;
    }

    public static HandlerList getHandlerList() {
        return HANDLERS;
    }

}
