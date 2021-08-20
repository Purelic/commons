package net.purelic.commons;

import cloud.commandframework.CommandTree;
import cloud.commandframework.bukkit.CloudBukkitCapabilities;
import cloud.commandframework.execution.AsynchronousCommandExecutionCoordinator;
import cloud.commandframework.execution.CommandExecutionCoordinator;
import cloud.commandframework.paper.PaperCommandManager;
import com.google.common.io.ByteArrayDataInput;
import com.google.common.io.ByteStreams;
import net.md_5.bungee.api.ChatColor;
import net.purelic.commons.analytics.Analytics;
import net.purelic.commons.commands.admin.DeathEffectCommand;
import net.purelic.commons.commands.admin.DemoteCommand;
import net.purelic.commons.commands.admin.PromoteCommand;
import net.purelic.commons.commands.nick.ForceNickCommand;
import net.purelic.commons.commands.nick.NickCommand;
import net.purelic.commons.commands.nick.NicksCommand;
import net.purelic.commons.commands.nick.UnnickCommand;
import net.purelic.commons.commands.npc.NPCReloadCommand;
import net.purelic.commons.commands.op.*;
import net.purelic.commons.commands.parsers.CustomCommand;
import net.purelic.commons.commands.player.*;
import net.purelic.commons.commands.whitelist.*;
import net.purelic.commons.events.CommonsReadyEvent;
import net.purelic.commons.events.PlayerRankChangeEvent;
import net.purelic.commons.listeners.*;
import net.purelic.commons.modules.NPCModule;
import net.purelic.commons.profile.Preference;
import net.purelic.commons.profile.Profile;
import net.purelic.commons.profile.Rank;
import net.purelic.commons.profile.preferences.ChatChannel;
import net.purelic.commons.runnables.IdleTimer;
import net.purelic.commons.utils.*;
import net.purelic.commons.utils.packets.Hologram;
import net.purelic.commons.utils.packets.NPC;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.Configuration;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.plugin.messaging.PluginMessageListener;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.*;
import java.util.function.Function;

public class Commons extends JavaPlugin implements Listener, PluginMessageListener {

    private static final String SPRING_MESSAGE_CHANNEL = "purelic:spring";

    private PaperCommandManager<CommandSender> commandManager;

    private static Commons plugin;
    private static boolean commonsReady;
    private static boolean serverReady;
    private static Map<UUID, Profile> profiles;
    private static World lobby;

    private static Configuration config;
    public static int idleTimer;
    private static boolean cleanDirectory;

    @Override
    public void onEnable() {
        plugin = this;
        commonsReady = false;
        serverReady = false;
        profiles = new HashMap<>();

        config = this.getConfig();
        idleTimer = config.getInt("idle_timer", 0);
        cleanDirectory = config.getBoolean("clean_directory", true);

        // Register plugin channels
        this.getServer().getMessenger().registerOutgoingPluginChannel(this, SPRING_MESSAGE_CHANNEL);
        this.getServer().getMessenger().registerIncomingPluginChannel(this, SPRING_MESSAGE_CHANNEL, this);

        // Register listeners and commands
        this.registerListeners();
        this.registerCommands();

        // Init settings
        Bukkit.setWhitelist(false);
        DatabaseUtils.connectDatabase();
        MapUtils.connectDropbox(config);
        ChatUtils.addCensoredWords(config);
        Fetcher.cacheNicks();
        Analytics.connectAnalytics(config);

        // Clean up files
        if (cleanDirectory) {
            MapUtils.removeAllMaps();
        }
    }

    @Override
    public void onDisable() {
        Bukkit.getOnlinePlayers().forEach(player -> player.kickPlayer("Server shutdown"));
        if (cleanDirectory) MapUtils.removeAllMaps();
        DatabaseUtils.setServerShutdown();
    }

    public static Commons getPlugin() {
        return plugin;
    }

    public static boolean isReady() {
        return serverReady && commonsReady;
    }

    public static void setCommonsReady() {
        if (commonsReady) return;
        commonsReady = true;
        if (idleTimer > 0) TaskUtils.runLater(new IdleTimer(), idleTimer);
        callEvent(new CommonsReadyEvent());
        if (serverReady) DatabaseUtils.setServerOnline();
    }

