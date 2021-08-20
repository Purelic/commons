package net.purelic.commons.profile.preferences;

import net.purelic.commons.Commons;
import net.purelic.commons.profile.Preference;
import net.purelic.commons.profile.Profile;
import net.purelic.commons.profile.preferences.effects.*;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public enum DeathEffect implements CustomEffect {

    POOF(new PoofEffect()),
    CONFETTI(new ConfettiEffect()),
    THANKSGIVING(new ThanksgivingEffect()),
    SNOW(new SnowEffect()),
    ANGRY(new AngryEffect()),
    BLACK_FRIDAY(new BlackFridayEffect()),
    BLOOD(new BloodEffect()),
    BLOOD_HELIX(new BloodHelixEffect()),
    CHESS(new ChessEffect()),
    CHRISTMAS(new ChristmasEffect()),
    CLOUD(new CloudEffect()),
    GOLD(new GoldEffect()),
    HEART(new HeartEffect()),
    HAPPY(new HappyEffect()),
    POTION(new PotionEffect()),
    WARP(new WarpEffect()),
    ENDER(new EnderEffect()),
    NUKE(new NukeEffect()),
    ;

    private static final DeathEffect DEFAULT = POOF;
    private final CustomEffect effect;

    DeathEffect(CustomEffect effect) {
        this.effect = effect;
    }

    @Override
    public void play(Location location) {
        this.effect.play(location);
    }

    @Override
    public void play(Player player) {
        this.effect.play(player);
    }

    public static DeathEffect get(Player player) {
        return get(Commons.getProfile(player));
    }

    public static DeathEffect get(Profile profile) {
        String preference = (String) profile.getPreference(Preference.DEATH_EFFECT, DEFAULT.name());
        DeathEffect deathEffect;

        if (DeathEffect.contains(preference) && !profile.isNicked()) {
            deathEffect = DeathEffect.valueOf(preference.toUpperCase());
        } else {
            deathEffect = DEFAULT;
        }

        return deathEffect;
    }

    public static List<String> getNames() {
        return Stream.of(DeathEffect.values()).map(Enum::name).collect(Collectors.toList());
    }

    public static boolean contains(String value) {
        return DeathEffect.getNames().contains(value.toUpperCase());
    }

}