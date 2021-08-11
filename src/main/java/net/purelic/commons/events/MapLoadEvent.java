package net.purelic.commons.events;

import org.bukkit.World;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class MapLoadEvent extends Event {

    private static final HandlerList HANDLERS = new HandlerList();

    private final String map;
    private final World world;
    private final boolean lobby;

    public MapLoadEvent(String map, World world, boolean lobby) {
        this.map = map;
        this.world = world;
        this.lobby = lobby;
    }

    public String getMap() {
        return this.map;
    }

    public World getWorld() {
        return this.world;
    }

    public boolean isLobby() {
        return this.lobby;
    }

    public HandlerList getHandlers() {
        return HANDLERS;
    }

    public static HandlerList getHandlerList() {
        return HANDLERS;
    }

}
