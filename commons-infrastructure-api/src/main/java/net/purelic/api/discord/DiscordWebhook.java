package net.purelic.api.discord;

import net.purelic.api.discord.embed.EmbedObject;

/**
 * Sends {@link EmbedObject}s to a {@link WebhookDestination}
 */
public interface DiscordWebhook {

    String BASE_URL = "https://discordapp.com/api/webhooks/";
    String AVATAR_URL = "https://purelic.net/siteicon.png";

    /**
     * Add an embed to the outgoing queue. Use {@link #execute()} to send all queued embeds
     * @param embedObject the embed to queue for sending
     */
    void addEmbedToQueue(EmbedObject embedObject, WebhookDestination a);

    /**
     * Clear the outgoing queue for any embeds not sent
     */
    void clearQueue();

    /**
     *
     */
    void execute();
}
