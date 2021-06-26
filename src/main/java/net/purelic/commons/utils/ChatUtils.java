package net.purelic.commons.utils;

import com.google.cloud.Timestamp;
import com.lunarclient.bukkitapi.LunarClientAPI;
import com.lunarclient.bukkitapi.object.TitleType;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.*;
import net.minecraft.server.v1_8_R3.ChatComponentText;
import net.minecraft.server.v1_8_R3.PacketPlayOutChat;
import net.purelic.commons.Commons;
import net.purelic.commons.listeners.PlayerQuit;
import net.purelic.commons.utils.lunar.LunarUtils;
import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.validator.routines.UrlValidator;
import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.configuration.Configuration;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.github.paperspigot.Title;
import org.ocpsoft.prettytime.PrettyTime;

import java.time.Duration;
import java.util.*;
import java.util.regex.Pattern;

public class ChatUtils {

    public static final String BULLET = " \u2022 "; // •
    public static final String ARROW = "\u00BB"; // »

    private static final UrlValidator VALIDATOR = new UrlValidator();
    private static final List<String> BLOCKED_WORDS = new ArrayList<>();
    private static final List<String> CENSORED_WORDS = new ArrayList<>();
    private static String censorRegex = "";

    public static void addCensoredWords(Configuration config) {
        BLOCKED_WORDS.addAll(config.getStringList("blocked_words"));
        CENSORED_WORDS.addAll(config.getStringList("censored_words"));
        censorRegex = getCensorRegex();
    }

