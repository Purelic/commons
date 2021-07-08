package net.purelic.commons.paper.utils;

import com.dropbox.core.DbxDownloader;
import com.dropbox.core.DbxException;
import com.dropbox.core.DbxRequestConfig;
import com.dropbox.core.v2.DbxClientV2;
import com.dropbox.core.v2.files.DeleteErrorException;
import com.dropbox.core.v2.files.FileMetadata;
import com.dropbox.core.v2.files.WriteMode;
import net.purelic.commons.paper.Commons;
import org.apache.commons.io.FileUtils;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.configuration.Configuration;
import org.yaml.snakeyaml.Yaml;

import java.io.*;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Collectors;

public class MapUtils {

    private static final List<String> RESERVED_MAP_NAMES = new ArrayList<>(Arrays.asList("lobby", "hub", "public"));
    private static final List<String> SAVE_FOLDERS = new ArrayList<>(Arrays.asList("logs", "plugins", "cache", "world", "void", "world_nether", "world_the_end"));

    private static final String PLAYLIST_URL = "https://raw.githubusercontent.com/PuRelic/playlists/main/%PLAYLIST%.yml";
    private static final String REPO_PATH = "/Map Repository/Public";

    private static final String ROOT = Commons.getRoot();
    private static final String MAPS_PATH = ROOT + "Maps";

    private static DbxClientV2 DROPBOX;
    private static Set<String> publicMapCache = new HashSet<>();

    public static void connectDropbox(Configuration config) {
        DbxRequestConfig dbxRequestConfig = DbxRequestConfig.newBuilder("dropbox/java-tutorial").build();
        DROPBOX = new DbxClientV2(dbxRequestConfig, config.getString("dropbox_token"));
    }

    public static String[] downloadPublicMaps() {
        downloadZip(REPO_PATH, "Maps.zip");
        unzipFile("Maps");
        return unzipMaps();
    }

    public static List<String> downloadPublishedMaps(UUID uuid, Set<String> maps) {
        List<String> lower = maps.stream().map(String::toLowerCase).collect(Collectors.toList());
        Set<String> publishedMaps = listPublishedMaps(uuid);
        List<String> downloaded = new ArrayList<>();

        for (String map : publishedMaps) {
            if (lower.contains(map.toLowerCase())) continue; // skip maps already downloaded
            downloaded.add(downloadPublishedMap(uuid, map));
        }

        return downloaded;
    }

    private static void downloadZip(String path, String out) {
        try (FileOutputStream outputStream = new FileOutputStream(new File(ROOT, out))) {
            DROPBOX.files().downloadZip(path).download(outputStream);
        } catch (DbxException | IOException e) {
            e.printStackTrace();
        }
    }

    public static String downloadPublicMap(String map) {
        String path = REPO_PATH + "/" + map + ".zip";
        return downloadMap(path);
    }

    public static String downloadPublishedMap(UUID uuid, String map) {
        return downloadMap(uuid, map, "Published");
    }

    public static String downloadDraftMap(UUID uuid, String map) {
        return downloadMap(uuid, map, "Drafts");
    }

    private static String downloadMap(UUID uuid, String map, String folder) {
        String path = "/Map Repository/Private/" + uuid.toString() + "/" + folder + "/" + map + ".zip";
        return downloadMap(path);
    }

    private static String downloadMap(String path) {
        String mapName;

        new File(MAPS_PATH).mkdir();

        try {
            DbxDownloader<FileMetadata> downloader = DROPBOX.files().download(path);
            String fileName = downloader.getResult().getName();

            try (FileOutputStream outputStream = new FileOutputStream(new File(MAPS_PATH, fileName))) {
                downloader.download(outputStream);
            }

            mapName = fileName.replaceAll(".zip", "");
        } catch (DbxException | IOException e) {
            // could not find map
            e.printStackTrace();
            return null;
        }

        unzipFile("Maps/" + mapName);

        return mapName;
    }

    private static void unzipFile(String path) {
        ZipUtils.unzip(new File(ROOT + path + ".zip"), new File(ROOT + path));
    }

    private static String[] unzipMaps() {
        File downloads = new File(ROOT, "Maps/Public");
        File[] files = downloads.listFiles();
        int total = files.length;
        String[] maps = new String[total];

        for (int i = 0; i < total; i++) {
            File file = files[i];
            String mapName = file.getName().replace(".zip", "");
            maps[i] = mapName;

            File source = new File(ROOT, "Maps/Public/" + mapName + ".zip");
            File destination = new File(ROOT, "Maps/" + mapName);
            ZipUtils.unzip(source, destination);
        }

        downloads.delete();

        return maps;
    }

    public static Map<String, Object> downloadPlaylist(String playlist) {
        String url = PLAYLIST_URL.replaceAll("%PLAYLIST%", playlist).replaceAll(" ", "%20");

        try (InputStream inputStream = new URL(url).openStream()) {
            Map<String, Object> data = (Map<String, Object>) new Yaml().load(inputStream);
            inputStream.close();
            return data;
        } catch (IOException e) {
            e.printStackTrace();
            return new HashMap<>();
        }
    }

