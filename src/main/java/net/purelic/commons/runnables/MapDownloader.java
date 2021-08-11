package net.purelic.commons.runnables;

import net.purelic.commons.utils.MapUtils;
import net.purelic.commons.utils.TaskUtils;
import org.bukkit.scheduler.BukkitRunnable;

public class MapDownloader extends BukkitRunnable {

    private final String map;
    private final boolean load;
    private final boolean lobby;

    public MapDownloader(String map, boolean lobby) {
        this(map, true, lobby);
    }

    public MapDownloader(String map, boolean load, boolean lobby) {
        this.map = map;
        this.load = load;
        this.lobby = lobby;
    }

    @Override
    public void run() {
        String downloaded = this.lobby ? MapUtils.downloadLobbyMap(this.map) : MapUtils.downloadPublicMap(this.map);
        if (downloaded != null && this.load) TaskUtils.runAsync(new MapLoader(this.map, this.lobby));
    }

}
