package net.purelic.commons.purelic.firebase;

import com.google.api.core.ApiFuture;
import com.google.auth.oauth2.GoogleCredentials;
import com.google.cloud.Timestamp;
import com.google.cloud.firestore.DocumentReference;
import com.google.cloud.firestore.DocumentSnapshot;
import com.google.cloud.firestore.Firestore;
import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.cloud.FirestoreClient;
import net.purelic.commons.purelic.api.profile.PlayerDatabaseProfile;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ExecutionException;
import java.util.function.Function;

public class FirestoreDatabase {

    private final File authFile; //TODO: transient?
    private final Function<UUID, String> playerNameProvider;
    private final Firestore database;

    private final LoadingCache<UUID, PlayerDatabaseProfile> cache = CacheBuilder.newBuilder().build(new CacheLoader<UUID, PlayerDatabaseProfile>() {
        @Override
        public PlayerDatabaseProfile load(@NotNull UUID uuid) throws ExecutionException, InterruptedException {
            return FirestoreDatabase.this.fetchProfile(uuid);
        }
    });

    public FirestoreDatabase(File authFile, Function<UUID, String> playerNameProvider) throws IOException {
        this.authFile = authFile;
        this.playerNameProvider = playerNameProvider;

        FirebaseApp.initializeApp(FirebaseOptions.builder().setCredentials(GoogleCredentials.fromStream(new FileInputStream(authFile))).build());
        this.database = FirestoreClient.getFirestore();
    }

    public PlayerDatabaseProfile getProfile(UUID uuid) {
        return this.cache.getUnchecked(uuid);
    }

    private PlayerDatabaseProfile fetchProfile(UUID uuid) throws ExecutionException, InterruptedException {
        DocumentReference docRef = database.collection("players").document(uuid.toString());
        ApiFuture<DocumentSnapshot> future = docRef.get();
        DocumentSnapshot document = future.get();
        Timestamp now = Timestamp.now();


        if (document.exists()) {
            return document.toObject(PurelicPlayerDatabaseProfile.class);
        } else {

            String name = this.playerNameProvider.apply(uuid);
            //PlayerDatabaseProfile profile = new PurelicPlayerDatabaseProfile(uuid, name, null, null, null);
            /*profile.setJoined(now);
            profile.setLastSeen(now);
            profile.setName(name);
            profile.setNameLower(name.toLowerCase());*/

            Map<String, Object> data = new HashMap<>(); //TODO this first and then just fetch?
            data.put("joined", now);
            data.put("last_seen", now);
            data.put("time_played", 0);
            data.put("name", name);
            data.put("name_lower", name.toLowerCase());
            docRef.set(data);

            return docRef.get().get().toObject(PurelicPlayerDatabaseProfile.class);
        }
    }
}
