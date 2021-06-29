package net.purelic.api;

import net.purelic.api.discord.DiscordWebhook;
import net.purelic.api.profile.PlayerDatabaseProfile;

import java.util.UUID;
import java.util.concurrent.atomic.AtomicReference;

/**
 * Access point to all non-pure functionality
 */
public interface Commons {

    AtomicReference<Commons> GLOBAL = new AtomicReference<>(null);

    DiscordWebhook getDiscordWebhook();

    PlayerDatabaseProfile getProfile(UUID uuid);

    @SuppressWarnings({"unchecked", "deprecation"}) //newInstance is deprecated since java 9
    static Commons getImpl() throws IllegalStateException{
        if(GLOBAL.get() == null) {
            Class<? extends Commons> clazz = null;

            try {
                clazz = (Class<? extends Commons>) Class.forName("net.purelic.api.impl.Commons");
            } catch (ClassNotFoundException | ClassCastException e) {
                throw new IllegalStateException("Fatal error when trying to retrieve the Commons impl", e);
            }

            try {
                GLOBAL.set(clazz.newInstance());
            }catch (InstantiationException | IllegalAccessException e){
                throw new IllegalStateException("Fatal error when trying to initialize the Commons impl", e);
            }
        }

        return GLOBAL.get();
    }

}