    public static void setServerReady() {
        if (serverReady) return;
        serverReady = true;
        if (commonsReady) DatabaseUtils.setServerOnline();
    }

    public static String getRoot() {
        return Bukkit.getWorldContainer().getAbsolutePath() + "/";
    }

    public static World getLobby() {
        return lobby;
    }

    public static void setLobby(World world) {
        lobby = world;
    }

    public static String getLobbyPreference() {
        if (ServerUtils.isPrivate() && Commons.hasOwner()) {
            return (String) Commons.getProfile(Commons.getOwnerId()).getPreference(Preference.LOBBY, "Default Lobby");
        } else {
            return "Default Lobby";
        }
    }

    public static boolean isOwner(Player player) {
        return isOwner(player.getUniqueId());
    }

    public static boolean isOwner(UUID uuid) {
        return ServerUtils.isPrivate() && ServerUtils.getId().equals(uuid.toString());
    }

    public static boolean hasOwner() {
        return !ServerUtils.getId().isEmpty();
    }

    public static UUID getOwnerId() {
        return UUID.fromString(ServerUtils.getId());
    }

    public static Profile getProfile(Player player) {
        return getProfile(player.getUniqueId(), player.getName());
    }

    public static Profile getProfile(UUID uuid) {
        return getProfile(uuid, null);
    }

    public static Profile getProfile(UUID uuid, String name) {
        return getProfile(uuid, name, false);
    }

    public static Profile getProfile(UUID uuid, String name, boolean updateLastSeen) {
        if (profiles.get(uuid) == null) DatabaseUtils.loadProfile(uuid, name, updateLastSeen);
        else if (updateLastSeen) DatabaseUtils.setLastSeenToNow(uuid);

        return profiles.get(uuid);
    }

    public static Profile addProfile(UUID uuid, Profile profile) {
        profiles.put(uuid, profile);
        profile.setUniqueId(uuid);
        return profile;
    }

    public static void removeProfile(Player player) {
        profiles.remove(player.getUniqueId());
    }

    private void registerListeners() {
        this.register(new NameChange());
        this.register(new OpStatusChange());
        this.register(new PlayerInteract());
        this.register(new PlayerJoin());
        this.register(new PlayerPreLogin());
        this.register(new PlayerQuit());
        this.register(new PlayerRankChange());
        this.register(new ProcessCommand());
        this.register(new WeatherChange());
        this.register(new WorldInit());

        try {
            Class.forName("me.vagdedes.spartan.api.API");
            this.register(new PlayerViolationCommand(config));
        } catch (ClassNotFoundException e) {
            System.out.println("Spartan API not loaded");
        }
    }

    private void register(Listener listener) {
        this.getServer().getPluginManager().registerEvents(listener, this);
    }

    public static void registerListener(Listener listener) {
        plugin.register(listener);
    }

