package net.purelic.commons.purelic;

import net.purelic.commons.purelic.api.PurelicAPI;
import net.purelic.commons.purelic.api.discord.DiscordWebhook;
import net.purelic.commons.purelic.discord.DiscordWebhookImpl;
import net.purelic.commons.purelic.api.profile.PlayerDatabaseProfile;

import java.util.UUID;

public class PurelicAPIImpl implements PurelicAPI {

    private final DiscordWebhook discordWebhook = new DiscordWebhookImpl();
    @Override
    public DiscordWebhook getDiscordWebhook() {
        return this.discordWebhook;
    }

    @Override
    public PlayerDatabaseProfile getProfile(UUID uuid) {
        return null;
    }
}
