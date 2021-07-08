package net.purelic.commons.paper.utils;

import org.apache.commons.io.IOUtils;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Enumeration;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import java.util.zip.ZipOutputStream;

public class ZipUtils {

    public static void zip(String sourceDirPath, String zipFilePath) throws IOException {
        Path p = Files.createFile(Paths.get(zipFilePath));
        try (ZipOutputStream zs = new ZipOutputStream(Files.newOutputStream(p))) {
            Path pp = Paths.get(sourceDirPath);
            Files.walk(pp)
                .filter(path -> !Files.isDirectory(path))
                .forEach(path -> {
                    ZipEntry zipEntry = new ZipEntry(pp.relativize(path).toString());
                    try {
                        String fileName = path.getFileName().toString();
                        if (fileName.equals("level.dat")
                            || fileName.startsWith("r.")
                            || fileName.equals("map.yml")) {
                            zs.putNextEntry(zipEntry);
                            Files.copy(path, zs);
                            zs.closeEntry();
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                });
        }
    }

    public static void unzip(File source, File destination) {
        try (ZipFile zip = new ZipFile(source)) {
            Enumeration<? extends ZipEntry> entries = zip.entries();

            while (entries.hasMoreElements()) {
                ZipEntry entry = entries.nextElement();
                File entryDestination = new File(destination, entry.getName().replace("\\", File.separator));

                if (entry.isDirectory()) {
                    entryDestination.mkdirs();
                } else {
                    entryDestination.getParentFile().mkdirs();
                    try (InputStream in = zip.getInputStream(entry); OutputStream out = new FileOutputStream(entryDestination)) {
                        IOUtils.copy(in, out);
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        source.delete();
    }

}
