package net.purelic.api.impl.discord;

import net.purelic.api.discord.DiscordWebhook;
import net.purelic.api.discord.WebhookDestination;
import net.purelic.api.discord.embed.Author;
import net.purelic.api.discord.embed.EmbedObject;
import net.purelic.api.discord.embed.Field;
import net.purelic.api.discord.embed.Footer;
import net.purelic.api.discord.embed.Image;
import net.purelic.api.discord.embed.Thumbnail;

import javax.net.ssl.HttpsURLConnection;
import java.awt.Color;
import java.io.IOException;
import java.io.OutputStream;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Queue;

public class DiscordWebhookImpl implements DiscordWebhook { //TODO: log embeds sent

    private final Map<WebhookDestination, Queue<EmbedObject>> outgoingQueues = new HashMap<>();

    @Override
    public void addEmbedToQueue(EmbedObject embedObject, WebhookDestination destination) {
        outgoingQueues.compute(destination, (hook, embedObjects) -> {
            Queue<EmbedObject> outgoingObjects = embedObjects;

            if(outgoingObjects == null) outgoingObjects = new ArrayDeque<>();
            outgoingObjects.add(embedObject);

            return outgoingObjects;
        });
    }

    @Override
    public void clearQueue() {
        for(Queue<EmbedObject> queue : outgoingQueues.values()){
            queue.clear();
        }
    }

    @Override
    public void execute() {
        if (this.outgoingQueues.isEmpty()) {
            return;
        }

        for(Map.Entry<WebhookDestination, Queue<EmbedObject>> entry : outgoingQueues.entrySet()) {

            JSONObject json = new JSONObject();

            WebhookDestination destination = entry.getKey();

            json.put("content", "");
            json.put("username", destination.getUsername());
            json.put("avatar_url", destination.getAvatarUrl());
            json.put("tts", false);//TODO

            Queue<EmbedObject> embedQueue = entry.getValue();

            if (!embedQueue.isEmpty()) {
                List<JSONObject> embedObjects = new ArrayList<>();

                while (!embedQueue.isEmpty()) {
                    EmbedObject embed = embedQueue.poll();

                    JSONObject jsonEmbed = new JSONObject();

                    jsonEmbed.put("title", embed.getTitle());
                    jsonEmbed.put("description", embed.getDescription());
                    jsonEmbed.put("url", embed.getUrl());

                    if (embed.getColor() != null) {
                        Color color = embed.getColor();
                        int rgb = color.getRed();
                        rgb = (rgb << 8) + color.getGreen();
                        rgb = (rgb << 8) + color.getBlue();

                        jsonEmbed.put("color", rgb);
                    }

                    Footer footer = embed.getFooter();
                    Image image = embed.getImage();
                    Thumbnail thumbnail = embed.getThumbnail();
                    Author author = embed.getAuthor();
                    List<Field> fields = embed.getFields();

                    if (footer != null) {
                        JSONObject jsonFooter = new JSONObject();

                        jsonFooter.put("text", footer.getText());
                        jsonFooter.put("icon_url", footer.getIconUrl());
                        jsonEmbed.put("footer", jsonFooter);
                    }

                    if (image != null) {
                        JSONObject jsonImage = new JSONObject();

                        jsonImage.put("url", image.getUrl());
                        jsonEmbed.put("image", jsonImage);
                    }

                    if (thumbnail != null) {
                        JSONObject jsonThumbnail = new JSONObject();

                        jsonThumbnail.put("url", thumbnail.getUrl());
                        jsonEmbed.put("thumbnail", jsonThumbnail);
                    }

                    if (author != null) {
                        JSONObject jsonAuthor = new JSONObject();

                        jsonAuthor.put("name", author.getName());
                        jsonAuthor.put("url", author.getUrl());
                        jsonAuthor.put("icon_url", author.getIconUrl());
                        jsonEmbed.put("author", jsonAuthor);
                    }

                    List<JSONObject> jsonFields = new ArrayList<>();
                    for (Field field : fields) {
                        JSONObject jsonField = new JSONObject();

                        jsonField.put("name", field.getName());
                        jsonField.put("value", field.getValue());
                        jsonField.put("inline", field.isInline());

                        jsonFields.add(jsonField);
                    }

                    jsonEmbed.put("fields", jsonFields.toArray());
                    embedObjects.add(jsonEmbed);
                }

                json.put("embeds", embedObjects.toArray());
            }

            try {
                URL url = new URL(entry.getKey().getWebhookUrl());
                HttpsURLConnection connection = (HttpsURLConnection) url.openConnection();
                connection.addRequestProperty("Content-Type", "application/json");
                connection.addRequestProperty("User-Agent", "Java-DiscordWebhook-BY-Gelox_");
                connection.setDoOutput(true);
                connection.setRequestMethod("POST");

                OutputStream stream = connection.getOutputStream();
                stream.write(json.toString().getBytes(StandardCharsets.UTF_8));
                stream.flush();
                stream.close();

                connection.getInputStream().close();
                connection.disconnect();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        this.clearQueue();
    }
}
