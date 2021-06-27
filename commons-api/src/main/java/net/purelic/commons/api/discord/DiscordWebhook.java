package net.purelic.commons.api.discord;

import net.purelic.commons.api.discord.embed.EmbedObject;

public interface DiscordWebhook {

    void addEmbed(EmbedObject embedObject);

    void execute();
}
