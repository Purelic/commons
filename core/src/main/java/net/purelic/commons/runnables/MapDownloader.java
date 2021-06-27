package net.purelic.commons.runnables;

import net.purelic.commons.utils.MapUtils;
import net.purelic.commons.utils.TaskUtils;
import org.bukkit.scheduler.BukkitRunnable;

public class MapDownloader extends BukkitRunnable {

    private final String map;
    private final boolean load;

    public MapDownloader(String map) {
        this(map, true);
    }

    public MapDownloader(String map, boolean load) {
        this.map = map;
        this.load = load;
    }

    @Override
    public void run() {
        String downloaded = MapUtils.downloadPublicMap(this.map);
        if (downloaded != null && this.load) TaskUtils.runAsync(new MapLoader(this.map));
    }

}
