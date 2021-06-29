package net.purelic.commons.api.text.chat;

import net.purelic.commons.api.text.PurelicAudience;

import java.util.List;

/**
 * Used to check messages for naughty words.
 *
 * <p>All messages sent through {@link PurelicAudience} uses these checks, but all other displays of text has
 * to implement usage of this manually.</p>
 */
public interface NaughtyMessageManager { //TODO: add access point

    /**
     * Gets all currently blocked words
     * @return a list of all blocked words
     */
    List<String> getBlockedWords();

    /**
     * Checks if the message contain any blocked words from {@link #getBlockedWords()}
     * @return true if the message contains any blocked words, false if not
     */
    boolean blockMessage(String message);

    /**
     * Gets all currently censored words
     * @return a list of all blocked words
     */
    List<String> getCensoredWords();

    /**
     * Censors any censored words in this message also from {@link #getCensoredWords()}
     * @param message the message to check for censored words
     * @return the message input with "*" switched out for censored words
     */
    String censorMessage(String message);
}
