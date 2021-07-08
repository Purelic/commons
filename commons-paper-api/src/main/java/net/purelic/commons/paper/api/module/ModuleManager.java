package net.purelic.commons.paper.api.module;

/**
 * Activates or deactivates modules
 *
 * @see Modules
 */
public interface ModuleManager {
    /**
     *
     * @return this
     */
    ModuleManager basicScoreboard();

    /**
     *
     * @return this
     */
    ModuleManager blockPhysics();

    /**
     *
     * @return this
     */
    ModuleManager fancyChat();

    /**
     *
     * @return this
     */
    ModuleManager noHunger();

    /**
     *
     * @return this
     */
    ModuleManager noLeavesDecay();

    /**
     *
     * @return this
     */
    ModuleManager noPlayerDamage();
}
