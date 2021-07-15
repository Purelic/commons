package net.purelic.commons.paper.modules.manage;

import net.purelic.commons.paper.api.PaperCommons;
import org.bukkit.event.Listener;

public class ActivatingManager extends PurelicModuleManager {
    @Override
    void doSomething(Listener listener) {
        PaperCommons.get().register(listener);
    }
}
