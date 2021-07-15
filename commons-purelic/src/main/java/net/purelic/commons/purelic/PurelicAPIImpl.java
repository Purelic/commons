package net.purelic.commons.purelic;

import net.purelic.commons.purelic.api.PurelicAPI;
import net.purelic.commons.purelic.api.discord.DiscordWebhook;
import net.purelic.commons.purelic.discord.DiscordWebhookImpl;
import net.purelic.commons.purelic.api.profile.PlayerDatabaseProfile;
import net.purelic.commons.purelic.firebase.FirestoreDatabase;

import java.io.File;
import java.io.IOException;
import java.util.UUID;
import java.util.function.Function;

public class PurelicAPIImpl implements PurelicAPI {

    private final DiscordWebhook discordWebhook = new DiscordWebhookImpl();
    private final FirestoreDatabase database = new FirestoreDatabase(new File(""), UUID::toString);

    public PurelicAPIImpl() throws IOException {}

    @Override
    public DiscordWebhook getDiscordWebhook() {
        return this.discordWebhook;
    }

    @Override
    public PlayerDatabaseProfile getProfile(UUID uuid) {
        return this.database.getProfile(uuid);
    }
}
