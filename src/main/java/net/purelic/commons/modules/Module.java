package net.purelic.commons.modules;

import net.purelic.commons.Commons;
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
