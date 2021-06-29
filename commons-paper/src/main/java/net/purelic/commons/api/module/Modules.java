package net.purelic.commons.api.module;

/**
 * A Module is a standalone feature that depends on listening on events.
 * These can be activated or deactivated with {@link #activate()}
 * or {@link #deactivate()} followed by the relevant modules as they are defined in {@link ModuleManager}
 */
public interface Modules {

    /**
     * Can be used to activate any modules
     * @return a manager which activates all modules called through their relevant methods
     */
    ModuleManager activate();

    /**
     * Can be used to deactivate any modules
     * @return a manager which deactivates all modules called through their relevant methods
     */
    ModuleManager deactivate();
}
