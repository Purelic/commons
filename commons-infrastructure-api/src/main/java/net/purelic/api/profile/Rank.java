package net.purelic.api.profile;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.jetbrains.annotations.Nullable;

import static net.kyori.adventure.text.Component.newline;
import static net.kyori.adventure.text.Component.text;
import static net.kyori.adventure.text.event.ClickEvent.openUrl;
import static net.kyori.adventure.text.event.HoverEvent.showText;

public enum Rank {

    // League
    IRON("Iron Rank", '❖', NamedTextColor.DARK_GRAY, "0 - 500 ELO", "leaderboards"),
    GOLD("Gold Rank", '❖', NamedTextColor.YELLOW, "500 - 1000 ELO", "leaderboards"),
    DIAMOND("Diamond Rank", '❖', NamedTextColor.AQUA, "1000 - 1500 ELO", "leaderboards"),
    EMERALD("Emerald Rank", '❖', NamedTextColor.GREEN, "1500 - 2000 ELO", "leaderboards"),
    QUARTZ("Quartz Rank", '❖', NamedTextColor.WHITE, "2000+ ELO", "leaderboards"),
    OBSIDIAN("Obsidian Rank", '❖', NamedTextColor.BLACK, "Top League Player", "leaderboards"),

    // Player
    PREMIUM("Premium", '*', NamedTextColor.GREEN, "Donated to the server at purelic.net/donate", "donate"), // Old - ◊
    CREATOR("Creator", '*', NamedTextColor.RED, "Content creator on the internet", "staff"),
    // OG_PLAYER("OG Player", '*', NamedTextColor.WHITE, "Joined the original PuRelic in 2015/2016"),

    // Staff
    HELPER("Helper", '»', NamedTextColor.LIGHT_PURPLE, "Helps with answering questions and supporting the community", "staff"),
    MODERATOR("Moderator", '»', NamedTextColor.RED, "Helps with moderating public and private servers", "staff", HELPER),
    MAP_DEVELOPER("Map Developer", '»', NamedTextColor.BLUE, "Helps with creating maps for the server", "staff"),
    DEVELOPER("Developer", '»', NamedTextColor.DARK_PURPLE, "Helps with developing features for the server", "staff", MAP_DEVELOPER),
    ADMIN("Admin", '»', NamedTextColor.DARK_RED, "Helps run and administrate the network", "staff", MODERATOR),
    ;

    public static final NamedTextColor OP_COLOR = NamedTextColor.GOLD;
    public static final char OP_FLAIR = '❖';
    public static final Component FANCY_OP_FLAIR = text(OP_FLAIR, OP_COLOR).hoverEvent(showText(text("Operator", OP_COLOR).append(newline()).append(text("Has op permissions on this server", NamedTextColor.GRAY))));

    private final Component name;
    private final Component flair;
    private final NamedTextColor color;
    private final Component fancyFlair;
    private final @Nullable Rank childRank;

    Rank(String name, char flair, NamedTextColor color, String description, String page, @Nullable Rank childRank) {
        this.name = text(name);
        this.flair = text(flair);
        this.color = color;
        this.fancyFlair =
                this.flair
                        .color(this.color)
                        .hoverEvent(
                                showText(this.name.color(this.color)
                                        .append(newline())
                                        .append(text(description, NamedTextColor.GRAY))))
                        .clickEvent(openUrl("https://purelic.net/" + page));
        this.childRank = childRank;
    }

    Rank(String name, Character flair, NamedTextColor color, String description, String page) {
        this(name, flair, color, description, page, null);
    }

    public Component getName(boolean colored) {
        if (colored) return this.name.color(this.color);
        else return this.name;
    }

    public Component getFancyFlair() {
        return this.fancyFlair;
    }

    public NamedTextColor getColor() {
        return this.color;
    }

    public boolean hasChildRank() {
        return this.childRank != null;
    }

    public @Nullable Rank getChildRank() {
        return childRank;
    }
}
