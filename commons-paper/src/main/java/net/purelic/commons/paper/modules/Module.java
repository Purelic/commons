package net.purelic.commons.paper.modules;

import net.purelic.commons.paper.Commons;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;

public interface Module extends Listener {

    default void register() {
        Commons.registerListener(this);
    }

    default void unregister() {
        HandlerList.unregisterAll(this);
    }

}
