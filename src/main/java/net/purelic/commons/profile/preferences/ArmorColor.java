package net.purelic.commons.profile.preferences;

import java.awt.*;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public enum ArmorColor {

    YELLOW("#FFFF55"),
    RED("#FF5555"),
    BLUE("#5555FF"),
    GREEN("#55FF55"),
    DARK_BLUE("#0000AA"),
    DARK_GREEN("#00AA00"),
    DARK_AQUA("#555555"),
    DARK_RED("#AA0000"),
    DARK_PURPLE("#AA00AA"),
    GRAY("#AAAAAA"),
    DARK_GRAY("#00AAAA"),
    AQUA("#55FFFF"),
    LIGHT_PURPLE("#FF55FF"),
    OG_PURPLE("#8427FD"),
    GOLD("#FFAA00"),
    BUHLOSSOM_PINK("#D67689", "#EDB3C2", "#E9E9E9", "#D1D1D1"),
    DART_RED("#191919", "#631312", "#191919", "#631312"),
    OAF_TEAL("#016B71", "#003537", "#05F72C", "#00D561"),
    BLACK("#000000"),
    WHITE("#FFFFFF"),
    PASTEL_GREEN("#BAFFC9"),
    PASTEL_BLUE("#BAE1FF"),
    ;

    private final long helmet;
    private final long chestplate;
    private final long leggings;
    private final long boots;

    ArmorColor(String color) {
        this(color, color);
    }

    ArmorColor(String top, String bottom) {
        this(top, top, bottom, bottom);
    }

    ArmorColor(String helmet, String chestplate, String leggings, String boots) {
        this.helmet = this.getRGB(helmet);
        this.chestplate = this.getRGB(chestplate);
        this.leggings = this.getRGB(leggings);
        this.boots = this.getRGB(boots);
    }

    private int getRGB(String hex) {
        Color color = Color.decode(hex);
        int r = color.getRed();
        int g = color.getGreen();
        int b = color.getBlue();
        return r << 16 | g << 8 | b;
    }

    public long getHelmet() {
        return this.helmet;
    }

    public long getChestplate() {
        return this.chestplate;
    }

    public long getLeggings() {
        return this.leggings;
    }

    public long getBoots() {
        return this.boots;
    }

    public static List<String> getNames() {
        return Stream.of(ArmorColor.values()).map(Enum::name).collect(Collectors.toList());
    }

    public static boolean contains(String value) {
        return ArmorColor.getNames().contains(value.toUpperCase());
    }

}
