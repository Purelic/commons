package net.purelic.commons.listeners;

import org.bukkit.Difficulty;
import org.bukkit.World;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.world.WorldInitEvent;

public class WorldInit implements Listener {

    @EventHandler
    public void onWorldInit(WorldInitEvent event) {
        World world = event.getWorld();
        world.setKeepSpawnInMemory(false);
        world.setGameRuleValue("doMobSpawning", "false");
        world.setGameRuleValue("mobGriefing", "false");
        world.setGameRuleValue("doFireTick", "false");
        world.setGameRuleValue("randomTickSpeed", "0");
        world.setDifficulty(Difficulty.HARD);
        world.setAmbientSpawnLimit(0);
        world.setAutoSave(false);
        world.setPVP(true);
        world.setStorm(false);
        world.setThundering(false);
    }

}
