package net.purelic.commons.runnables;

import net.purelic.commons.Commons;
import net.purelic.commons.events.MapLoadEvent;
import net.purelic.commons.utils.MapUtils;
import net.purelic.commons.utils.NullChunkGenerator;
import net.purelic.commons.utils.TaskUtils;
import org.bukkit.World;
import org.bukkit.WorldCreator;
import org.bukkit.scheduler.BukkitRunnable;

public class MapLoader extends BukkitRunnable {

    private final boolean lobby;
    private final String map;
    private final String id;
    private final boolean copy;

    public MapLoader(String map, boolean lobby) {
        this(map, map, lobby);
    }

    public MapLoader(String map, boolean copy, boolean lobby) {
        this(map, map, copy, lobby);
    }

    public MapLoader(String map, String id, boolean lobby) {
        this(map, id, true, lobby);
    }

    public MapLoader(String map, String id, boolean copy, boolean lobby) {
        this.lobby = lobby;
        this.map = map;
        this.id = id;
        this.copy = copy;
    }

    @Override
    public void run() {
        if (this.copy) MapUtils.copyMap(this.map, this.id);

        TaskUtils.run(() -> {
            World world = (new WorldCreator(this.id)).generator(new NullChunkGenerator()).createWorld();
            if (!this.lobby) Commons.callEvent(new MapLoadEvent(this.map, world));
        });
    }

}