    public static Map<String, Object> getMapYaml(String map) {
        try (FileInputStream inputStream = new FileInputStream(new File(MAPS_PATH, map + "/map.yml"))) {
            Map<String, Object> data = (Map<String, Object>) new Yaml().load(inputStream);
            inputStream.close();
            return data;
        } catch (IOException e) {
            e.printStackTrace();
            return new HashMap<>();
        }
    }

    public static void saveDraft(World world, UUID uuid) {
        String map = world.getName();
        world.save();
        saveDraft(map, uuid);
    }

    public static void saveDraft(String map, UUID uuid) {
        saveMap(map, uuid, "Drafts");
    }

    public static void publishMap(String map, UUID uuid) {
        saveMap(map, uuid, "Published");
    }

    public static void pushMap(String map) {
        String path = MAPS_PATH + "/" + map + ".zip";
        String dest = "/Map Repository/Public/" + map + ".zip";
        saveMap(map, path, dest);
    }

    private static void zipMap(String name) {
        String sourceDir = ROOT + name + "/";
        String outputFile = MAPS_PATH + "/" + name + ".zip";

        try {
            File localFile = new File(outputFile);
            if (localFile.exists()) localFile.delete();
            ZipUtils.zip(sourceDir, outputFile);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void saveMap(String map, UUID uuid, String folder) {
        String path = MAPS_PATH + "/" + map + ".zip";
        String dest = "/Map Repository/Private/" + uuid.toString() + "/" + folder + "/" + map + ".zip";
        saveMap(map, path, dest);
    }

    private static void saveMap(String map, String path, String dest) {
        zipMap(map);

        File localFile = new File(path);

        try (InputStream in = new FileInputStream(localFile)) {
            DROPBOX.files().uploadBuilder(dest)
                .withMode(WriteMode.OVERWRITE)
                .withClientModified(new Date(localFile.lastModified()))
                .uploadAndFinish(in);
        } catch (DbxException ex) {
            System.err.println("Error uploading to Dropbox: " + ex.getMessage());
        } catch (IOException ex) {
            System.err.println("Error reading from file \"" + localFile + "\": " + ex.getMessage());
        }
    }

    public static void deleteWorld(World world) {
        deleteWorld(world.getName());
    }

    private static void deleteWorld(String world) {
        TaskUtils.runAsync(() -> {
            File file = new File(ROOT + world);
            Bukkit.unloadWorld(world, false);

            if (file.exists()) {
                try {
                    FileUtils.cleanDirectory(file);
                    file.delete();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    public static void deleteMap(UUID uuid, String map, boolean published) {
        String zipPath = MAPS_PATH + "/" + map + ".zip";
        String dest = "/Map Repository/Private/" + uuid.toString() + "/" + (published ? "Published" : "Drafts") + "/" + map + ".zip";
        File localZip = new File(zipPath);

        deleteWorld(map);

        try {
            if (localZip.exists()) localZip.delete();
            DROPBOX.files().deleteV2(dest);
        } catch (DeleteErrorException ignored) {
            // map was deleted, but never uploaded
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static Set<String> listDrafts(UUID uuid) {
        return listMaps("/Map Repository/Private/" + uuid.toString() + "/Drafts");
    }

    public static Set<String> listPublishedMaps(UUID uuid) {
        return listMaps("/Map Repository/Private/" + uuid.toString() + "/Published");
    }

    public static boolean isPublicMapName(String name) {
        if (publicMapCache.isEmpty()) {
            publicMapCache = listMaps("/Map Repository/Public");
        }

        return publicMapCache.stream().anyMatch(map -> map.equalsIgnoreCase(name));
    }

    private static Set<String> listMaps(String path) {
        Set<String> maps = new TreeSet<>();

        try {
            DROPBOX.files().listFolder(path).getEntries()
                .forEach(entry -> maps.add(entry.getName().replace(".zip", "")));
        } catch (Exception e) {
            // Folder doesn't exist
        }

        return maps;
    }

    public static String getDownloadLink(UUID uuid, String map) {
        String path = "/Map Repository/Private/" + uuid.toString() + "/Drafts";

        try {
            return DROPBOX.files().getTemporaryLink(path + "/" + map + ".zip").getLink();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static void copyMap(String map, String id) {
        copyMap(map, id, true);
    }

    public static void copyMap(String map, String id, boolean subfolder) {
        String path = ROOT + (subfolder ? "Maps/" : "") + map;

        try {
            Files.walk(Paths.get(path))
                .forEach(source -> {
                    Path destination = Paths.get(ROOT + id, source.toString()
                        .substring(path.length()));
                    try {
                        Files.copy(source, destination);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                });
        } catch (IOException e) {
            System.out.println("Failed to copy map folder " + map);
            e.printStackTrace();
        }
    }

    public static void removeAllMaps() {
        try {
            for (File file : Objects.requireNonNull((new File(ROOT)).listFiles())) {
                if ((file.isDirectory() && !SAVE_FOLDERS.contains(file.getName())) || file.getName().equals("Maps.zip")) {
                    Bukkit.unloadWorld(file.getName(), false);
                    FileUtils.cleanDirectory(file);
                    file.delete();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static boolean isReservedMapName(String name) {
        return RESERVED_MAP_NAMES.contains(name.toLowerCase());
    }

}
