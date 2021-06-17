package net.purelic.commons.utils;

import java.io.File;
import java.io.IOException;

public class FileUtils {

    public static void copyDirectory(File source, File destination) {
        try {
            org.apache.commons.io.FileUtils.copyDirectory(source, destination, false);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
