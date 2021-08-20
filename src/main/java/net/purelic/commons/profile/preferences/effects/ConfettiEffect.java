package net.purelic.commons.profile.preferences.effects;

import net.purelic.commons.utils.TaskUtils;
import org.bukkit.Color;
import org.bukkit.FireworkEffect;
import org.bukkit.Location;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Firework;
import org.bukkit.entity.Player;
import org.bukkit.inventory.meta.FireworkMeta;

public class ConfettiEffect implements CustomEffect {

    @Override
    public void play(Player player) {
        this.play(player.getLocation());
    }

    @Override
    public void play(Location location) {
        Firework firework = (Firework) location.getWorld().spawnEntity(location, EntityType.FIREWORK);
        FireworkMeta meta = firework.getFireworkMeta();

        FireworkEffect effect = FireworkEffect.builder()
            .flicker(false)
            .trail(false)
            .with(FireworkEffect.Type.BALL)
            .withColor(Color.fromRGB(11743532), Color.fromRGB(15435844), Color.fromRGB(14602026),
                Color.fromRGB(4312372), Color.fromRGB(6719955), Color.fromRGB(8073150), Color.fromRGB(14188952))
            .build();

        meta.addEffect(effect);
        firework.setFireworkMeta(meta);
        TaskUtils.runLater(firework::detonate, 2L);
    }

}
