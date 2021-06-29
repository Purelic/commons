package net.purelic.commons.api.text;

import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.BaseComponent;

public interface NameDecorationManager {

    BaseComponent[] getPrefix(String prefix, String hover, ChatColor primary, ChatColor secondary);
}
