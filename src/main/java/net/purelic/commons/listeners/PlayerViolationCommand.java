package net.purelic.commons.listeners;

import me.vagdedes.spartan.api.PlayerViolationCommandEvent;
import me.vagdedes.spartan.system.Enums;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.purelic.commons.Commons;
import net.purelic.commons.profile.Profile;
import net.purelic.commons.utils.*;
import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.configuration.Configuration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import java.awt.*;

public class PlayerViolationCommand implements Listener {

    private final String webhook;

    public PlayerViolationCommand(Configuration config) {
        this.webhook = config.getString("guardian_webhook");
    }

    @EventHandler
    public void onPlayerViolationCommand(PlayerViolationCommandEvent event) {
        Player player = event.getPlayer();
        String hacks = this.getHacks(event.getHackTypes());
        int ping = Commons.getPing(player);

        if (ping > 200) return;

        for (Player online : Bukkit.getOnlinePlayers()) {
            Profile profile = Commons.getProfile(online);

            if (!profile.isMod()) continue;

            ChatUtils.sendMessage(online,
                new ComponentBuilder("GUARDIAN  ").color(ChatColor.RED).bold(true).create()[0],
                Fetcher.getFancyName(player),
                new ComponentBuilder(ChatColor.GRAY + " " + ChatUtils.ARROW + ChatColor.WHITE + " " + hacks).create()[0],
                new ComponentBuilder(" (" + ping + "ms / " + Commons.getTPS() + " TPS)").color(ChatColor.GRAY).create()[0]);

            online.playSound(online.getLocation(), Sound.BLAZE_DEATH, 2F, 1F);
        }

        this.sendDiscordReport(player, hacks);
    }

    private String getHacks(Enums.HackType[] hackTypes) {
        String hacks = "";

        boolean first = true;

        for (Enums.HackType hack : hackTypes) {
            if (!first) hacks += ", ";
            first = false;

            if (hack == Enums.HackType.IrregularMovements) {
                hacks += "Irregular Movements";
            } else if (hack == Enums.HackType.HitReach) {
                hacks += "Reach";
            } else if (hack == Enums.HackType.FastClicks) {
                hacks += "Auto-Clicking";
            } else {
                hacks += hack.name();
            }
        }

        return hacks;
    }

    private void sendDiscordReport(Player reported, String hacks) {
        DiscordWebhook webhook = new DiscordWebhook(this.webhook, "Guardian");
        webhook.addEmbed(new DiscordWebhook.EmbedObject()
            .setColor(Color.ORANGE)
            .addField("Nickname", NickUtils.isNicked(reported) ? NickUtils.getNick(reported) : "N/A", false)
            .addField("Hack(s)", hacks, false)
            .addField("Server", ServerUtils.getName(), false)
            .addField("Ping", Commons.getPing(reported) + "", false)
            .addField("TPS", Commons.getTPS() + "", false)
            .setFooter("Automatic Report", "https://purelic.net/siteicon.png")
            .setAuthor(reported.getName(), "https://purelic.net/players/" + reported.getName(), "https://crafatar.com/renders/head/" + reported.getUniqueId().toString() + "?size=128&overlay")
            // TODO .setAuthor(NickUtils.getRealName(reported), "https://purelic.net/players/" + NickUtils.getRealName(reported), "https://crafatar.com/renders/head/" + reported.getUniqueId().toString() + "?size=128&overlay")
        );
        webhook.execute();
    }

}
