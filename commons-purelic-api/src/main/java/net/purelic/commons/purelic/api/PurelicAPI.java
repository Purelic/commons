package net.purelic.commons.purelic.api;

import net.purelic.commons.purelic.api.discord.DiscordWebhook;
import net.purelic.commons.purelic.api.profile.PlayerDatabaseProfile;

import java.util.UUID;
import java.util.concurrent.atomic.AtomicReference;

/**
 * Access point to all non-pure functionality
 */
public interface PurelicAPI {

    AtomicReference<PurelicAPI> GLOBAL = new AtomicReference<>(null);

    DiscordWebhook getDiscordWebhook();

    PlayerDatabaseProfile getProfile(UUID uuid);

    @SuppressWarnings({"unchecked", "deprecation"}) //newInstance is deprecated since java 9
    static PurelicAPI getImpl() throws IllegalStateException{
        if(GLOBAL.get() == null) {
            Class<? extends PurelicAPI> clazz;

            try {
                clazz = (Class<? extends PurelicAPI>) Class.forName("net.purelic.api.impl.PurelicAPIImpl");
            } catch (ClassNotFoundException | ClassCastException e) {
                throw new IllegalStateException("Fatal error when trying to retrieve the PurelicAPI impl", e);
            }

            try {
                GLOBAL.set(clazz.newInstance());
            }catch (InstantiationException | IllegalAccessException e){
                throw new IllegalStateException("Fatal error when trying to initialize the PurelicAPI impl", e);
            }
        }

        return GLOBAL.get();
    }

}
