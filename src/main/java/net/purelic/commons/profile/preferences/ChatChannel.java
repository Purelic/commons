package net.purelic.commons.profile.preferences;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public enum ChatChannel {

    ALL,
    GLOBAL,
    PARTY,
    STAFF,
    ;

    public static List<String> getNames() {
        return Stream.of(ChatChannel.values()).map(Enum::name).collect(Collectors.toList());
    }

    public static boolean contains(String value) {
        return ChatChannel.getNames().contains(value.toUpperCase());
    }

}
