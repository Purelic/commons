package net.purelic.commons.listeners;

import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.QueryDocumentSnapshot;
import com.google.cloud.firestore.QuerySnapshot;
import com.google.common.collect.ImmutableMap;
import net.purelic.commons.events.NameChangedEvent;
import net.purelic.commons.utils.DatabaseUtils;
import net.purelic.commons.utils.Fetcher;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import java.util.List;
import java.util.UUID;
import java.util.concurrent.ExecutionException;

public class NameChangeEvent implements Listener {

    @EventHandler
    public void onNameChange(NameChangedEvent event) {
        // get all documents/profiles with the name equal to this name but the uuid is different
        // if the uuid is different force update that other profile's name

        // Find all the documents that match this players name
        ApiFuture<QuerySnapshot> future =
            DatabaseUtils.getFirestore().collection("players")
                .whereEqualTo("name_lower", event.getName().toLowerCase())
                .get();

        List<QueryDocumentSnapshot> documents;

        try {
            documents = future.get().getDocuments();
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
            return;
        }

        for (QueryDocumentSnapshot snapshot : documents) {
            UUID uuid = UUID.fromString(snapshot.getId());

            // If a document with a different uuid but with the same name exists, force update their name
            if (!uuid.equals(event.getId())) {
                String name = Fetcher.getNameOf(uuid);

                if (name != null) { // shouldn't ever happen
                    DatabaseUtils.update(
                        UUID.fromString(snapshot.getId()),
                        ImmutableMap.of("name", name, "name_lower", name.toLowerCase())
                    );
                }
            }
        }
    }

}
