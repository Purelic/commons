package net.purelic.commons.api.event;

import net.purelic.commons.api.player.Purelative;
import org.bukkit.entity.Player;
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
