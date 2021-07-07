package net.purelic.commons.utils.packets;

import com.mojang.authlib.properties.Property;
import com.mojang.authlib.properties.PropertyMap;
import net.minecraft.server.v1_8_R3.*;
import net.purelic.commons.utils.TaskUtils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_8_R3.scoreboard.CraftScoreboard;
import org.bukkit.entity.Player;

import java.util.Collections;
import java.util.UUID;

public class FakePlayer {

    private final EntityPlayer entity;
    private final Packet<?>[] packets;

    public FakePlayer(Location location, String name, Property skin) {
        this(location, PacketUtils.getEntityPlayer(location, PacketUtils.getGameProfile(UUID.randomUUID(), name, skin)));
    }

    public FakePlayer(Location location, String name, Player player) {
        this(location, PacketUtils.getEntityPlayer(location, PacketUtils.getGameProfile(UUID.randomUUID(), name, player)));
    }

    public FakePlayer(Location location, EntityPlayer fakePlayer) {
        this.entity = fakePlayer;

        this.entity.setLocation(
            location.getX(),
            location.getY(),
            location.getZ(),
            location.getYaw(),
            location.getPitch()
        );

        DataWatcher watcher = this.entity.getDataWatcher();
        watcher.watch(10, (byte) 127);

        // Use packet scoreboard to hide the fake player's name tag
        ScoreboardTeam team = new ScoreboardTeam(((CraftScoreboard) Bukkit.getScoreboardManager().getMainScoreboard()).getHandle(), this.entity.getName());
        team.setNameTagVisibility(ScoreboardTeamBase.EnumNameTagVisibility.NEVER);
        team.setPrefix(ChatColor.BLACK + "");

        this.packets = new Packet<?>[]{
            // When the player is hidden it can sometimes break the skin, this packet will fix that
            new PacketPlayOutPlayerInfo(PacketPlayOutPlayerInfo.EnumPlayerInfoAction.ADD_PLAYER, this.entity),

            // Spawn the fake player
            new PacketPlayOutNamedEntitySpawn(this.entity),

            // Rotate the player's head
            new PacketPlayOutEntityHeadRotation(this.entity, (byte) (location.getYaw() * 256 / 360)),

            // Show the second skin layer
            new PacketPlayOutEntityMetadata(this.entity.getId(), watcher, true),

            // Hide the name tag
            new PacketPlayOutScoreboardTeam(team, 1), // remove team (if exists)
            new PacketPlayOutScoreboardTeam(team, 0), // create the team
            new PacketPlayOutScoreboardTeam(team, Collections.singleton(this.entity.getName()), 3), // add entity to the team
        };
    }

    public int getEntityId() {
        return this.entity.getId();
    }

    public void setSkin(Player player) {
        PropertyMap pm = this.entity.getProfile().getProperties();
        pm.remove("textures", pm.get("textures").iterator().next());
        pm.put("textures", PacketUtils.getSkinProperty(player));
    }

    public void show(Player player) {
        PacketUtils.sendPackets(player, this.packets);
        // removing the npc from tab too soon can sometimes fail to load the skin
        TaskUtils.runLaterAsync(() -> PacketUtils.sendPackets(player, new PacketPlayOutPlayerInfo(PacketPlayOutPlayerInfo.EnumPlayerInfoAction.REMOVE_PLAYER, this.entity)), 80L);
    }

    public void remove() {
        Bukkit.getOnlinePlayers().forEach(this::remove);
    }

    public void remove(Player player) {
        PacketUtils.sendPackets(player,
            new PacketPlayOutEntityStatus(this.entity, (byte) 3), // kill the player
            new PacketPlayOutPlayerInfo(PacketPlayOutPlayerInfo.EnumPlayerInfoAction.REMOVE_PLAYER, this.entity) // remove from tab
        );
    }

}
