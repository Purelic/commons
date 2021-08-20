package net.purelic.commons.utils;

import com.google.api.core.ApiFuture;
import com.google.auth.oauth2.GoogleCredentials;
import com.google.cloud.Timestamp;
import com.google.cloud.firestore.*;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.cloud.FirestoreClient;
import net.purelic.commons.Commons;
import net.purelic.commons.events.NameChangedEvent;
import net.purelic.commons.events.ProfileLoadedEvent;
import net.purelic.commons.events.ServerReadyEvent;
import net.purelic.commons.profile.Profile;
import net.purelic.commons.utils.constants.ServerStatus;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ExecutionException;

public class DatabaseUtils {

    private static Firestore database;
    private static Map<UUID, DocumentReference> documents;
    public static DocumentReference serverDoc;

    public static void connectDatabase() {
        try {
            String authPath = Commons.getPlugin().getDataFolder() + "/purelic-firebase-adminsdk.json";
            InputStream serviceAccount = new FileInputStream(authPath);
            GoogleCredentials credentials = GoogleCredentials.fromStream(serviceAccount);
            FirebaseOptions options = FirebaseOptions.builder().setCredentials(credentials).build();
            FirebaseApp.initializeApp(options);
            database = FirestoreClient.getFirestore();
            documents = new HashMap<>();
            loadServerDoc();
        } catch (IOException e) {
            System.out.println("Error connecting to the database. Shutting down server...");
            e.printStackTrace();
            Bukkit.getServer().shutdown();
        }
    }

    public static Firestore getFirestore() {
        return database;
    }

    public static DocumentSnapshot loadProfile(UUID uuid, String name, boolean updateLastSeen) {
        try {
            DocumentReference docRef = database.collection("players").document(uuid.toString());
            ApiFuture<DocumentSnapshot> future = docRef.get();
            DocumentSnapshot document = future.get();
            Timestamp now = Timestamp.now();

            if (document.exists()) {
                Profile profile = Commons.addProfile(uuid, document.toObject(Profile.class));

                if (updateLastSeen) {
                    docRef.update("last_seen", now);
                    profile.setLastSeen(now);
                }

                if (name != null && !name.equals(document.getString("name"))) {
                    // Name change
                    docRef.update("name", name);
                    docRef.update("name_lower", name.toLowerCase());
                    profile.setName(name);
                    profile.setNameLower(name.toLowerCase());
                    Commons.callEvent(new NameChangedEvent(uuid, name));
                }

                Commons.callEvent(new ProfileLoadedEvent(uuid, profile, document, false));
            } else {
                if (name == null) return null;

                Profile profile = Commons.addProfile(uuid, new Profile());
                profile.setJoined(now);
                profile.setLastSeen(now);
                profile.setName(name);
                profile.setNameLower(name.toLowerCase());

                Map<String, Object> data = new HashMap<>();
                data.put("joined", now);
                data.put("last_seen", now);
                data.put("time_played", 0);
                data.put("name", name);
                data.put("name_lower", name.toLowerCase());
                docRef.set(data);

                Commons.callEvent(new ProfileLoadedEvent(uuid, profile, document, true));
            }

            documents.put(uuid, docRef);
            return document;
        } catch (ExecutionException | InterruptedException e) {
            System.out.println("Error loading profile for player " + uuid.toString());
            e.printStackTrace();
            return null;
        }
    }

