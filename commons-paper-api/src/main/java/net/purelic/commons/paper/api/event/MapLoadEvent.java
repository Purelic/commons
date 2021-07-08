package net.purelic.commons.paper.api.event;

import org.bukkit.World;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class MapLoadEvent extends Event {

    private static final HandlerList HANDLERS = new HandlerList();

    private final String map;
    private final World world;

    public MapLoadEvent(String map, World world) {
        this.map = map;
        this.world = world;
    }

    public String getMap() {
        return this.map;
    }

    public World getWorld() {
        return this.world;
    }

    public HandlerList getHandlers() {
        return HANDLERS;
    }

    public static HandlerList getHandlerList() {
        return HANDLERS;
    }

}
