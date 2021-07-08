package net.purelic.commons.paper.profile;

import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.TextComponent;
import net.purelic.commons.paper.utils.text.ClickAction;
import net.purelic.commons.paper.utils.text.TextBuilder;

public enum Rank {

    // League
    IRON("Iron Rank", '❖', ChatColor.DARK_GRAY, "0 - 500 ELO", "leaderboards"),
    GOLD("Gold Rank", '❖', ChatColor.YELLOW, "500 - 1000 ELO", "leaderboards"),
    DIAMOND("Diamond Rank", '❖', ChatColor.AQUA, "1000 - 1500 ELO", "leaderboards"),
    EMERALD("Emerald Rank", '❖', ChatColor.GREEN, "1500 - 2000 ELO", "leaderboards"),
    QUARTZ("Quartz Rank", '❖', ChatColor.WHITE, "2000+ ELO", "leaderboards"),
    OBSIDIAN("Obsidian Rank", '❖', ChatColor.BLACK, "Top League Player", "leaderboards"),

    // Staff
    ADMIN("Admin", '»', ChatColor.DARK_RED, "Helps run and administrate the network", "staff"),
    DEVELOPER("Developer", '»', ChatColor.DARK_PURPLE, "Helps with developing features for the server", "staff"),
    MAP_DEVELOPER("Map Developer", '»', ChatColor.BLUE, "Helps with creating maps for the server", "staff"),
    MODERATOR("Moderator", '»', ChatColor.RED, "Helps with moderating public and private servers", "staff"),
    HELPER("Helper", '»', ChatColor.LIGHT_PURPLE, "Helps with answering questions and supporting the community", "staff"),

    // Player
    PREMIUM("Premium", '*', ChatColor.GREEN, "Donated to the server at purelic.net/donate", "donate"), // Old - ◊
    CREATOR("Creator", '*', ChatColor.RED, "Content creator on the internet", "staff"),
    // OG_PLAYER("OG Player", '*', ChatColor.WHITE, "Joined the original PuRelic in 2015/2016"),
    ;

    private static final ChatColor OP_COLOR = ChatColor.GOLD;
    private static final String OP_FLAIR = OP_COLOR + "❖";
    private static final TextComponent FANCY_OP_FLAIR =
        TextBuilder.of(OP_FLAIR)
            .onHover(OP_COLOR + "Operator" + "\n" + ChatColor.GRAY + "Has op permissions on this server")
            .build();

    private final String name;
    private final String flair;
    private final ChatColor color;
    private final TextComponent fancyFlair;
    private Rank childRank;

    static {
        ADMIN.childRank = MODERATOR;
        DEVELOPER.childRank = MAP_DEVELOPER;
        MODERATOR.childRank = HELPER;
    }

    Rank(String name, Character flair, ChatColor color, String description, String page) {
        this.name = name;
        this.flair = flair.toString();
        this.color = color;
        this.fancyFlair =
            TextBuilder.of(this.flair)
                .color(this.color)
                .onHover(this.color + this.name + "\n" + ChatColor.GRAY + description)
                .onClick(ClickAction.openUrl("https://purelic.net/" + page))
                .build();
    }

    public String getName(boolean colored) {
        if (colored) return this.color + this.name + ChatColor.RESET;
        else return this.name;
    }

    public TextComponent getFancyFlair() {
        return this.fancyFlair;
    }

    public static TextComponent getFancyOperatorFlair() {
        return FANCY_OP_FLAIR;
    }

    public String getFlair() {
        return this.color + this.flair;
    }

    public ChatColor getColor() {
        return this.color;
    }

    public static String getOperatorFlair() {
        return OP_FLAIR;
    }

    public boolean hasChildRank() {
        return this.childRank != null;
    }

    public Rank getChildRank() {
        return childRank;
    }

    public static Rank fromString(String string) {
        for (Rank rank : values()) {
            if (rank.getName(false).equalsIgnoreCase(string)) {
                return rank;
            }
        }

        return null;
    }

}
