package net.purelic.commons.utils.packets;

import net.purelic.commons.Commons;
import net.purelic.commons.utils.Fetcher;
import net.purelic.commons.utils.YamlObject;
import net.purelic.commons.utils.packets.constants.NPCModifiers;
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
    private final String skin;
    private final Hologram hologram;
    private FakePlayer entity;

    public NPC(Map<String, Object> yaml) {
        super(yaml);
        this.id = this.get(NPCModifiers.ID);
        this.x = this.get(NPCModifiers.X);
        this.y = this.get(NPCModifiers.Y);
        this.z = this.get(NPCModifiers.Z);
        this.yaw = this.get(NPCModifiers.YAW);
        this.skin = this.get(NPCModifiers.SKIN);
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

    public int getEntityId() {
        if (this.entity == null) return -1;
        return this.entity.getEntityId();
    }

    public void create() {
        Location location = new Location(Commons.getLobby(), this.x, this.y, this.z, Float.parseFloat(this.yaw + ""), 0F);

        try {
            this.entity = new FakePlayer(location, "NPC", Fetcher.getMinecraftUser(this.skin).getSkin());
            this.entity.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void show(Player player) {
        this.entity.show(player);
        this.hologram.show(player);
    }

    public void remove() {
        if (this.entity != null) this.entity.remove();
        this.hologram.remove();
    }

}
