package net.purelic.commons.api.task;

import net.purelic.commons.api.PaperCommons;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

//TaskUtils
public interface PurelicTaskManager {

    /**
     * Sets the plugin to use whenever tasks are scheduled through any method.
     *
     * <p>By default {@link PaperCommons} is used</p>
     * @param plugin the plugin to use whenever tasks are scheduled through any method
     */
    void setExecutingPlugin(Plugin plugin);

    /**
     * Execute something as soon as possible on the main thread.
     * @param runnable something to execute
     * @return the task object made by Bukkit
     */
    BukkitTask run(Runnable runnable);

    BukkitTask runLater(Runnable runnable, long delay);

    BukkitTask runTimer(Runnable runnable, long delay, long interval);

    BukkitTask runAsync(Runnable runnable);

    BukkitTask runLaterAsync(Runnable runnable, long delay);

    BukkitTask runTimerAsync(Runnable runnable, long delay, long interval);

    boolean isRunning(BukkitRunnable runnable);

    boolean isRunning(BukkitTask task);

    boolean cancelIfRunning(BukkitRunnable runnable);

    boolean cancelIfRunning(BukkitTask task);
}
