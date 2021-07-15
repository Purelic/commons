package net.purelic.commons.paper.modules;

import net.purelic.commons.paper.utils.ChatUtils;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

public class FancyChatModule implements Listener {

    public static final FancyChatModule INSTANCE = new FancyChatModule();

    private FancyChatModule(){}

    @EventHandler
    public void onPlayerChat(AsyncPlayerChatEvent event) {
        event.setCancelled(true);
        ChatUtils.sendFancyChatMessage(event.getPlayer(), event.getMessage());
    }

}
