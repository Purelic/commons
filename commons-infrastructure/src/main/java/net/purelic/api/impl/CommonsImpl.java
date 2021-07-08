package net.purelic.api.impl;

import net.purelic.api.Commons;
import net.purelic.api.discord.DiscordWebhook;
import net.purelic.api.impl.discord.DiscordWebhookImpl;
import net.purelic.api.profile.PlayerDatabaseProfile;

import java.util.UUID;

public class CommonsImpl implements Commons {

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
