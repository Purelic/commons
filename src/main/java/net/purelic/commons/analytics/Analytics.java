package net.purelic.commons.analytics;

import com.google.cloud.Timestamp;
import com.google.common.collect.ImmutableMap;
import com.rudderstack.sdk.java.RudderAnalytics;
import com.rudderstack.sdk.java.messages.TrackMessage;
import net.purelic.commons.Commons;
import org.bukkit.Bukkit;
import org.bukkit.configuration.Configuration;
import org.bukkit.entity.Player;

import java.util.Map;

public class Analytics {

    private static RudderAnalytics analytics = null;

    public static void connectAnalytics(Configuration config) {
        analytics = RudderAnalytics.builder(
            config.getString("analytics.write_key"),
            config.getString("analytics.data_plane_url")
        ).build();
    }

    public static void track(AnalyticsEvent event) {
        if (analytics == null) return;

        TrackMessage.Builder track = TrackMessage.builder(event.getName())
            .timestamp(Timestamp.now().toDate())
            .userId(event.getPlayerId().toString());

        Player player = Bukkit.getPlayer(event.getPlayerId());

        if (player != null) {
            track.context(ImmutableMap.<String, Object>builder().put("ip", player.getAddress().getAddress().getHostAddress()).build());
        }

        Map<String, Object> properties = event.getProperties();
        String sessionId = Commons.getProfile(event.getPlayerId()).getSessionId();

        if (sessionId != null) {
            properties.put("session_id", Commons.getProfile(event.getPlayerId()).getSessionId());
        }

        analytics.enqueue(track.properties(properties));
    }

    public static String urlBuilder(Player player, String url, String content, String... utms) {
        StringBuilder urlBuilder = new StringBuilder(url)
            .append("?uuid=").append(player.getUniqueId().toString())
            .append("&utm_source=server")
            .append("&utm_medium=chat")
            .append("&utm_content=").append(content);

        for (String utm : utms) {
            urlBuilder.append("&").append(utm);
        }

        return urlBuilder.toString();
    }

}
