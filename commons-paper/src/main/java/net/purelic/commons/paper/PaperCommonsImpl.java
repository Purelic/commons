package net.purelic.commons.paper;

import cloud.commandframework.bukkit.CloudBukkitCapabilities;
import cloud.commandframework.execution.AsynchronousCommandExecutionCoordinator;
import cloud.commandframework.paper.PaperCommandManager;
import com.google.common.io.ByteArrayDataInput;
import com.google.common.io.ByteStreams;
import net.kyori.adventure.platform.AudienceProvider;
import net.kyori.adventure.platform.bukkit.BukkitAudiences;
import net.md_5.bungee.api.ChatColor;
import net.purelic.commons.paper.api.PaperCommons;
import net.purelic.commons.paper.api.chat.NaughtyMessageManager;
import net.purelic.commons.paper.api.command.CustomCommand;
import net.purelic.commons.paper.api.module.Modules;
import net.purelic.commons.paper.api.player.Purelative;
import net.purelic.commons.paper.api.task.PurelicTaskManager;
import net.purelic.commons.paper.modules.manage.PurelicPaperModules;
import net.purelic.commons.paper.player.PurelativeProvider;
import net.purelic.commons.paper.utils.ChatUtils;
import net.purelic.commons.paper.utils.ServerUtils;
import net.purelic.commons.purelic.api.PurelicAPI;
import net.purelic.commons.purelic.api.discord.DiscordWebhook;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.plugin.messaging.PluginMessageListener;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

public class PaperCommonsImpl extends JavaPlugin implements PaperCommons, PluginMessageListener {

    private PurelicAPI api;
    private AudienceProvider audienceProvider;
    private PaperCommandManager<Purelative> commandManager;
    private PurelativeProvider purelativeProvider;
    private final Modules modules = new PurelicPaperModules();

    @Override
    public void onEnable() {
        this.api = PurelicAPI.getImpl();
        this.audienceProvider = BukkitAudiences.create(this);
        this.purelativeProvider = new PurelativeProvider(api::getProfile, this.audienceProvider);
        this.register(this.purelativeProvider);

        try {
            this.commandManager = new PaperCommandManager<>(
                    this,
                    AsynchronousCommandExecutionCoordinator.<Purelative>newBuilder().build(),
                    (sender) -> this.getPurelative(Bukkit.getPlayer(sender.getName()).getUniqueId()),//TODO console?
                    Purelative::getBukkit);
        } catch (Exception e) {
            e.printStackTrace(); //todo: shutdown here
        }

        // Create case insensitive suggestions
        this.commandManager.setCommandSuggestionProcessor((context, strings) -> {
            String input;

            if (context.getInputQueue().isEmpty()) {
                input = "";
            } else {
                input = context.getInputQueue().peek();
            }

            input = input.toLowerCase();
            List<String> suggestions = new LinkedList<>();

            for (String suggestion : strings) {
                if (suggestion == null) continue;

                if (suggestion.toLowerCase().startsWith(input)) {
                    suggestions.add(suggestion);
                }
            }

            return suggestions;
        });

        // Register async completions
        if (this.commandManager.queryCapability(CloudBukkitCapabilities.ASYNCHRONOUS_COMPLETION)) {
            this.commandManager.registerAsynchronousCompletions();
        }
    }

    @Override
    public DiscordWebhook getDiscordWebhook() {
        return api.getDiscordWebhook();
    }

    @Override
    public Purelative getPurelative(UUID uuid) {
        return this.purelativeProvider.get(uuid);
    }

    @Override
    public Collection<Purelative> getOnlinePurelatives() {
        return Bukkit.getOnlinePlayers().stream().map(p -> this.getPurelative(p.getUniqueId())).collect(Collectors.toList());
    }

    @Override
    public Modules getModules() {
        return this.modules;
    }

    @Override
    public PurelicTaskManager getTaskManager() {
        return null;
    }

    @Override
    public NaughtyMessageManager getMessageControl() {
        return null;
    }

    @Override
    public void register(Listener listener) {
        Bukkit.getPluginManager().registerEvents(listener, this);
    }

    @Override
    public void unRegister(Listener listener) {
        HandlerList.unregisterAll(listener);
    }

    @Override
    public void register(CustomCommand command) {
        this.commandManager.command(command.getCommandBuilder(this.commandManager));
    }

    @Override
    public void sendSpringMessage(String subChannel, String... data) {
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        DataOutputStream out = new DataOutputStream(stream);

        try {
            out.writeUTF(subChannel);
            for (String s : data) out.writeUTF(s);
        } catch (IOException e) {
            e.printStackTrace();
        }

        this.getServer().sendPluginMessage(this, "purelic:spring", stream.toByteArray()); //TODO: abstract out channel string
    }

    @Override
    public void onPluginMessageReceived(String channel, Player player, byte[] bytes) {
        if (!channel.equalsIgnoreCase("purelic:spring")) return;

        ByteArrayDataInput in = ByteStreams.newDataInput(bytes);
        String subChannel = in.readUTF();

        if (subChannel.equalsIgnoreCase("PrivateMessage")) {
            UUID senderId = UUID.fromString(in.readUTF());
            UUID recipientId = UUID.fromString(in.readUTF());
            String message = in.readUTF();
            ChatUtils.sendPrivateMessage(senderId, recipientId, message);
        } else if (subChannel.equalsIgnoreCase("QuitMessage")) {
            UUID playerId = UUID.fromString(in.readUTF());
            String server = in.readUTF();
            ChatUtils.sendQuitMessage(playerId, server);
        } else if (subChannel.equalsIgnoreCase("PunishMessage")) {
            UUID punisherId = UUID.fromString(in.readUTF());
            String type = in.readUTF();
            UUID punishedId = UUID.fromString(in.readUTF());
            String reason = in.readUTF();
            ChatUtils.broadcastPunishment(punisherId, type, punishedId, reason);
        } else if (subChannel.equalsIgnoreCase("ReportMessage")) {
            UUID reporterId = UUID.fromString(in.readUTF());
            UUID reportedId = UUID.fromString(in.readUTF());
            String reason = in.readUTF();
            ChatUtils.broadcastReport(reporterId, reportedId, reason);
        } else if (subChannel.equalsIgnoreCase("StaffChat")) {
            UUID playerId = UUID.fromString(in.readUTF());
            String message = in.readUTF();
            ChatUtils.sendFancyChatMessage(
                    Bukkit.getPlayer(playerId),
                    message,
                    ChatUtils.getPrefix("Staff", "Staff Chat", ChatColor.DARK_RED, ChatColor.WHITE),
                    ServerUtils.getOnlineStaff()
            );
        } else if (subChannel.equalsIgnoreCase("PartyChat")) {
            UUID playerId = UUID.fromString(in.readUTF());
            String partyName = in.readUTF();
            String message = in.readUTF();
            int players = in.readInt();

            List<Player> audience = new ArrayList<>();

            for (int i = 0; i < players; i++) {
                audience.add(Bukkit.getPlayer(UUID.fromString(in.readUTF())));
            }

            ChatUtils.sendFancyChatMessage(
                    Bukkit.getPlayer(playerId),
                    message,
                    ChatUtils.getPrefix(partyName, "Party Chat", ChatColor.GOLD, ChatColor.WHITE),
                    audience
            );
        }
    }
}
