package net.purelic.api.utils.component;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextDecoration;

import static net.kyori.adventure.text.Component.newline;
import static net.kyori.adventure.text.Component.text;

public class HeaderComponent {

    private static final String MAGIC_SPACING = "                    ";
    private static final String MAGIC_SPACING_HEADER = "   ";


    public static Component header(String header, boolean bold, NamedTextColor primary, NamedTextColor secondary, boolean padding){
        final TextComponent.Builder builder = text();

        if(padding) builder.append(newline());

        builder
                .append(text(MAGIC_SPACING, secondary, TextDecoration.STRIKETHROUGH))
                .append(text(MAGIC_SPACING_HEADER + header + MAGIC_SPACING_HEADER, primary).decoration(TextDecoration.BOLD, bold))
                .append(text(MAGIC_SPACING, secondary, TextDecoration.STRIKETHROUGH));

        if(padding) builder.append(newline());

        return builder.build();
    }

    public static Component header(String header, boolean bold, NamedTextColor primary, NamedTextColor secondary){
        return header(header, bold, primary, secondary, false);
    }

    public static Component header(String header, NamedTextColor primary, NamedTextColor secondary){
        return header(header, false, primary, secondary);
    }

    public static Component header(String header, boolean bold){
        return header(header, bold, NamedTextColor.AQUA, NamedTextColor.WHITE);
    }

    public static Component header(String header){
        return header(header, false);
    }


}
