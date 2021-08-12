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
    BUHLOSSOM_PINK("#EDB3C2", "#E9E9E9"),
    DART_RED("#191919", "#631312", "#191919", "#631312"),
    OAF_TEAL("#00C7D5", "#00C7D5", "#05F72C", "#00D561"),
    BLACK("#000000"),
    WHITE("#FFFFFF"),
    PASTEL_GREEN("#BAFFC9"),
    PASTEL_BLUE("#BAE1FF"),
    SANDSTONE("#E1E1B8"),
    DARK_LOVE("#401F59", "#61265F", "#C23B6F", "#E34275"),
    GREEN_CAMO("#2B3D33", "#8A925E", "#C5C086", "#3B4937"),
    RAINBOW("#FF0018", "#FFFF41", "#008018", "#0000F9"),
    BLACK_AND_GOLD("#DAAB2D", "#DAAB2D", "#262626", "#020B13"),
    SKY_BLUE("#419AF2", "#4FA9F2", "#6CC8F1", "#7BD8F0")
    ;

    private final int helmet;
    private final int chestplate;
    private final int leggings;
    private final int boots;

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

    public int getHelmet() {
        return this.helmet;
    }

    public int getChestplate() {
        return this.chestplate;
    }

    public int getLeggings() {
        return this.leggings;
    }

    public int getBoots() {
        return this.boots;
    }

    public static List<String> getNames() {
        return Stream.of(ArmorColor.values()).map(Enum::name).collect(Collectors.toList());
    }

    public static boolean contains(String value) {
        return ArmorColor.getNames().contains(value.toUpperCase());
    }

}