    public static Map<String, Object> getOfflineProfile(UUID uuid) {
        DocumentReference docRef = database.collection("players").document(uuid.toString());
        ApiFuture<DocumentSnapshot> future = docRef.get();

        try {
            DocumentSnapshot document = future.get();
            return document.exists() ? document.getData() : null;
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static Map<String, Object> getOfflineProfile(String name) {
        ApiFuture<QuerySnapshot> future =
            database.collection("players")
                .whereEqualTo("name_lower", name.toLowerCase())
                .get();

        try {
            QueryDocumentSnapshot d = future.get().getDocuments().get(0);
            Map<String, Object> data = d.getData();
            data.put("uuid", d.getId());
            return data;
        } catch (Exception e) {
            // no document found
            return null;
        }
    }

    public static void arrayUnion(Profile profile, String field, Object value) {
        DocumentReference docRef = documents.get(profile.getUniqueId());
        docRef.update(field, FieldValue.arrayUnion(value));
    }

    public static void arrayRemove(Profile profile, String field, Object value) {
        DocumentReference docRef = documents.get(profile.getUniqueId());
        docRef.update(field, FieldValue.arrayRemove(value));
    }

    public static void update(UUID uuid, Map<String, Object> values) {
        DocumentReference docRef = documents.get(uuid);
        docRef.update(values);
    }

    public static void update(Profile profile, String field, Object value) {
        DocumentReference docRef = documents.get(profile.getUniqueId());
        if (value == null) {
            Map<String, Object> updates = new HashMap<>();
            updates.put(field, FieldValue.delete());
            docRef.update(updates);
        } else {
            docRef.update(field, value);
        }
    }

    public static void update(Profile profile, String field, Map<String, Object> values) {
        DocumentReference docRef = documents.get(profile.getUniqueId());
        docRef.update(field, values);
    }

    public static void setLastSeenToNow(UUID uuid) {
        Timestamp now = Timestamp.now();
        documents.get(uuid).update("last_seen", now);
        Commons.getProfile(uuid).setLastSeen(now);
    }

    public static void updateLastSeen(Player player) {
        DocumentReference docRef = database.collection("players").document(player.getUniqueId().toString());

        Profile profile = Commons.getProfile(player);
        Timestamp lastSeen = profile.getLastSeen();
        Timestamp now = Timestamp.now();

        docRef.update("last_seen", now);
        docRef.update("time_played", FieldValue.increment(now.getSeconds() - lastSeen.getSeconds()));
    }

    public static List<QueryDocumentSnapshot> getServers() {
        ApiFuture<QuerySnapshot> future = database.collection("servers").get();

        try {
            return future.get().getDocuments();
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static List<QueryDocumentSnapshot> getGameModes(UUID uuid) {
        return getGameModes(uuid.toString());
    }

    public static List<QueryDocumentSnapshot> getGameModes(String uuid) {
        ApiFuture<QuerySnapshot> future =
            database.collection("game_modes")
                .whereEqualTo("author", uuid)
                .get();
        try {
            return future.get().getDocuments();
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static QueryDocumentSnapshot getGameMode(String author, String name, boolean alias) {
        ApiFuture<QuerySnapshot> future =
            database.collection("game_modes")
                .whereEqualTo("author", author)
                .whereEqualTo(alias ? "alias" : "name", alias ? name.toUpperCase() : name)
                .get();

        try {
            return future.get().getDocuments().get(0);
        } catch (Exception e) {
            // e.printStackTrace();
            // No game mode found
            return null;
        }
    }

    public static void updateGameMode(String id, Map<String, Object> data) {
        database.collection("game_modes").document(id).set(data);
    }

    public static void deleteGameMode(String id) {
        database.collection("game_modes").document(id).delete();
    }

    public static DocumentSnapshot getPrivateServer(Player player) {
        DocumentReference docRef = database.collection("servers").document(player.getUniqueId().toString());
        ApiFuture<DocumentSnapshot> future = docRef.get();

        try {
            return future.get();
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static void setServerOnline() {
        Commons.callEvent(new ServerReadyEvent());

        if (serverDoc != null) {
            Map<String, Object> data = new HashMap<>();
            data.put("online", true);

            if (ServerUtils.getPlaylist() != null) {
                data.put("status", ServerStatus.STARTING.name());
                data.put("map", null);
                data.put("game_mode", null);
            } else {
                data.put("status", ServerStatus.STARTED.name());
            }

            DatabaseUtils.serverDoc.update(data);
        }
    }

    public static void loadServerDoc() {
        try (final DatagramSocket socket = new DatagramSocket()) {
            socket.connect(InetAddress.getByName("8.8.8.8"), 10002);

            String ip = socket.getLocalAddress().getHostAddress();
            String id = DatabaseUtils.getServerIdByIp(ip);

            if (id != null) {
                DatabaseUtils.serverDoc = database.collection("servers").document(id);
            }
        } catch (UnknownHostException | SocketException e) {
            e.printStackTrace();
        }
    }

    public static void unlockServer() {
        if (DatabaseUtils.serverDoc == null) return;
        DatabaseUtils.serverDoc.update("locked", false);
    }

    public static String getServerIdByIp(String ip) {
        ApiFuture<QuerySnapshot> future =
            database.collection("server_ips")
                .whereEqualTo("ip", ip)
                .get();

        try {
            List<QueryDocumentSnapshot> docs = future.get().getDocuments();
            return docs.size() > 0 ? docs.get(0).getId() : null;
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static void setServerShutdown() {
        if (DatabaseUtils.serverDoc != null) {
            DatabaseUtils.serverDoc.update("shutdown", true);
        }
    }

    public static void updatePlayerCount(int players) {
        if (DatabaseUtils.serverDoc != null) {
            DatabaseUtils.serverDoc.update("players_online", players);
        }
    }

    public static void updateStatus(ServerStatus status, String map, String gameMode) {
        if (DatabaseUtils.serverDoc != null) {
            Map<String, Object> data = new HashMap<>();
            data.put("status", status.name());
            data.put("map", map);
            data.put("game_mode", gameMode);
            DatabaseUtils.serverDoc.update(data);
        }
    }

    public static DocumentReference getProfileDocRef(Player player) {
        return getProfileDocRef(player.getUniqueId());
    }

    public static DocumentReference getProfileDocRef(UUID uuid) {
        return documents.get(uuid);
    }

    public static String getPartyId(Player player) {
        return getPartyId(player.getUniqueId());
    }

    public static String getPartyId(UUID uuid) {
        ApiFuture<DocumentSnapshot> future = DatabaseUtils.getProfileDocRef(uuid).get();

        try {
            DocumentSnapshot document = future.get();

            if (document.exists()) {
                return document.getString("party_id");
            } else {
                return null;
            }
        } catch (InterruptedException | ExecutionException e) {
            return null;
        }
    }

}
