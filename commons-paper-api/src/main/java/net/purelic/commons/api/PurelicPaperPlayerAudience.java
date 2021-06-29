package net.purelic.commons.api;

import net.kyori.adventure.audience.Audience;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.title.Title;
import org.jetbrains.annotations.NotNull;

public interface PurelicPaperPlayerAudience extends Audience {

    void sendActionBar(final @NotNull Component message, boolean includeLegacy);

    void clearActionBar();

    void sendAlertMessage(final @NotNull Component message);

    //TODO: fix new title default times
    //TODO: fancy chat message
    //TODO: Naughty messages
}