    private void registerCommandManager() {
        final Function<CommandTree<CommandSender>, CommandExecutionCoordinator<CommandSender>> executionCoordinatorFunction =
            AsynchronousCommandExecutionCoordinator.<CommandSender>newBuilder().build();

        final Function<CommandSender, CommandSender> mapperFunction = Function.identity();

        try {
            this.commandManager = new PaperCommandManager<>(
                this,
                executionCoordinatorFunction,
                mapperFunction,
                mapperFunction
            );
        } catch (final Exception e) {
            this.getLogger().severe("Failed to initialize the command manager!");
            this.getServer().getPluginManager().disablePlugin(this);
            return;
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

    public PaperCommandManager<CommandSender> getCommandManager() {
        return this.commandManager;
    }

    private void registerCommands() {
        this.registerCommandManager();

        // Admin Commands
        this.register(new DeathEffectCommand());
        this.register(new DemoteCommand());
        this.register(new PromoteCommand());

        // Nick Commands
        this.register(new ForceNickCommand());
        this.register(new NickCommand());
        this.register(new NicksCommand());
        this.register(new UnnickCommand());

        // NPC Commands
        this.register(new NPCReloadCommand());

        // OP Commands
        this.register(new AlertCommand());
        this.register(new BanCommand());
        this.register(new DeopCommand());
        this.register(new HeadCommand());
        this.register(new ImageCommand());
        this.register(new KickCommand());
        this.register(new OpCommand());
        this.register(new ShutdownCommand());
        this.register(new UnbanCommand());

        // Player Commands
        MacroCommand.registerMacroCommands();
        this.register(new ChatChannelCommand());
        this.register(new MeCommand());
        this.register(new ShrugCommand());
        this.register(new VersionCommand());

        // Whitelist Commands
        this.register(new WhitelistAddCommand());
        this.register(new WhitelistClearCommand());
        this.register(new WhitelistKickCommand());
        this.register(new WhitelistListCommand());
        this.register(new WhitelistOffCommand());
        this.register(new WhitelistOnCommand());
        this.register(new WhitelistRemoveCommand());
    }

    private void register(CustomCommand command) {
        this.commandManager.command(command.getCommandBuilder(this.commandManager).build());
    }

    public static void registerCommand(CustomCommand command) {
        plugin.register(command);
    }

    public static void sendSpringMessage(Player player, String subChannel, String... data) {
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        DataOutputStream out = new DataOutputStream(stream);

        try {
            out.writeUTF(subChannel);
            out.writeUTF(player.getUniqueId().toString());
            for (String s : data) out.writeUTF(s);
        } catch (IOException e) {
            e.printStackTrace();
        }

        player.sendPluginMessage(plugin, SPRING_MESSAGE_CHANNEL, stream.toByteArray());
    }

    @SuppressWarnings("all")
    @Override
    public void onPluginMessageReceived(String channel, Player player, byte[] bytes) {
        if (!channel.equalsIgnoreCase(SPRING_MESSAGE_CHANNEL)) return;

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
        } else if (subChannel.equals("UpdateNPC")) {
            String id = in.readUTF();
            String[] lines = in.readUTF().split("\n");
            NPC npc = NPCModule.getNPC(id);

            if (npc != null) {
                Hologram hologram = npc.getHologram();
                hologram.set(lines);
                hologram.show();
            }
        } else if (subChannel.equals("UpdateHologram")) {
            String id = in.readUTF();
            String[] lines = in.readUTF().split("\n");
            Hologram hologram = NPCModule.getHologramById(id);

            if (hologram != null) {
                hologram.set(lines);
                hologram.show();
            }
        } else if (subChannel.equals("AddRank")) {
            UUID playerId = UUID.fromString(in.readUTF());
            Rank rank = Rank.valueOf(in.readUTF());
            Profile profile = getProfile(playerId);
            profile.addRank(rank);
            Commons.callEvent(new PlayerRankChangeEvent(profile.getPlayer(), profile));
        } else if (subChannel.equals("RemoveRank")) {
            UUID playerId = UUID.fromString(in.readUTF());
            Rank rank = Rank.valueOf(in.readUTF());
            Profile profile = getProfile(playerId);
            profile.removeRank(rank);
            Commons.callEvent(new PlayerRankChangeEvent(profile.getPlayer(), profile));
        } else if (subChannel.equals("GiveEggs")) {
            UUID playerId = UUID.fromString(in.readUTF());
            int eggs = Integer.parseInt(in.readUTF());
            Bukkit.getPlayer(playerId).getInventory().addItem(new ItemCrafter(Material.MONSTER_EGG)
                .amount(eggs)
                .name(ChatColor.LIGHT_PURPLE + "NPC Skin Changer")
                .craft());
            getProfile(playerId).addNPCEggs(eggs);
        } else if (subChannel.equals("UpdateChatChannel")) {
            UUID playerId = UUID.fromString(in.readUTF());
            ChatChannel chatChannel = ChatChannel.valueOf(in.readUTF());
            getProfile(playerId).setChatChannel(chatChannel);
        }
    }

    public static double getTPS() {
        double tps = Bukkit.getServer().spigot().getTPS()[0];
        return Math.min((double) Math.round(tps * 100) / 100, 20.00);
    }

    public static int getPing(UUID uuid) {
        return getPing(Bukkit.getPlayer(uuid));
    }

    public static int getPing(Player player) {
        return ((CraftPlayer) player).getHandle().ping;
    }

    public static void callEvent(Event event) {
        Bukkit.getPluginManager().callEvent(event);
    }

}
