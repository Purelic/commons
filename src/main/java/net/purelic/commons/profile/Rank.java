package net.purelic.commons.profile;

import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.TextComponent;
import net.purelic.commons.utils.text.ClickAction;
import net.purelic.commons.utils.text.TextBuilder;

public enum Rank {

    // League
    IRON("Iron Rank", '❖', ChatColor.DARK_GRAY, false, "0 - 500 ELO", "leaderboards"),
    GOLD_LEAGUE("Gold Rank", '❖', ChatColor.YELLOW, false, "500 - 1000 ELO", "leaderboards"),
    DIAMOND("Diamond Rank", '❖', ChatColor.AQUA, false, "1000 - 1500 ELO", "leaderboards"),
    EMERALD("Emerald Rank", '❖', ChatColor.GREEN, false, "1500 - 2000 ELO", "leaderboards"),
    QUARTZ("Quartz Rank", '❖', ChatColor.WHITE, false, "2000+ ELO", "leaderboards"),
    OBSIDIAN("Obsidian Rank", '❖', ChatColor.BLACK, false, "Top League Player", "leaderboards"),

    // Staff
    ADMIN("Admin", '»', ChatColor.DARK_RED, true, "Helps run and administrate the network", "staff"),
    DEVELOPER("Developer", '»', ChatColor.DARK_PURPLE, true, "Helps with developing features for the server", "staff"),
    MAP_DEVELOPER("Map Developer", '»', ChatColor.BLUE, true, "Helps with creating maps for the server", "staff"),
    MODERATOR("Moderator", '»', ChatColor.RED, true, "Helps with moderating public and private servers", "staff"),
    HELPER("Helper", '»', ChatColor.LIGHT_PURPLE, true, "Helps with answering questions and supporting the community", "staff"),

    // Player
    CREATOR("Creator", '*', ChatColor.RED, false, "Content creator on the internet", "staff"),
    PREMIUM_PLUS("Premium+", '+', ChatColor.GREEN, false, "Subscribed to Premium at purelic.net/donate", "donate"), // Old - ◊
    PREMIUM("Premium", '*', ChatColor.GREEN, false, "Donated to the server at purelic.net/donate", "donate"), // Old - ◊,
    GOLD("Gold", '*', ChatColor.YELLOW, false, "Donated to the server at purelic.net/donate", "donate"),
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
    private final boolean staff;
    private final TextComponent fancyFlair;
    private Rank childRank;

    static {
        ADMIN.childRank = MODERATOR;
        DEVELOPER.childRank = MAP_DEVELOPER;
        MODERATOR.childRank = HELPER;
    }

    Rank(String name, Character flair, ChatColor color, boolean staff, String description, String page) {
        this.name = name;
        this.flair = flair.toString();
        this.color = color;
        this.staff = staff;
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

    public boolean isStaff() {
        return this.staff;
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
