package net.purelic.api.utils.component;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.event.HoverEvent;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextDecoration;

import static net.kyori.adventure.text.Component.text;

public class PrefixComponent {


    public static Component prefix(String prefix, String hover, NamedTextColor primary, NamedTextColor secondary){

        return text("", secondary, TextDecoration.BOLD)
                .append(text("(")
                .append(text(prefix, primary))
                .append(text(")")))
                .hoverEvent(HoverEvent.showText(text(hover, primary)));
    }
}
