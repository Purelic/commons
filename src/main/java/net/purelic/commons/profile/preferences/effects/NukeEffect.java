package net.purelic.commons.profile.preferences.effects;

import net.purelic.commons.Commons;
import org.bukkit.Effect;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

public class NukeEffect implements CustomEffect {

    @Override
    public void play(Player player) {
        this.play(player.getLocation().clone());
    }

    @Override
    public void play(Location location) {
        new BukkitRunnable() {

            double t = 0.7853981633974483D;

            @Override
            public void run() {
                this.t += 0.3141592653589793D;

                for (double d1 = 0.0D; d1 <= 6.283185307179586D; d1 += 0.09817477042468103D) {
                    double d2 = this.t * Math.cos(d1);
                    double d3 = 2.0D * Math.exp(-0.1D * this.t) * Math.sin(this.t) + 1.5D;
                    double d4 = this.t * Math.sin(d1);
                    location.add(d2, d3, d4);
                    CustomEffect.effect(location, Effect.FIREWORKS_SPARK, 0F, 0F, 1);
                    location.subtract(d2, d3, d4);
                    d2 = this.t * Math.cos(d1 += 0.04908738521234052D);
                    d3 = 2.0D * Math.exp(-0.1D * this.t) * Math.sin(this.t) + 1.5D;
                    d4 = this.t * Math.sin(d1);
                    location.add(d2, d3, d4);
                    CustomEffect.effect(location, Effect.FIREWORKS_SPARK, 0F, 0F, 1);
                    location.subtract(d2, d3, d4);
                }
                if (this.t > 10.0D)
                    cancel();
            }
        }.runTaskTimerAsynchronously(Commons.getPlugin(), 0L, 1L);
    }

}
