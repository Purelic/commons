package net.purelic.commons.utils.packets;

import net.purelic.commons.Commons;
import net.purelic.commons.utils.YamlObject;
import net.purelic.commons.utils.packets.constants.HologramModifiers;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Hologram extends YamlObject<HologramModifiers> {

    private final double x;
    private final double y;
    private final double z;
    private final Location base;
    private final HashMap<Integer, HologramLine> lines;

    public Hologram(NPC npc) {
        super(new HashMap<>());
        this.x = npc.getX();
        this.y = npc.getY() + 1.5;
        this.z = npc.getZ();
        this.base = new Location(Commons.getLobby(), this.x, this.y, this.z);
        this.lines = new HashMap<>();
    }

    public Hologram(Map<String, Object> yaml) {
        super(yaml);
        this.x = this.get(HologramModifiers.X);
        this.y = this.get(HologramModifiers.Y);
        this.z = this.get(HologramModifiers.Z);
        this.base = new Location(Commons.getLobby(), this.x, this.y, this.z);
        this.lines = new HashMap<>();

        List<String> lines = this.get(HologramModifiers.LINES);
        this.set(lines.toArray(new String[0]));
    }

    public void set(String... lines) {
        // remove all the existing stands if the number of lines are different
        if (lines.length != this.lines.size()) {
            this.remove();
        }

        // read the lines backwards since we create holograms from the bottom up
        int index = 0;
        for (int i = lines.length - 1; i >= 0; i--) {
            this.updateLine(index, lines[i]);
            index++;
        }
    }

    public void show() {
        for (HologramLine line : this.lines.values()) line.show();
    }

    public void show(Player player) {
        for (HologramLine line : this.lines.values()) line.show(player);
    }

    public void remove() {
        for (HologramLine line : this.lines.values()) line.remove();
        this.lines.clear();
    }

    public void removeLine(int index) {
        HologramLine line = this.lines.remove(index);
        if (line != null) line.remove();
    }

    public void updateLine(int index, String name) {
        HologramLine line = this.lines.get(index);

        if (line != null) {
            line.update(name);
        } else {
            double offset = 0.28 * (index + 1);
            this.lines.put(index, new HologramLine(this.base.clone().add(0, offset, 0), name));
        }
    }

}