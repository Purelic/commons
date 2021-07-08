package net.purelic.commons.purelic.api.utils.component;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.event.HoverEvent;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextDecoration;

import static net.kyori.adventure.text.Component.text;
import static net.kyori.adventure.text.event.HoverEvent.showText;

public class PrefixComponent {

    public static final Component COMMAND_ERROR_PREFIX = commandPrefix("Error", NamedTextColor.RED, NamedTextColor.DARK_RED);
    public static final Component COMMAND_ALERT_PREFIX = commandPrefix("Alert", NamedTextColor.YELLOW, NamedTextColor.GOLD);
    public static final Component COMMAND_SUCCESS_PREFIX = commandPrefix("Success", NamedTextColor.GREEN, NamedTextColor.DARK_GREEN);


    public static Component chatPrefix(String prefix, String hover, NamedTextColor primary, NamedTextColor secondary){

        return text("", secondary, TextDecoration.BOLD)
                .append(text("(")
                .append(text(prefix, primary))
                .append(text(")")))
                .hoverEvent(showText(text(hover, primary)));
    }

    public static Component commandPrefix(String hover, NamedTextColor primary, NamedTextColor secondary) {
        return text("", secondary, TextDecoration.BOLD)
                .append(text("["))
                .append(text("!", primary))
                .append(text("] "))
                .hoverEvent(showText(text(hover, primary)));
    }
}
