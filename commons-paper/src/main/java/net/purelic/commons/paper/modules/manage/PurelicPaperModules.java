package net.purelic.commons.paper.modules.manage;

import net.purelic.commons.paper.api.module.ModuleManager;
import net.purelic.commons.paper.api.module.Modules;

public class PurelicPaperModules implements Modules {

    private final ModuleManager activating = new ActivatingManager();
    private final ModuleManager deactivating = new DeactivatingManager();

    @Override
    public ModuleManager activate() {
        return this.activating;
    }

    @Override
    public ModuleManager deactivate() {
        return this.deactivating;
    }
}
