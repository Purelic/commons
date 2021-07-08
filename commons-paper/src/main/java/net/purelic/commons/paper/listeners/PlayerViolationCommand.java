package net.purelic.commons.paper.listeners;

import me.vagdedes.spartan.api.PlayerViolationCommandEvent;
import me.vagdedes.spartan.system.Enums;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.purelic.commons.paper.Commons;
import net.purelic.commons.paper.profile.Profile;
import net.purelic.commons.paper.utils.ChatUtils;
import net.purelic.commons.paper.utils.DiscordWebhook;
import net.purelic.commons.paper.utils.Fetcher;
import net.purelic.commons.paper.utils.NickUtils;
import net.purelic.commons.paper.utils.ServerUtils;
import net.purelic.commons.paper.utils.constants.ViolationCategory;
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
        String hack = this.formatHackType(event.getHackType());
        ViolationCategory category = this.getViolationCategory(event.getCommand());

        // Auto-Ban if we're certain the player is cheating
        if (category == ViolationCategory.Absolute) {
            Commons.sendSpringMessage(player, "AutoBan", player.getUniqueId().toString(), "Hacking - " + hack);
            return;
        }

        for (Player online : Bukkit.getOnlinePlayers()) {
            Profile profile = Commons.getProfile(online);

            if (!profile.isMod()) continue;

            ChatUtils.sendMessage(online,
                new ComponentBuilder("GUARDIAN  ").color(ChatColor.RED).bold(true).create()[0],
                Fetcher.getFancyName(player),
                new ComponentBuilder(ChatColor.GRAY + " " + ChatUtils.ARROW + ChatColor.WHITE + " " + hack + " (" + category + ")").create()[0],
                new ComponentBuilder(" (" + Commons.getPing(player) + "ms / " + Commons.getTPS() + " TPS)").color(ChatColor.GRAY).create()[0]);

            online.playSound(online.getLocation(), Sound.BLAZE_DEATH, 2F, 1F);
        }

        this.sendDiscordReport(player, hack, category);
    }

    private ViolationCategory getViolationCategory(String command) {
        return ViolationCategory.valueOf(command.split(" do ")[2].trim());
    }

    private String formatHackType(Enums.HackType hackType) {
        return hackType.name().replaceAll("([A-Z])", " $1").trim();
    }

    private void sendDiscordReport(Player reported, String hack, ViolationCategory violationCategory) {
        DiscordWebhook webhook = new DiscordWebhook(this.webhook, "Guardian");
        webhook.addEmbed(new DiscordWebhook.EmbedObject()
            .setColor(Color.ORANGE)
            .addField("Nickname", NickUtils.isNicked(reported) ? NickUtils.getNick(reported) : "N/A", false)
            .addField("Hack Type", hack, false)
            .addField("Violation Category", violationCategory.name(), false)
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
