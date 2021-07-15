package net.purelic.commons.paper.player;

import com.viaversion.viaversion.api.Via;
import net.kyori.adventure.audience.Audience;
import net.kyori.adventure.identity.Identity;
import net.kyori.adventure.key.Key;
import net.kyori.adventure.platform.AudienceProvider;
import net.kyori.adventure.sound.Sound;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import net.kyori.adventure.text.event.ClickEvent;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextDecoration;
import net.kyori.adventure.text.serializer.plain.PlainComponentSerializer;
import net.kyori.adventure.text.serializer.plain.PlainTextComponentSerializer;
import net.md_5.bungee.api.chat.BaseComponent;
import net.purelic.commons.paper.api.PaperCommons;
import net.purelic.commons.paper.api.player.Purelative;
import net.purelic.commons.paper.api.player.PurelativeInventory;
import net.purelic.commons.paper.api.util.component.FancyChatComponent;
import net.purelic.commons.purelic.api.profile.PlayerDatabaseProfile;
import net.purelic.commons.purelic.api.profile.PlayerPreferences;
import net.purelic.commons.purelic.api.profile.PlayerStatistics;
import net.purelic.commons.purelic.api.utils.Protocol;
import net.purelic.commons.purelic.api.utils.Rank;
import net.purelic.commons.purelic.firebase.PurelicPlayerDatabaseProfile;
import org.apache.commons.validator.routines.UrlValidator;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import static net.kyori.adventure.key.Key.key;
import static net.kyori.adventure.sound.Sound.sound;
import static net.kyori.adventure.text.Component.newline;
import static net.kyori.adventure.text.Component.text;
import static net.kyori.adventure.text.event.ClickEvent.openUrl;
import static net.kyori.adventure.text.event.ClickEvent.suggestCommand;
import static net.kyori.adventure.text.event.HoverEvent.showText;

public class ActualPurelative implements Purelative {

    private final PlayerDatabaseProfile databaseProfile;
    private final @Nullable Player player;
    private final Protocol protocol;
    private final Audience audience; //is Audience#empty if #isOnline is false

    public ActualPurelative(PlayerDatabaseProfile databaseProfile, @Nullable Player player, AudienceProvider provider){
        this.databaseProfile = databaseProfile;
        this.player = player;
        this.protocol = Protocol.getProtocol(Via.getAPI().getPlayerVersion(this.getId()));
        this.audience = provider.player(this.getId());
    }

    @Override
    public boolean isOnline() {
        return Bukkit.getPlayer(this.getId()) != null;
    }

    @Override
    public @Nullable Player getBukkit() {
        if(isOnline()) return this.player;
        return null;
    }

    @Override
    public Date getInstantJoined() {
        return null;
    }

    @Override
    public String getSessionId() {
        return null;
    }

    @Override
    public String getNickname() {
        return this.databaseProfile.getNickname();
    }

    @Override
    public long getTimePlayed() {
        return 0;
    }

    @Override
    public PurelativeInventory getInventory() {
        return null;
    }

    @Override
    public Protocol getProtocol() {
        return this.protocol;
    }

    public void sendFancyMessage(Component prefix, Component message, Collection<Purelative> recipient) {
        final TextComponent.Builder fancyMessage = text().append(text(": "));
        final String[] split = PlainTextComponentSerializer.plainText().serialize(message).split(" ");

        for (String word : split) { //TODO censoring?
            boolean mentioned = false;

            for (Purelative online : PaperCommons.get().getOnlinePurelatives()) {
                // Search for mentions in message
                if (word.equalsIgnoreCase(online.getUsername())) {
                    word = online.getUsername();

                    fancyMessage
                            .append(text(word + " ", NamedTextColor.GREEN))
                            .hoverEvent(showText(text("Click to PM")))
                            .clickEvent(suggestCommand("/msg " + word + " "));

                    mentioned = true;

                    // Play sound if mentioned player is in the message audience
                    if (recipient.contains(online)) {
                        online.playSound(sound(key("random.orb"), Sound.Source.MASTER, 1.0F, 1.0F));
                    }

                    break;
                }
            }

            if (!mentioned) {//TODO why one and not the other?
                // Check for valid links
                String link = (word.startsWith("http://") || word.startsWith("https://")) ? word : ("https://" + word);

                if (UrlValidator.getInstance().isValid(link)) { //TODO: ftp is a default scheme, dangerous?
                    word = word.replace("http://", "").replace("https://", "").replace("www.", "");

                    String truncSmall = ((word.length() >= 20) ? (word.substring(0, 20) + "...") : word);
                    String truncMedium = ((word.length() >= 30) ? (word.substring(0, 30) + "...") : word);

                    fancyMessage
                            .append(text(truncSmall + " ", NamedTextColor.AQUA))
                            .hoverEvent(showText(
                                    text("Click to Open ")
                                            .append(text("(At Own Risk)", NamedTextColor.GRAY))
                                            .append(newline())
                                            .append(text(truncMedium, NamedTextColor.AQUA, TextDecoration.ITALIC))))
                            .clickEvent(openUrl(link));
                } else {
                    fancyMessage.append(text(word + " "));
                }
            }
        }

        if (PaperCommons.get().getMessageControl().blockMessage(message)) {
            // If message contains a blocked word,
            // we block the message entirely for everyone,
            // but still show it to the sender
            this.sendMessage(fancyMessage);
        }

        recipient.forEach(p -> p.sendMessage(fancyMessage));
    }

    @Override
    public @NotNull Audience audience() {
        return this.audience;
    }

    @Override
    public UUID getId() {
        final Player player = this.getBukkit();
        if(player != null) return player.getUniqueId();
        return this.databaseProfile.getId();
    }

    @Override
    public String getUsername() {
        final Player player = this.getBukkit();
        if(player != null) return player.getDisplayName();
        return this.databaseProfile.getUsername();
    }

    @Override
    public PlayerPreferences getPreferences() {
        return this.databaseProfile.getPreferences();
    }

    @Override
    public PlayerStatistics getStats() {
        return this.databaseProfile.getStats();
    }

    @Override
    public List<Rank> getRanks() {
        return this.databaseProfile.getRanks();
    }

    @Override
    public int getRating() {
        return this.databaseProfile.getRating();
    }

    @Override
    public Date getWhenLastSeen() {
        return this.databaseProfile.getWhenLastSeen();
    }

    @Override
    public boolean hasDiscordLinked() {
        return this.databaseProfile.hasDiscordLinked();
    }
}