    public static BaseComponent[] getPrefix(String prefix, String hover, ChatColor primary, ChatColor secondary) {
        return
            new ComponentBuilder("(").color(secondary).bold(true)
                .event(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder(hover).color(primary).create()))
                .append(prefix, ComponentBuilder.FormatRetention.ALL).color(primary)
                .append(") ", ComponentBuilder.FormatRetention.ALL).color(secondary)
                .create();
    }

    public static BaseComponent[] getHeader(String header) {
        return getHeader(header, false);
    }

    public static BaseComponent[] getHeader(String header, boolean bold) {
        return getHeader(header, bold, ChatColor.AQUA, ChatColor.WHITE);
    }

    public static BaseComponent[] getHeader(String header, ChatColor primary, ChatColor secondary) {
        return getHeader(header, false, primary, secondary);
    }

    public static BaseComponent[] getHeader(String header, boolean bold, ChatColor primary, ChatColor secondary) {
        return getHeader(header, bold, primary, secondary, false);
    }

    public static BaseComponent[] getHeader(String header, boolean bold, ChatColor primary, ChatColor secondary, boolean padding) {
        return new ComponentBuilder((padding ? "\n" : "") + "                    ").color(secondary).strikethrough(true)
            .append("   " + header + "   ").reset().color(primary).bold(bold)
            .append("                    " + (padding ? "\n" : "")).reset().color(secondary).strikethrough(true)
            .create();
    }

    public static void broadcastActionBar(String message) {
        broadcastActionBar(message, false);
    }

    public static void broadcastActionBar(String message, boolean includeLegacy) {
        Bukkit.getOnlinePlayers().forEach(player -> sendActionBar(player, message, includeLegacy));
    }

    public static void sendActionBar(Player player, String message) {
        sendActionBar(player, message, false);
    }

    public static void sendActionBar(Player player, String message, boolean includeLegacy) {
        if (VersionUtils.isLegacy(player)) {
            if (includeLegacy) sendMessage(player, message);
        } else {
            PacketPlayOutChat packet = new PacketPlayOutChat(new ChatComponentText(message), (byte) 2);
            ((CraftPlayer) player).getHandle().playerConnection.sendPacket(packet);
        }
    }

    public static void clearActionBar(Player player) {
        sendActionBar(player, "");
    }

    public static void clearActionBarAll() {
        broadcastActionBar("");
    }

    public static void broadcastTitle(String title) {
        broadcastTitle(title, "");
    }

    public static void broadcastTitle(String title, String subtitle) {
        for (Player player : Bukkit.getOnlinePlayers()) sendTitle(player, title, subtitle);
    }

    public static void broadcastTitle(String title, String subtitle, int duration) {
        for (Player player : Bukkit.getOnlinePlayers()) sendTitle(player, title, subtitle, duration);
    }

    public static void sendTitle(Player player, String title) {
        sendTitle(player, title, "");
    }

    public static void sendTitle(Player player, String title, String subtitle) {
        sendTitle(player, title, subtitle, 40);
    }

    public static void sendTitle(Player player, String title, String subtitle, int duration) {
        sendTitle(player, title, subtitle, 5, duration, 5);
    }

    public static void sendTitle(Player player, String title, String subtitle, int fadeIn, int duration, int fadeOut) {
        if (VersionUtils.isLegacy(player) && LunarUtils.isLoaded()) {
            LunarClientAPI.getInstance().sendTitle(
                player,
                TitleType.TITLE,
                title,
                1F, // scale
                Duration.ofMillis(fadeIn * 50L),
                Duration.ofMillis(duration * 50L),
                Duration.ofMillis(fadeOut * 50L)
            );

            LunarClientAPI.getInstance().sendTitle(
                player,
                TitleType.SUBTITLE,
                subtitle,
                1F, // scale
                Duration.ofMillis(fadeIn * 50L),
                Duration.ofMillis(duration * 50L),
                Duration.ofMillis(fadeOut * 50L)
            );
        } else {
            player.sendTitle(new Title(
                new TextComponent(title),
                new TextComponent(subtitle),
                fadeIn, duration, fadeOut
            ));
        }
    }

    public static void sendFancyChatMessage(Player player, String message) {
        sendFancyChatMessage(player, message, new ComponentBuilder(""), Bukkit.getOnlinePlayers());
    }

    public static void sendFancyChatMessage(Player player, String message, ComponentBuilder prefix, Collection<? extends Player> audience) {
        sendFancyChatMessage(player, message, prefix.create(), audience);
    }

    public static void sendFancyChatMessage(Player player, String message, BaseComponent[] prefix, Collection<? extends Player> audience) {
        if (blockMessage(message)) {
            // If message contains a blocked word,
            // we block the message entirely for everyone,
            // but still show it to the sender
            audience = new ArrayList<>(Collections.singleton(player));
        }

        BaseComponent[] fancyMessage = getFancyChatMessage(player, prefix, message, audience);
        for (Player online : audience) online.sendMessage(fancyMessage);
    }

    private static BaseComponent[] getFancyChatMessage(Player player, BaseComponent[] prefix, String message, Collection<? extends Player> audience) {
        // Censor words in message
        message = message.replaceAll(censorRegex, "*");

        BaseComponent[] fancyName = new BaseComponent[]{Fetcher.getFancyName(player)};
        ComponentBuilder fancyMessage = new ComponentBuilder(": ");
        String[] split = message.split(" ");

        for (String word : split) {
            boolean mentioned = false;

            for (Player online : Bukkit.getOnlinePlayers()) {
                // Search for mentions in message
                if (word.equalsIgnoreCase(online.getName())) {
                    word = online.getName();

                    fancyMessage
                        .append(word + " ").color(ChatColor.GREEN)
                        .event(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder("Click to PM").create()))
                        .event(new ClickEvent(ClickEvent.Action.SUGGEST_COMMAND, "/msg " + word + " "));

                    mentioned = true;

                    // Play sound if mentioned player is in the message audience
                    if (audience.contains(online)) {
                        online.playSound(online.getLocation(), Sound.ORB_PICKUP, 1.0F, 1.0F);
                    }

                    break;
                }
            }

            if (!mentioned) {
                // Check for valid links
                String link = (word.startsWith("http://") || word.startsWith("https://")) ? word : ("https://" + word);

                if (VALIDATOR.isValid(link)) {
                    word = word.replace("http://", "").replace("https://", "").replace("www.", "");

                    String truncSmall = ((word.length() >= 20) ? (word.substring(0, 20) + "...") : word);
                    String truncMedium = ((word.length() >= 30) ? (word.substring(0, 30) + "...") : word);

                    fancyMessage
                        .append(truncSmall + " ").color(ChatColor.AQUA)
                        .event(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder(
                            "Click to Open " + ChatColor.GRAY + "(At Own Risk)\n" + ChatColor.ITALIC + ChatColor.AQUA + truncMedium).create()))
                        .event(new ClickEvent(ClickEvent.Action.OPEN_URL, link));
                } else {
                    fancyMessage.append(word + " ").reset();
                }
            }
        }

        return (BaseComponent[]) ArrayUtils.addAll(prefix, ArrayUtils.addAll(fancyName, fancyMessage.create()));
    }

    private static boolean blockMessage(String message) {
        String[] words = message.split("\\b");

        for (String censored : BLOCKED_WORDS) {
            for (String word : words) {
                if (censored.equalsIgnoreCase(word.trim())) {
                    return true;
                }
            }
        }

        return false;
    }

    private static String getCensorRegex() {
        StringBuilder builder = new StringBuilder();

        for (String word : CENSORED_WORDS) {
            if (builder.length() > 0) builder.append("|");
            builder.append(String.format("(?i)(?<=(?=%s).{0,%d}).", Pattern.quote(word), word.length() - 1));
        }

        return builder.toString();
    }

    public static void sendPrivateMessage(UUID sender, UUID recipient, String message) {
        sendPrivateMessage(Bukkit.getPlayer(sender), Bukkit.getPlayer(recipient), message);
    }

    public static void sendPrivateMessage(Player sender, Player recipient, String message) {
        sendPrivateMessage(sender, recipient, message, true);
        sendPrivateMessage(sender, recipient, message, false);
        recipient.playSound(recipient.getLocation(), Sound.ORB_PICKUP, 10F, 1F);
    }

    private static void sendPrivateMessage(Player sender, Player recipient, String message, boolean isSender) {
        TextComponent pm = new TextComponent(ChatColor.GRAY + "(" + (isSender ? "To " : "From "));
        pm.addExtra(isSender ? Fetcher.getFancyName(recipient, sender) : Fetcher.getFancyName(sender, recipient));
        pm.addExtra(ChatColor.GRAY + "): " + ChatColor.RESET + message);
        (isSender ? sender : recipient).sendMessage(pm);
    }

    public static void sendQuitMessage(UUID uuid, String server) {
        Player player = Bukkit.getPlayer(uuid);

        TextComponent message = new TextComponent(
            ChatColor.GRAY + (server == null || server.isEmpty() ? " quit" : " moved to " + server));

        if (player == null && PlayerQuit.hasCachedName(uuid)) {
            Bukkit.broadcast(PlayerQuit.getCachedName(uuid), message);
        } else if (player != null) {
            Bukkit.broadcast(Fetcher.getFancyName(player), message);
        }
    }

    public static void broadcastPunishment(UUID punisherId, String type, UUID punishedId, String reason) {
        ChatUtils.sendMessageAll(
            new ComponentBuilder("GUARDIAN  ").color(ChatColor.RED).bold(true).create()[0],
            Fetcher.getFancyName(punisherId),
            new ComponentBuilder(" " + type + " ").create()[0],
            Fetcher.getFancyName(punishedId),
            new ComponentBuilder(ChatColor.GRAY + " " + ARROW + ChatColor.WHITE + " " + reason).create()[0]);
    }

    public static void broadcastReport(UUID reporterId, UUID reportedId, String reason) {
        for (Player player : Bukkit.getOnlinePlayers()) {
            if (!Commons.getProfile(player).isMod()) continue;

            ChatUtils.sendMessage(player,
                new ComponentBuilder("GUARDIAN  ").color(ChatColor.RED).bold(true).create()[0],
                Fetcher.getFancyName(reporterId),
                new ComponentBuilder(" reported ").create()[0],
                Fetcher.getFancyName(reportedId),
                new ComponentBuilder(ChatColor.GRAY + " " + ARROW + ChatColor.WHITE + " " + reason).create()[0],
                new ComponentBuilder(" (" + Commons.getPing(reportedId) + "ms / " + Commons.getTPS() + " TPS)").color(ChatColor.GRAY).create()[0]);

            player.playSound(player.getLocation(), Sound.BLAZE_DEATH, 2F, 1F);
        }
    }

    public static void sendMessageAll(ComponentBuilder builder) {
        sendMessageAll(new TextComponent(builder.create()));
    }

    public static void sendMessageAll(String message) {
        sendMessageAll(new TextComponent(message));
    }

    public static void sendMessageAll(BaseComponent... message) {
        sendMessageAll(new TextComponent(message));
    }

    public static void sendMessageAll(TextComponent message) {
        Bukkit.getOnlinePlayers().forEach(player -> sendMessage(player, message));
    }

    public static void sendMessage(Player player, ComponentBuilder builder) {
        sendMessage(player, new TextComponent(builder.create()));
    }

    public static void sendMessage(Player player, String message) {
        sendMessage(player, new TextComponent(message));
    }

    public static void sendMessage(Player player, BaseComponent... message) {
        sendMessage(player, new TextComponent(message));
    }

    public static void sendMessage(Player player, TextComponent message) {
        if (VersionUtils.isLegacy(player)) player.sendMessage(message.toLegacyText().split("\n"));
        else player.sendMessage(message);
    }

    public static String format(Timestamp timestamp) {
        return format(timestamp.toDate());
    }

    public static String format(Date date) {
        return new PrettyTime().format(date);
    }

    public static void broadcastAlert(String message) {
        ChatUtils.sendMessageAll(getAlertMessage(message));
    }

    private static ComponentBuilder getAlertMessage(String message) {
        return new ComponentBuilder("\n")
            .append("ALERT  ").color(ChatColor.RED).bold(true)
            .append(message).reset()
            .append("\n");
    }

}
