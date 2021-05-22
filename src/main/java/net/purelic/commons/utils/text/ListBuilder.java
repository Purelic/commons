package net.purelic.commons.utils.text;

import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.TextComponent;
import net.purelic.commons.utils.ChatUtils;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class ListBuilder {

    private final String header;
    private final String empty;
    private final List<BaseComponent[]> bullets;

    public ListBuilder(String header, String empty) {
        this.header = header;
        this.empty = empty;
        this.bullets = new ArrayList<>();
    }

    public ListBuilder add(String bullet) {
        return add(TextBuilder.bullet(bullet));
    }

    public ListBuilder add(TextComponent bullet) {
        return add(new TextComponent(ChatColor.GRAY + " " + ChatUtils.BULLET + " " + ChatColor.RESET), bullet);
    }

    public ListBuilder add(ComponentBuilder bullet) {
        return add(bullet.create());
    }

    public ListBuilder add(TextBuilder bullet) {
        return add((BaseComponent) bullet.build());
    }

    public ListBuilder add(BaseComponent... bullet) {
        this.bullets.add(bullet);
        return this;
    }

    public void send(Player player) {
        ChatUtils.sendMessage(player, ChatUtils.getHeader(this.header));

        if (this.bullets.isEmpty()) {
            ChatUtils.sendMessage(player, TextBuilder.bullet(this.empty).build());
            return;
        }

        for (BaseComponent[] bullet : this.bullets) {
            ChatUtils.sendMessage(player, bullet);
        }
    }

}
