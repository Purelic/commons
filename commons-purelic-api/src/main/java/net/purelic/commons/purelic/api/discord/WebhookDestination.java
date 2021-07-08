package net.purelic.api.discord;

public enum WebhookDestination {

    GUARDIAN(DiscordWebhook.BASE_URL, "Guardian", DiscordWebhook.AVATAR_URL),
    SERVER_CREATE_ALERTS(DiscordWebhook.BASE_URL, "Alert", DiscordWebhook.AVATAR_URL);

    private final String webhookUrl;
    private final String username;
    private final String avatarUrl;

    WebhookDestination(String webhookUrl, String username, String avatarUrl){
        this.webhookUrl = webhookUrl;
        this.username = username;
        this.avatarUrl = avatarUrl;
    }

    public String getWebhookUrl() {
        return webhookUrl;
    }

    public String getUsername() {
        return username;
    }

    public String getAvatarUrl() {
        return avatarUrl;
    }
}
