package net.purelic.commons.utils.text;

import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.*;

/**
 * A more user-friendly version of the Chat Component API designed mainly for book-based operations
 * NOTE: some (of the more useless) features don't work in some MC versions for client-based errors
 */
public class TextBuilder {

    private static final String BULLET = "•";

    private String text = "";
    private ClickAction onClick = null;
    private HoverAction onHover = null;
    private ChatColor color = ChatColor.WHITE;
    private ChatColor[] style;

    /**
     * Sets the color of the text, or takes the previous color (if null is passed)
     *
     * @param color the color of the text
     * @return the calling TextBuilder's instance
     */
    public TextBuilder color(ChatColor color) {
        this.color = color;
        return this;
    }

    public TextBuilder add(String extra) {
        this.text += extra;
        return this;
    }

    /**
     * Sets the style of the text
     *
     * @param style the style of the text
     * @return the calling TextBuilder's instance
     */
    public TextBuilder style(ChatColor... style) {
        this.style = style;
        return this;
    }

    public TextBuilder onClick(ClickAction action) {
        this.onClick = action;
        return this;
    }

    public TextBuilder onHover(String text) {
        this.onHover = HoverAction.showText(text);
        return this;
    }

    public TextBuilder onHover(BaseComponent... text) {
        this.onHover = HoverAction.showText(text);
        return this;
    }

    public TextBuilder onHover(HoverAction action) {
        this.onHover = action;
        return this;
    }

    /**
     * Creates the component representing the built text
     *
     * @return the component representing the built text
     */
    public TextComponent build() {
        TextComponent res = new TextComponent(text);
        if (onClick != null)
            res.setClickEvent(new ClickEvent(onClick.action(), onClick.value()));
        if (onHover != null)
            res.setHoverEvent(new HoverEvent(onHover.action(), onHover.value()));
        if (color != null) {
            res.setColor(color);
        }
        if (style != null) {
            for (ChatColor c : style) {
                switch (c) {
                    case MAGIC:
                        res.setObfuscated(true);
                        break;
                    case BOLD:
                        res.setBold(true);
                        break;
                    case STRIKETHROUGH:
                        res.setStrikethrough(true);
                        break;
                    case UNDERLINE:
                        res.setUnderlined(true);
                        break;
                    case ITALIC:
                        res.setItalic(true);
                        break;
                }
            }
        }
        return res;
    }

    /**
     * Creates a new TextBuilder with the parameter as his initial text
     *
     * @param text initial text
     * @return a new TextBuilder with the parameter as his initial text
     */
    public static TextBuilder of(String text) {
        return new TextBuilder().text(text);
    }

    public static TextBuilder of(char text) {
        return of("" + text);
    }

    public static TextBuilder bullet() {
        return bullet("");
    }

    public static TextBuilder bullet(String text) {
        return of(ChatColor.GRAY + " " + BULLET + " " + ChatColor.RESET + text);
    }

    private TextBuilder text(String text) {
        this.text = text;
        return this;
    }

    public static HoverEvent hover(String hover) {
        return new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder(hover).create());
    }

}

