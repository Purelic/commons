package net.purelic.commons.utils;

public class LunarUtils {

    private static boolean isLoaded;

    static {
        try {
            Class.forName("com.lunarclient.bukkitapi.LunarClientAPI");
            isLoaded = true;
        } catch (ClassNotFoundException e) {
            isLoaded = false;
        }
    }

    public static boolean isLoaded(){
        return isLoaded;
    }

}
