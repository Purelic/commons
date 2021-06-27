package net.purelic.commons.utils;

import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import org.apache.commons.lang.ArrayUtils;
import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.entity.Player;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

public class CommandUtils {

    private static final Set<UUID> OP_PLAYERS = new HashSet<>();

    private static BaseComponent[] errorPrefix;
    private static BaseComponent[] alertPrefix;
    private static BaseComponent[] successPrefix;

    public static boolean isOp(Player player) {
        return player != null && OP_PLAYERS.contains(player.getUniqueId());
    }

    public static void setOp(Player player, boolean op) {
        if (op) OP_PLAYERS.add(player.getUniqueId());
        else OP_PLAYERS.remove(player.getUniqueId());
    }

    private static BaseComponent[] getPrefix(String hover, ChatColor primary, ChatColor secondary) {
        return
            new ComponentBuilder("[").color(secondary).bold(true)
                .event(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder(hover).color(primary).create()))
                .append("!", ComponentBuilder.FormatRetention.ALL).color(primary)
                .append("] ", ComponentBuilder.FormatRetention.ALL).color(secondary)
                .create();
    }

    private static BaseComponent[] getErrorPrefix() {
        if (errorPrefix == null) errorPrefix = getPrefix("Error", ChatColor.RED, ChatColor.DARK_RED);
        return errorPrefix;
    }

    private static BaseComponent[] getAlertPrefix() {
        if (alertPrefix == null) alertPrefix = getPrefix("Alert", ChatColor.YELLOW, ChatColor.GOLD);
        return alertPrefix;
    }

    private static BaseComponent[] getSuccessPrefix() {
        if (successPrefix == null) successPrefix = getPrefix("Success", ChatColor.GREEN, ChatColor.DARK_GREEN);
        return successPrefix;
    }

    public static void broadcastErrorMessage(String message) {
        Bukkit.getOnlinePlayers().forEach(player -> sendErrorMessage(player, message));
    }

    public static void broadcastErrorMessage(BaseComponent... message) {
        Bukkit.getOnlinePlayers().forEach(player -> sendErrorMessage(player, message));
    }

    public static void sendErrorMessage(Player player, String message) {
        TextComponent errorMessage = new TextComponent(message);
        errorMessage.setColor(ChatColor.RED);
        sendErrorMessage(player, errorMessage);
    }

    public static void sendErrorMessage(Player player, BaseComponent... messages) {
        player.sendMessage((BaseComponent[]) ArrayUtils.addAll(getErrorPrefix(), messages));
        player.playSound(player.getLocation(), Sound.ITEM_BREAK, 1.0F, 1.0F);
    }

    public static void sendNoPlayerMessage(Player player, String target) {
        sendNotFoundMessage(player, "player", target);
    }

    public static void sendNotFoundMessage(Player player, String type, String arg) {
        TextComponent errorMessage = new TextComponent(
            String.format("Could not find %s \"%s\"", type, arg));
        errorMessage.setColor(ChatColor.RED);
        sendErrorMessage(player, errorMessage);
    }

    public static void broadcastAlertMessage(String message) {
        Bukkit.getOnlinePlayers().forEach(player -> sendAlertMessage(player, message));
    }

    public static void broadcastAlertMessage(BaseComponent... message) {
        Bukkit.getOnlinePlayers().forEach(player -> sendAlertMessage(player, message));
    }

    public static void sendAlertMessage(Player player, String message) {
        sendAlertMessage(player, new TextComponent(message));
    }

    public static void sendAlertMessage(Player player, BaseComponent... messages) {
        player.sendMessage((BaseComponent[]) ArrayUtils.addAll(getAlertPrefix(), messages));
        player.playSound(player.getLocation(), Sound.CLICK, 1.0F, 1.0F);
    }

    public static void broadcastSuccessMessage(String message) {
        Bukkit.getOnlinePlayers().forEach(player -> sendSuccessMessage(player, message));
    }

    public static void broadcastSuccessMessage(BaseComponent... message) {
        Bukkit.getOnlinePlayers().forEach(player -> sendSuccessMessage(player, message));
    }

    public static void sendSuccessMessage(Player player, String message) {
        TextComponent successMessage = new TextComponent(message);
        successMessage.setColor(ChatColor.GREEN);
        sendSuccessMessage(player, successMessage);
    }

    public static void sendSuccessMessage(Player player, BaseComponent... messages) {
        player.sendMessage((BaseComponent[]) ArrayUtils.addAll(getSuccessPrefix(), messages));
        player.playSound(player.getLocation(), Sound.ORB_PICKUP, 1.0F, 1.0F);
    }

}
