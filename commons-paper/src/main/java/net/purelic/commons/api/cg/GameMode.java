package net.purelic.commons.api.cg;

/**
 * A GameMode is a combination of a {@link GameType} and a set of rules for that game type.
 */
public interface GameMode {

    GameType getType();
}
