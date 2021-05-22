package net.purelic.commons.profile.preferences;

import net.md_5.bungee.api.ChatColor;
import org.apache.commons.lang.WordUtils;

import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public enum ArmorColor {

    // Free Colors
    RED(ChatColor.RED, false),
    YELLOW(ChatColor.YELLOW, false),
    GREEN(ChatColor.GREEN, false),
    BLUE(ChatColor.BLUE, false),
    AQUA(ChatColor.AQUA, false),
    GRAY(ChatColor.GRAY, false),

    // Premium Colors
    DARK_RED(ChatColor.DARK_RED, true),
    GOLD(ChatColor.GOLD, true),
    DARK_GREEN(ChatColor.DARK_GREEN, true),
    DARK_BLUE(ChatColor.DARK_BLUE, true),
    DARK_AQUA(ChatColor.DARK_AQUA, true),
    PINK(ChatColor.LIGHT_PURPLE, true),
    PURPLE(ChatColor.DARK_PURPLE, true),
    WHITE(ChatColor.WHITE, true),
    DARK_GRAY(ChatColor.DARK_GRAY, true),
    BLACK(ChatColor.BLACK, true),
    ;

    private final String name;
    private final ChatColor color;
    private final boolean premium;

    ArmorColor(ChatColor color, boolean premium) {
        this.name = WordUtils.capitalizeFully(this.name().replaceAll("_", " "));
        this.color = color;
        this.premium = premium;
    }

    public String getName() {
        return this.name;
    }

    public ChatColor getColor() {
        return this.color;
    }

    public boolean isPremium() {
        return this.premium;
    }

    public static List<String> getNames() {
        return Stream.of(ArmorColor.values()).map(Enum::name).collect(Collectors.toList());
    }

    public static boolean contains(String value) {
        return ArmorColor.getNames().contains(value.toUpperCase());
    }

    public static ArmorColor random(boolean includePremium) {
        List<ArmorColor> colors = Arrays.stream(ArmorColor.values())
            .filter(color -> includePremium || !color.isPremium())
            .collect(Collectors.toList());
        return colors.get(new Random().nextInt(colors.size()));
    }

}
