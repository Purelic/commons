package net.purelic.commons.paper.modules.manage;

import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;

public class DeactivatingManager extends PurelicModuleManager{
    @Override
    void doSomething(Listener listener) {
        HandlerList.unregisterAll(listener);
    }
}
