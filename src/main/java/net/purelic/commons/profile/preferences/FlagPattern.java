package net.purelic.commons.profile.preferences;

import org.bukkit.DyeColor;
import org.bukkit.block.banner.Pattern;
import org.bukkit.block.banner.PatternType;

import java.util.Arrays;
import java.util.List;

public enum FlagPattern {

    BLANK(new Pattern(DyeColor.WHITE, PatternType.BASE)),
    BOLT(
        new Pattern(DyeColor.LIGHT_BLUE, PatternType.BASE),
        new Pattern(DyeColor.PURPLE, PatternType.GRADIENT_UP),
        new Pattern(DyeColor.BLACK, PatternType.STRIPE_RIGHT),
        new Pattern(DyeColor.BLACK, PatternType.STRIPE_LEFT),
        new Pattern(DyeColor.BLACK, PatternType.TRIANGLES_BOTTOM),
        new Pattern(DyeColor.BLACK, PatternType.TRIANGLES_TOP),
        new Pattern(DyeColor.BLACK, PatternType.SQUARE_TOP_RIGHT),
        new Pattern(DyeColor.BLACK, PatternType.SQUARE_BOTTOM_LEFT)
    ),
    BRICKS(
        new Pattern(DyeColor.LIGHT_BLUE, PatternType.BASE),
        new Pattern(DyeColor.PURPLE, PatternType.GRADIENT_UP),
        new Pattern(DyeColor.BLACK, PatternType.BRICKS),
        new Pattern(DyeColor.BLACK, PatternType.CURLY_BORDER),
        new Pattern(DyeColor.BLACK, PatternType.TRIANGLES_BOTTOM),
        new Pattern(DyeColor.BLACK, PatternType.TRIANGLES_TOP)
    ),
    MIRRORED_HEARTS(
        new Pattern(DyeColor.LIGHT_BLUE, PatternType.BASE),
        new Pattern(DyeColor.PURPLE, PatternType.GRADIENT_UP),
        new Pattern(DyeColor.BLACK, PatternType.CROSS),
        new Pattern(DyeColor.BLACK, PatternType.TRIANGLES_TOP),
        new Pattern(DyeColor.BLACK, PatternType.TRIANGLES_BOTTOM),
        new Pattern(DyeColor.BLACK, PatternType.BORDER),
        new Pattern(DyeColor.BLACK, PatternType.CURLY_BORDER)
    ),
    CREEPER(
        new Pattern(DyeColor.LIGHT_BLUE, PatternType.BASE),
        new Pattern(DyeColor.PURPLE, PatternType.GRADIENT_UP),
        new Pattern(DyeColor.BLACK, PatternType.CREEPER)
    ),
    POKEBALL(
        new Pattern(DyeColor.WHITE, PatternType.BASE),
        new Pattern(DyeColor.RED, PatternType.HALF_HORIZONTAL),
        new Pattern(DyeColor.RED, PatternType.HALF_HORIZONTAL),
        new Pattern(DyeColor.BLACK, PatternType.RHOMBUS_MIDDLE),
        new Pattern(DyeColor.RED, PatternType.STRIPE_TOP),
        new Pattern(DyeColor.WHITE, PatternType.STRIPE_BOTTOM),
        new Pattern(DyeColor.BLACK, PatternType.STRIPE_MIDDLE),
        new Pattern(DyeColor.WHITE, PatternType.CIRCLE_MIDDLE)
    ),
    SUS(
        new Pattern(DyeColor.LIGHT_BLUE, PatternType.BASE),
        new Pattern(DyeColor.WHITE, PatternType.STRIPE_DOWNLEFT),
        new Pattern(DyeColor.RED, PatternType.STRIPE_TOP),
        new Pattern(DyeColor.RED, PatternType.BORDER),
        new Pattern(DyeColor.RED, PatternType.HALF_HORIZONTAL_MIRROR)
    ),
    DREAM(
        new Pattern(DyeColor.WHITE, PatternType.BASE),
        new Pattern(DyeColor.BLACK, PatternType.HALF_HORIZONTAL_MIRROR),
        new Pattern(DyeColor.WHITE, PatternType.FLOWER),
        new Pattern(DyeColor.WHITE, PatternType.FLOWER),
        new Pattern(DyeColor.LIME, PatternType.BORDER),
        new Pattern(DyeColor.WHITE, PatternType.CURLY_BORDER),
        new Pattern(DyeColor.WHITE, PatternType.STRIPE_BOTTOM),
        new Pattern(DyeColor.WHITE, PatternType.STRIPE_BOTTOM),
        new Pattern(DyeColor.WHITE, PatternType.HALF_HORIZONTAL),
        new Pattern(DyeColor.LIME, PatternType.CURLY_BORDER),
        new Pattern(DyeColor.LIME, PatternType.BORDER),
        new Pattern(DyeColor.LIME, PatternType.STRIPE_TOP),
        new Pattern(DyeColor.LIME, PatternType.STRIPE_BOTTOM)
    ),
    MAD_DOG(
        new Pattern(DyeColor.WHITE, PatternType.BASE),
        new Pattern(DyeColor.BROWN, PatternType.STRIPE_BOTTOM),
        new Pattern(DyeColor.LIGHT_BLUE, PatternType.BORDER),
        new Pattern(DyeColor.BROWN, PatternType.TRIANGLE_BOTTOM),
        new Pattern(DyeColor.PINK, PatternType.RHOMBUS_MIDDLE),
        new Pattern(DyeColor.BLACK, PatternType.CREEPER),
        new Pattern(DyeColor.BLACK, PatternType.FLOWER),
        new Pattern(DyeColor.BROWN, PatternType.SKULL),
        new Pattern(DyeColor.LIGHT_BLUE, PatternType.STRIPE_TOP),
        new Pattern(DyeColor.LIGHT_BLUE, PatternType.TRIANGLE_TOP),
        new Pattern(DyeColor.BROWN, PatternType.CIRCLE_MIDDLE)
    ),
    DRACONIAN(
        new Pattern(DyeColor.YELLOW, PatternType.BASE),
        new Pattern(DyeColor.CYAN, PatternType.STRAIGHT_CROSS),
        new Pattern(DyeColor.YELLOW, PatternType.BRICKS),
        new Pattern(DyeColor.YELLOW, PatternType.STRIPE_LEFT),
        new Pattern(DyeColor.YELLOW, PatternType.STRIPE_RIGHT),
        new Pattern(DyeColor.RED, PatternType.CURLY_BORDER)
    ),
    SUNRISE(
        new Pattern(DyeColor.WHITE, PatternType.BASE),
        new Pattern(DyeColor.PURPLE, PatternType.GRADIENT),
        new Pattern(DyeColor.PINK, PatternType.GRADIENT_UP),
        new Pattern(DyeColor.ORANGE, PatternType.CIRCLE_MIDDLE),
        new Pattern(DyeColor.YELLOW, PatternType.FLOWER),
        new Pattern(DyeColor.WHITE, PatternType.TRIANGLE_BOTTOM),
        new Pattern(DyeColor.GRAY, PatternType.TRIANGLES_BOTTOM)
    ),
    SAD_FACE(
        new Pattern(DyeColor.BLACK, PatternType.BASE),
        new Pattern(DyeColor.WHITE, PatternType.SKULL),
        new Pattern(DyeColor.WHITE, PatternType.CIRCLE_MIDDLE),
        new Pattern(DyeColor.BLACK, PatternType.FLOWER),
        new Pattern(DyeColor.BLACK, PatternType.FLOWER),
        new Pattern(DyeColor.BLACK, PatternType.FLOWER),
        new Pattern(DyeColor.BLACK, PatternType.HALF_HORIZONTAL_MIRROR),
        new Pattern(DyeColor.BLACK, PatternType.HALF_HORIZONTAL_MIRROR)
    ),
    DEMON(
        new Pattern(DyeColor.RED, PatternType.BASE),
        new Pattern(DyeColor.BLACK, PatternType.RHOMBUS_MIDDLE),
        new Pattern(DyeColor.BLACK, PatternType.STRIPE_SMALL),
        new Pattern(DyeColor.BLACK, PatternType.CREEPER),
        new Pattern(DyeColor.BLACK, PatternType.BORDER),
        new Pattern(DyeColor.BLACK, PatternType.TRIANGLES_TOP),
        new Pattern(DyeColor.ORANGE, PatternType.TRIANGLES_BOTTOM),
        new Pattern(DyeColor.WHITE, PatternType.SKULL),
        new Pattern(DyeColor.BLACK, PatternType.SKULL)
    ),
    BLEEDING_HEART(
        new Pattern(DyeColor.PINK, PatternType.BASE),
        new Pattern(DyeColor.WHITE, PatternType.STRIPE_RIGHT),
        new Pattern(DyeColor.WHITE, PatternType.HALF_VERTICAL),
        new Pattern(DyeColor.PINK, PatternType.STRIPE_RIGHT),
        new Pattern(DyeColor.PINK, PatternType.HALF_VERTICAL),
        new Pattern(DyeColor.WHITE, PatternType.CIRCLE_MIDDLE),
        new Pattern(DyeColor.PINK, PatternType.TRIANGLE_TOP)
    ),
    WINGED_HEART(
        new Pattern(DyeColor.WHITE, PatternType.BASE),
        new Pattern(DyeColor.BLACK, PatternType.HALF_HORIZONTAL_MIRROR),
        new Pattern(DyeColor.BLACK, PatternType.HALF_HORIZONTAL),
        new Pattern(DyeColor.WHITE, PatternType.FLOWER),
        new Pattern(DyeColor.BLACK, PatternType.HALF_HORIZONTAL),
        new Pattern(DyeColor.BLACK, PatternType.STRAIGHT_CROSS),
        new Pattern(DyeColor.RED, PatternType.CIRCLE_MIDDLE),
        new Pattern(DyeColor.BLACK, PatternType.TRIANGLE_TOP),
        new Pattern(DyeColor.BLACK, PatternType.GRADIENT_UP)
    ),
    DIAMOND_SWORD(
        new Pattern(DyeColor.BLACK, PatternType.BASE),
        new Pattern(DyeColor.ORANGE, PatternType.FLOWER),
        new Pattern(DyeColor.YELLOW, PatternType.FLOWER),
        new Pattern(DyeColor.BLACK, PatternType.TRIANGLE_TOP),
        new Pattern(DyeColor.LIGHT_BLUE, PatternType.STRIPE_CENTER),
        new Pattern(DyeColor.YELLOW, PatternType.TRIANGLE_BOTTOM),
        new Pattern(DyeColor.BLACK, PatternType.SQUARE_BOTTOM_RIGHT),
        new Pattern(DyeColor.BLACK, PatternType.SQUARE_BOTTOM_LEFT),
        new Pattern(DyeColor.YELLOW, PatternType.TRIANGLES_BOTTOM),
        new Pattern(DyeColor.BLACK, PatternType.CURLY_BORDER)
    ),
    CROWNED_SKULL(
        new Pattern(DyeColor.BLACK, PatternType.BASE),
        new Pattern(DyeColor.WHITE, PatternType.CURLY_BORDER),
        new Pattern(DyeColor.WHITE, PatternType.STRIPE_CENTER),
        new Pattern(DyeColor.BLACK, PatternType.STRIPE_BOTTOM),
        new Pattern(DyeColor.WHITE, PatternType.CREEPER),
        new Pattern(DyeColor.YELLOW, PatternType.STRIPE_TOP),
        new Pattern(DyeColor.BLACK, PatternType.TRIANGLES_TOP)
    ),
    POTION(
        new Pattern(DyeColor.BLACK, PatternType.BASE),
        new Pattern(DyeColor.WHITE, PatternType.STRIPE_CENTER),
        new Pattern(DyeColor.BLACK, PatternType.STRIPE_SMALL),
        new Pattern(DyeColor.WHITE, PatternType.TRIANGLES_TOP),
        new Pattern(DyeColor.BLACK, PatternType.STRIPE_LEFT),
        new Pattern(DyeColor.BLACK, PatternType.STRIPE_RIGHT),
        new Pattern(DyeColor.BLACK, PatternType.BORDER),
        new Pattern(DyeColor.WHITE, PatternType.RHOMBUS_MIDDLE),
        new Pattern(DyeColor.PURPLE, PatternType.CIRCLE_MIDDLE),
        new Pattern(DyeColor.SILVER, PatternType.STRIPE_BOTTOM),
        new Pattern(DyeColor.BLACK, PatternType.CURLY_BORDER),
        new Pattern(DyeColor.RED, PatternType.GRADIENT_UP),
        new Pattern(DyeColor.ORANGE, PatternType.TRIANGLES_BOTTOM)
    ),
    GUN(
        new Pattern(DyeColor.WHITE, PatternType.BASE),
        new Pattern(DyeColor.BLACK, PatternType.FLOWER),
        new Pattern(DyeColor.BLACK, PatternType.STRIPE_DOWNLEFT),
        new Pattern(DyeColor.WHITE, PatternType.DIAGONAL_RIGHT_MIRROR),
        new Pattern(DyeColor.BLACK, PatternType.STRIPE_DOWNRIGHT),
        new Pattern(DyeColor.WHITE, PatternType.CURLY_BORDER),
        new Pattern(DyeColor.WHITE, PatternType.SQUARE_BOTTOM_RIGHT)
    ),
    ;

    private final List<Pattern> patterns;

    FlagPattern(Pattern... patterns) {
        this.patterns = Arrays.asList(patterns);
    }

    public List<Pattern> getPatterns() {
        return this.patterns;
    }

}