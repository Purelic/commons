package net.purelic.commons.paper.api.event;

import net.purelic.commons.paper.api.player.Purelative;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class PurelativeNameChangedEvent extends Event {

    private static final HandlerList HANDLERS = new HandlerList();

    private final Purelative purelative;
    private final String name;

    public PurelativeNameChangedEvent(Purelative purelative, String name) {
        this.purelative = purelative;
        this.name = name;
    }

    public Purelative getPurelative() {
        return this.purelative;
    }

    public String getName() {
        return this.name;
    }

    public HandlerList getHandlers() {
        return HANDLERS;
    }

    public static HandlerList getHandlerList() {
        return HANDLERS;
    }

}