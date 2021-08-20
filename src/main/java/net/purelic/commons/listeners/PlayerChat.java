package net.purelic.commons.listeners;

import net.purelic.commons.Commons;
import net.purelic.commons.profile.Profile;
import net.purelic.commons.profile.preferences.ChatChannel;
import net.purelic.commons.utils.CommandUtils;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

public class PlayerChat implements Listener {

    @EventHandler(priority = EventPriority.MONITOR)
    public void onPlayerChat(AsyncPlayerChatEvent event) {
        Player player = event.getPlayer();
        String message = event.getMessage();
        Profile profile = Commons.getProfile(player);
        ChatChannel channel = Commons.getProfile(player).getChatChannel();

        if (channel == ChatChannel.STAFF) {
            if (profile.isStaff()) {
                Commons.sendSpringMessage(player, "StaffChat", message);
            } else {
                CommandUtils.sendErrorMessage(player, "You can no longer use this chat channel! Your chat channel has been reset to all.");
                profile.setChatChannel(channel);
            }
        } else if (channel == ChatChannel.PARTY) {
            Commons.sendSpringMessage(player, "PartyChat", message);
        }
    }

}
