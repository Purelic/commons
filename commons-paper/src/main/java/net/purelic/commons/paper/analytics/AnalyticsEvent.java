package net.purelic.commons.paper.analytics;

import net.purelic.commons.paper.utils.NickUtils;
import org.bukkit.entity.Player;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.UUID;

public abstract class AnalyticsEvent {

    private final String name;
    private final UUID playerId;
    protected final Map<String, Object> properties;

    public AnalyticsEvent(String name, Player player) {
        this(name, player.getUniqueId());
        this.properties.put("player_name", NickUtils.getRealName(player));
    }

    public AnalyticsEvent(String name, UUID playerId) {
        this.name = name;
        this.playerId = playerId;
        this.properties = new LinkedHashMap<>();
    }

    public String getName() {
        return this.name;
    }

    public UUID getPlayerId() {
        return this.playerId;
    }

    public Map<String, Object> getProperties() {
        return this.properties;
    }

    public void track() {
        Analytics.track(this);
    }

}
