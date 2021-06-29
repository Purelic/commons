package net.purelic.commons.api.text.chat;

import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.HoverEvent;

public class ChatUtils {

    public static BaseComponent[] getPrefix(String prefix, String hover, ChatColor primary, ChatColor secondary) {
        return
                new ComponentBuilder("(").color(secondary).bold(true)
                        .event(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder(hover).color(primary).create()))
                        .append(prefix, ComponentBuilder.FormatRetention.ALL).color(primary)
                        .append(") ", ComponentBuilder.FormatRetention.ALL).color(secondary)
                        .create();
    }

    public static BaseComponent[] getHeader(String header) {
        return getHeader(header, false);
    }

    public static BaseComponent[] getHeader(String header, boolean bold) {
        return getHeader(header, bold, ChatColor.AQUA, ChatColor.WHITE);
    }

    public static BaseComponent[] getHeader(String header, ChatColor primary, ChatColor secondary) {
        return getHeader(header, false, primary, secondary);
    }

    public static BaseComponent[] getHeader(String header, boolean bold, ChatColor primary, ChatColor secondary) {
        return getHeader(header, bold, primary, secondary, false);
    }

    public static BaseComponent[] getHeader(String header, boolean bold, ChatColor primary, ChatColor secondary, boolean padding) {
        return new ComponentBuilder((padding ? "\n" : "") + "                    ").color(secondary).strikethrough(true)
                .append("   " + header + "   ").reset().color(primary).bold(bold)
                .append("                    " + (padding ? "\n" : "")).reset().color(secondary).strikethrough(true)
                .create();
    }
}
