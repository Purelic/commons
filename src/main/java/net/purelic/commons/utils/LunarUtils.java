package net.purelic.commons.utils;

public class LunarUtils {

    public static boolean isLoaded() {
        try {
            Class.forName("com.lunarclient.bukkitapi.LunarClientAPI");
            return true;
        } catch (ClassNotFoundException e) {
            return false;
        }
    }

}
