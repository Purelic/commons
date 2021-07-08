package net.purelic.commons.paper.runnables;

import net.purelic.commons.paper.Commons;
import net.purelic.commons.paper.events.MapLoadEvent;
import net.purelic.commons.paper.utils.MapUtils;
import net.purelic.commons.paper.utils.NullChunkGenerator;
import net.purelic.commons.paper.utils.TaskUtils;
import org.bukkit.World;
import org.bukkit.WorldCreator;
import org.bukkit.scheduler.BukkitRunnable;

public class MapLoader extends BukkitRunnable {

    private final boolean lobby;
    private final String map;
    private final String id;
    private final boolean copy;

    public MapLoader(String map) {
        this(map, map);
    }

    public MapLoader(String map, boolean copy) {
        this(map, map, copy);
    }

    public MapLoader(String map, String id) {
        this(map, id, true);
    }

    public MapLoader(String map, String id, boolean copy) {
        this.lobby = map.equalsIgnoreCase("Lobby") || map.equalsIgnoreCase("Hub");
        this.map = map;
        this.id = id;
        this.copy = copy;
    }

    @Override
    public void run() {
        if (this.copy) MapUtils.copyMap(this.map, this.id);

        TaskUtils.run(() -> {
            World world = (new WorldCreator(id)).generator(new NullChunkGenerator()).createWorld();
            if (lobby) Commons.setLobby(world);
            else Commons.callEvent(new MapLoadEvent(map, world));
        });
    }

}
