package net.purelic.commons.runnables;

import org.bukkit.Bukkit;
import org.bukkit.scheduler.BukkitRunnable;

public class IdleTimer extends BukkitRunnable {

    private static IdleTimer instance;

    public IdleTimer() {
        instance = this;
    }

    @Override
    public void run() {
        Bukkit.shutdown();
    }

    public static IdleTimer getInstance() {
        return instance;
    }

}
