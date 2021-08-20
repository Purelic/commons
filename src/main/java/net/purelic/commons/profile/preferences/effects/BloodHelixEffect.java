package net.purelic.commons.profile.preferences.effects;

import net.purelic.commons.Commons;
import org.bukkit.Effect;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

public class BloodHelixEffect implements CustomEffect {

    @Override
    public void play(Player player) {
        this.play(player.getLocation().clone());
    }

    @Override
    public void play(Location location) {
        new BukkitRunnable() {

            double phi = 0.0D;

            @Override
            public void run() {
                this.phi += 0.39269908169872414D;

                for (double d = 0.0D; d <= 6.283185307179586D; d += 0.19634954084936207D) {
                    for (double d2 = 0.0D; d2 <= 1.0D; d2++) {
                        double d3 = 0.4D * (6.283185307179586D - d) * 0.5D * Math.cos(d + this.phi + d2 * Math.PI);
                        double d4 = 0.5D * d;
                        double d5 = 0.4D * (6.283185307179586D - d) * 0.5D * Math.sin(d + this.phi + d2 * Math.PI);
                        location.add(d3, d4, d5);
                        CustomEffect.effect(location, Effect.COLOURED_DUST, 0F, 0F, 1);
                        location.subtract(d3, d4, d5);
                    }
                }

                if (this.phi > 9.42477796076938D) this.cancel();
            }
        }.runTaskTimerAsynchronously(Commons.getPlugin(), 0L, 1L);
    }

}
