package net.purelic.commons.profile.preferences.effects;

import net.purelic.commons.Commons;
import org.bukkit.Effect;
import org.bukkit.Location;
import org.bukkit.scheduler.BukkitRunnable;

public class WarpEffect implements CustomEffect {

    @Override
    public void play(Location location) {
        new BukkitRunnable() {

            final float radius = 1.0F;
            final int particles = 20;
            final float grow = 0.2F;
            final int rings = 12;
            int step = 0;

            @Override
            public void run() {
                double y = this.step * this.grow;

                location.add(0.0D, y, 0.0D);

                for (int i = 0; i < this.particles; i++) {
                    double angle = 6.283185307179586D * i / this.particles;
                    double x = Math.cos(angle) * this.radius;
                    double z = Math.sin(angle) * this.radius;

                    location.add(x, 0.0D, z);
                    CustomEffect.effect(location, Effect.FIREWORKS_SPARK, 1);
                    location.subtract(x, 0.0D, z);
                }

                location.subtract(0.0D, y, 0.0D);
                this.step++;

                if (this.step > this.rings) this.cancel();
            }

        }.runTaskTimerAsynchronously(Commons.getPlugin(), 0L, 2L);


    }

}
