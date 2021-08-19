package net.purelic.commons.utils.packets;

import net.purelic.commons.Commons;
import net.purelic.commons.utils.Fetcher;
import net.purelic.commons.utils.NickUtils;
import net.purelic.commons.utils.YamlObject;
import net.purelic.commons.utils.packets.constants.NPCModifiers;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import java.io.IOException;
import java.util.Map;

public class NPC extends YamlObject<NPCModifiers> {

    private final String id;
    private final double x;
    private final double y;
    private final double z;
    private final double yaw;
    private String skin;
    private final Hologram hologram;
    private Location location;
    private FakePlayer entity;

    public NPC(Map<String, Object> yaml) {
        super(yaml);
        this.id = this.get(NPCModifiers.ID);
        this.x = this.get(NPCModifiers.X);
        this.y = this.get(NPCModifiers.Y);
        this.z = this.get(NPCModifiers.Z);
        this.yaw = this.get(NPCModifiers.YAW);
        this.skin = Commons.getPlugin().getConfig().getString("npc." + this.id, this.get(NPCModifiers.SKIN));
        this.hologram = new Hologram(this);
    }

    public String getId() {
        return this.id;
    }

    public double getX() {
        return this.x;
    }

    public double getY() {
        return this.y;
    }

    public double getZ() {
        return this.z;
    }

    public Hologram getHologram() {
        return this.hologram;
    }

    public boolean hasSkinSet() {
        return this.skin != null;
    }

    public String getSkin() {
        return this.skin;
    }

    public void setSkin(Player player) {
        this.skin = NickUtils.getRealName(player);
    }

    public int getEntityId() {
        if (this.entity == null) return -1;
        return this.entity.getEntityId();
    }

    public void create() {
        this.location = new Location(Commons.getLobby(), this.x, this.y, this.z, Float.parseFloat(this.yaw + ""), 0F);

        // If no skin is set then we replicate the player's skin when we show the entity
        if (this.skin != null) {
            try {
                this.entity = new FakePlayer(this.location, "NPC", Fetcher.getMinecraftUser(this.skin).getSkin());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        Bukkit.getOnlinePlayers().forEach(this::show);
    }

    public void show(Player player) {
        if (this.skin == null) {
            if (this.entity == null) this.entity = new FakePlayer(this.location, "NPC", player);
            else this.entity.setSkin(player);
        }

        this.entity.show(player);
        this.hologram.show(player);
    }

    public void remove() {
        this.remove(false);
    }

    public void remove(boolean entityOnly) {
        if (this.entity != null) this.entity.remove();
        if (entityOnly) return;
        this.hologram.remove();
    }

}
