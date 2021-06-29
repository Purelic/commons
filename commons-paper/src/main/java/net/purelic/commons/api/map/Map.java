package net.purelic.commons.api.map;

import org.bukkit.World;

public interface Map {

    String getName();

    World createWorld();
}
