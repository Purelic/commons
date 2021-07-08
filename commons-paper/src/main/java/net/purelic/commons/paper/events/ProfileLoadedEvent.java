package net.purelic.commons.paper.events;

import com.google.cloud.firestore.DocumentSnapshot;
import net.purelic.commons.paper.profile.Profile;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

import java.util.UUID;

public class ProfileLoadedEvent extends Event {

    private static final HandlerList HANDLERS = new HandlerList();

    private final UUID uuid;
    private final Profile profile;
    private final DocumentSnapshot document;
    private final boolean isNewProfile;

    public ProfileLoadedEvent(UUID uuid, Profile profile, DocumentSnapshot document, boolean isNewProfile) {
        this.uuid = uuid;
        this.profile = profile;
        this.document = document;
        this.isNewProfile = isNewProfile;
    }

    public UUID getUniqueId() {
        return this.uuid;
    }

    public Profile getProfile() {
        return this.profile;
    }

    public DocumentSnapshot getDocument() {
        return this.document;
    }

    public boolean isNewProfile() {
        return this.isNewProfile;
    }

    public HandlerList getHandlers() {
        return HANDLERS;
    }

    public static HandlerList getHandlerList() {
        return HANDLERS;
    }

}
