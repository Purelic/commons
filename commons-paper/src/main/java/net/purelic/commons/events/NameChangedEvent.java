package net.purelic.commons.events;

import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

import java.util.UUID;

public class NameChangedEvent extends Event {

    private static final HandlerList HANDLERS = new HandlerList();

    private final UUID uuid;
    private final String name;

    public NameChangedEvent(UUID uuid, String name) {
        this.uuid = uuid;
        this.name = name;
    }

    public UUID geId() {
        return this.uuid;
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
