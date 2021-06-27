package net.purelic.commons.api.module;

/**
 * Activates or deactivates modules
 *
 * @see Modules
 */
public interface ModuleManager {
    /**
     *
     * @return
     */
    ModuleManager basicScoreboard();

    ModuleManager blockPhysics();

    ModuleManager fancyChat();

    ModuleManager noHunger();

    ModuleManager noLeavesDecay();

    ModuleManager noPlayerDamage();
}
