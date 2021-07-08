package net.purelic.api.discord.embed;

import java.awt.Color;
import java.util.List;

public interface EmbedObject {

    String getTitle();

    EmbedObject setTitle(String title);

    String getDescription();

    EmbedObject setDescription(String description);

    String getUrl();

    EmbedObject setUrl(String url);

    Color getColor();

    EmbedObject setColor(Color color);

    Footer getFooter();

    EmbedObject setFooter(String text, String icon);

    Thumbnail getThumbnail();

    EmbedObject setThumbnail(String url);

    Image getImage();

    EmbedObject setImage(String url);

    Author getAuthor();

    EmbedObject setAuthor(String name, String url, String icon);

    List<Field> getFields();

    EmbedObject addField(String name, String value, boolean inline);
}
