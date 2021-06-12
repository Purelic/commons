package net.purelic.commons.utils.lunar;

import com.lunarclient.bukkitapi.LunarClientAPI;
import com.lunarclient.bukkitapi.object.LCWaypoint;
import org.bukkit.Bukkit;
import org.bukkit.Color;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class LunarWaypoint {

    private final List<UUID> viewers;
    private String name;
    private Location location;
    private int color;
    private LCWaypoint waypoint;

    public LunarWaypoint(String name, Location location, Color color) {
        this.viewers = new ArrayList<>();
        this.name = name;
        this.location = location;
        this.color = color.asRGB();
        this.waypoint = !LunarUtils.isLoaded() ? null : new LCWaypoint(name, location, this.color, true, true);
    }

    public void send(Player player) {
        if (LunarUtils.isLoaded()) LunarClientAPI.getInstance().sendWaypoint(player, this.waypoint);
    }

    public void show(Player player) {
        this.addViewer(player);
        this.send(player);
    }

    public void remove(Player player) {
        this.removeViewer(player);
        this.hide(player);
    }

    public void hide(Player player) {
        if (LunarUtils.isLoaded()) LunarClientAPI.getInstance().removeWaypoint(player, this.waypoint);
    }

    private void addViewer(Player player) {
        UUID uuid = player.getUniqueId();
        if (!this.viewers.contains(uuid)) this.viewers.add(uuid);
    }

    private void removeViewer(Player player) {
        this.viewers.remove(player.getUniqueId());
    }

    public void show() {
        Bukkit.getOnlinePlayers().forEach(this::show);
    }

    public void hide() {
        Bukkit.getOnlinePlayers().forEach(this::hide);
    }

    public void destroy() {
        this.hide();
        this.viewers.clear();
    }

    public void setName(String name) {
        this.name = name;
        this.update();
    }

    public void setLocation(Location location) {
        this.location = location;
        this.update();
    }

    public void setColor(Color color) {
        this.color = color.asRGB();
        this.update();
    }

    private void update() {
        // Remove the waypoint from all the viewers
        for (UUID uuid : this.viewers) {
            Player player = Bukkit.getPlayer(uuid);
            if (player != null) this.hide(player);
        }

        // Create a new waypoint
        this.waypoint = !LunarUtils.isLoaded() ? null : new LCWaypoint(this.name, this.location, this.color, true, true);

        // Send the new waypoint to all the viewers
        for (UUID uuid : this.viewers) {
            Player player = Bukkit.getPlayer(uuid);
            if (player != null) this.show(player);
        }
    }

}
