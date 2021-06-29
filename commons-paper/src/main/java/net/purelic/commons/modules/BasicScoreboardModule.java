package net.purelic.commons.modules;

import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.TextComponent;
import net.purelic.commons.Commons;
import net.purelic.commons.events.PlayerRankChangeEvent;
import net.purelic.commons.profile.Profile;
import net.purelic.commons.utils.NickUtils;
import net.purelic.commons.utils.ServerUtils;
import net.purelic.commons.utils.VersionUtils;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.Team;

public class BasicScoreboardModule implements Module {

    private final Scoreboard board;

    public BasicScoreboardModule() {
        this.board = Bukkit.getScoreboardManager().getNewScoreboard();
    }

    private void setScoreboard(Player player) {
        this.updateTeam(player, Commons.getProfile(player));
        this.setTabHeaderFooter(player);
        player.setDisplayName(ChatColor.AQUA + NickUtils.getRealName(player));
        player.setScoreboard(this.board);
    }

    private void addTeam(Player player) {
        String name = player.getName();
        Team team = this.board.getTeam(name);

        if (team == null) team = this.board.registerNewTeam(name);

        team.addEntry(name);
    }

    private void updateTeam(Player player, Profile profile) {
        this.addTeam(player); // creates a scoreboard team (if one doesn't already exist)
        this.board.getTeam(player.getName()).setPrefix(profile.getFlairs(true) + ChatColor.AQUA);
    }

    private void removeTeam(Player player) {
        this.board.getTeam(player.getName()).unregister();
    }

    private void setTabHeaderFooter(Player player) {
        if (VersionUtils.isLegacy(player)) return;

        String header = ServerUtils.isPrivate() ?
            ServerUtils.getName() + "'s Server" : "PuRelic";

        TextComponent tabHeader = new TextComponent(header);
        tabHeader.setBold(true);

        TextComponent tabFooter = new TextComponent("purelic.net");
        tabFooter.setColor(ChatColor.GRAY);

        player.setPlayerListHeaderFooter(tabHeader, tabFooter);
    }

    @EventHandler (priority = EventPriority.LOW)
    public void onPlayerJoin(PlayerJoinEvent event) {
        this.setScoreboard(event.getPlayer());
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
        this.removeTeam(event.getPlayer());
    }

    @EventHandler
    public void onPlayerRankChange(PlayerRankChangeEvent event) {
        this.updateTeam(event.getPlayer(), event.getProfile());
    }

}
