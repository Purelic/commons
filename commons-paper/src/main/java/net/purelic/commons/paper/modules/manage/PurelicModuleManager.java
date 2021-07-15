package net.purelic.commons.paper.modules.manage;

import net.purelic.commons.paper.api.module.ModuleManager;
import net.purelic.commons.paper.modules.BasicScoreboardModule;
import net.purelic.commons.paper.modules.BlockPhysicsModule;
import net.purelic.commons.paper.modules.FancyChatModule;
import net.purelic.commons.paper.modules.NoHungerModule;
import net.purelic.commons.paper.modules.NoLeavesDecayModule;
import net.purelic.commons.paper.modules.NoPlayerDamageModule;
import net.purelic.commons.paper.modules.TeleportFromVoidModule;
import org.bukkit.event.Listener;

abstract class PurelicModuleManager implements ModuleManager {

    abstract void doSomething(Listener listener);

    @Override
    public ModuleManager basicScoreboard() {
        this.doSomething(BasicScoreboardModule.INSTANCE);
        return this;
    }

    @Override
    public ModuleManager blockPhysics() {
        this.doSomething(BlockPhysicsModule.INSTANCE);
        return this;
    }

    @Override
    public ModuleManager fancyChat() {
        this.doSomething(FancyChatModule.INSTANCE);
        return this;
    }

    @Override
    public ModuleManager noHunger() {
        this.doSomething(NoHungerModule.INSTANCE);
        return this;
    }

    @Override
    public ModuleManager noLeavesDecay() {
        this.doSomething(NoLeavesDecayModule.INSTANCE);
        return this;
    }

    @Override
    public ModuleManager noPlayerDamage() {
        this.doSomething(NoPlayerDamageModule.INSTANCE);
        return this;
    }

    @Override
    public ModuleManager teleportFromVoid() {
        this.doSomething(TeleportFromVoidModule.INSTANCE);
        return this;
    }
}
