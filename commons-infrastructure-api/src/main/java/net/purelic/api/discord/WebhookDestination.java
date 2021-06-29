package net.purelic.api.discord;

public enum WebhookDestination {

    GUARDIAN(DiscordWebhook.BASE_URL),
    SERVER_CREATE_ALERTS(DiscordWebhook.BASE_URL);

    private final String webhookUrl;

    WebhookDestination(String webhookUrl){
        this.webhookUrl = webhookUrl;
    }

    public String getWebhookUrl() {
        return webhookUrl;
    }
}
