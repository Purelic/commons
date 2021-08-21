package net.purelic.commons.modules;

import net.purelic.commons.Commons;
import net.purelic.commons.profile.preferences.ChatChannel;
import net.purelic.commons.utils.ChatUtils;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.AsyncPlayerChatEvent;

public class FancyChatModule implements Module {

    @EventHandler
    public void onPlayerChat(AsyncPlayerChatEvent event) {
        ChatChannel channel = Commons.getProfile(event.getPlayer()).getChatChannel();

        if (channel == ChatChannel.GLOBAL || channel == ChatChannel.ALL) {
            event.setCancelled(true);
            ChatUtils.sendFancyChatMessage(event.getPlayer(), event.getMessage());
        }
    }

}
