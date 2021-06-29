package net.purelic.commons.utils;

import net.purelic.commons.Commons;
import org.bukkit.Bukkit;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

public class TaskUtils {

    public static BukkitTask runLaterAsync(Runnable runnable, long delay) {
        if (runnable instanceof BukkitRunnable) return ((BukkitRunnable) runnable).runTaskLaterAsynchronously(Commons.getPlugin(), delay);
        else return Bukkit.getScheduler().runTaskLaterAsynchronously(Commons.getPlugin(), runnable, delay);
    }

    public static BukkitTask runLater(Runnable runnable, long delay) {
        if (runnable instanceof BukkitRunnable) return ((BukkitRunnable) runnable).runTaskLater(Commons.getPlugin(), delay);
        else return Bukkit.getScheduler().runTaskLater(Commons.getPlugin(), runnable, delay);
    }

    public static BukkitTask runAsync(Runnable runnable) {
        if (runnable instanceof BukkitRunnable) return ((BukkitRunnable) runnable).runTaskAsynchronously(Commons.getPlugin());
        else return Bukkit.getScheduler().runTaskAsynchronously(Commons.getPlugin(), runnable);
    }

    public static BukkitTask run(Runnable runnable) {
        if (runnable instanceof BukkitRunnable) return ((BukkitRunnable) runnable).runTask(Commons.getPlugin());
        else return Bukkit.getScheduler().runTask(Commons.getPlugin(), runnable);
    }

    public static BukkitTask runTimerAsync(Runnable runnable, long delay, long interval) {
        if (runnable instanceof BukkitRunnable) return ((BukkitRunnable) runnable).runTaskTimerAsynchronously(Commons.getPlugin(), delay, interval);
        else return Bukkit.getScheduler().runTaskTimerAsynchronously(Commons.getPlugin(), runnable, delay, interval);
    }

    public static BukkitTask runTimer(Runnable runnable, long delay, long interval) {
        if (runnable instanceof BukkitRunnable) return ((BukkitRunnable) runnable).runTaskTimer(Commons.getPlugin(), delay, interval);
        else return Bukkit.getScheduler().runTaskTimer(Commons.getPlugin(), runnable, delay, interval);
    }

    public static boolean isRunning(BukkitRunnable runnable) {
        if (runnable == null) return false;

        try {
            int id = runnable.getTaskId();
            return Bukkit.getScheduler().isQueued(id) || Bukkit.getScheduler().isCurrentlyRunning(id);
        } catch (IllegalStateException e) {
            e.printStackTrace();
            return false;
        }
    }

    public static void cancelIfRunning(BukkitRunnable runnable) {
        if (isRunning(runnable)) runnable.cancel();
    }

}
