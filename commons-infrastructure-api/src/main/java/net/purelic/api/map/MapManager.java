package net.purelic.api.map;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

public interface MapManager {


    //TODO: Map object instead of reference to folder name(merges file and yml)

    String[] downloadPublicMaps();

    List<String> downloadPublishedMaps(UUID uuid, Set<String> maps);

    String downloadPublicMap(String map);

    String downloadPublishedMap(UUID uuid, String map);

    String downloadDraftMap(UUID uuid, String map);

    Map<String, Object> downloadPlaylist(String playlist);  //TODO Playlist object

    Map<String, Object> getMapYaml(String map);

    void saveDraft(String map, UUID uuid);

    void publishMap(String map, UUID uuid);

    void pushMap(String map);

    void deleteWorld(String world); //TODO: maybe this belongs somewhere else?

    void deleteMap(UUID uuid, String map, boolean published);

    Set<String> listDrafts(UUID uuid);

    Set<String> listPublishedMaps(UUID uuid);

    boolean isPublicMapName(String name);

    Set<String> listMaps(String path);

    String getDownloadLink(UUID uuid, String map);

    void copyMap(String map, String id);

    void copyMap(String map, String id, boolean subfolder);

    void removeAllMaps();

    boolean isReservedMapName(String name);
}
