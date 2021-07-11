package net.purelic.commons.modules;

import io.netty.channel.*;
import net.minecraft.server.v1_8_R3.PacketPlayInUseEntity;
import net.purelic.commons.Commons;
import net.purelic.commons.events.NPCInteractEvent;
import net.purelic.commons.utils.TaskUtils;
import net.purelic.commons.utils.packets.Hologram;
import net.purelic.commons.utils.packets.NPC;
import net.purelic.commons.utils.packets.constants.NPCInteractAction;
import org.bukkit.configuration.Configuration;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.plugin.java.JavaPlugin;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class NPCModule implements Module {

    private static final Map<String, NPC> NPCS = new HashMap<>();
    private static final List<Hologram> HOLOGRAMS = new ArrayList<>();
    private static JavaPlugin plugin;

    public NPCModule(JavaPlugin plugin) {
        NPCModule.plugin = plugin;
        reload();
    }

    public static void reload() {
        if (plugin == null) return;
        plugin.reloadConfig();
        reload(plugin.getConfig());
    }

    @SuppressWarnings("unchecked")
    public static void reload(Configuration config) {
        for (NPC npc : NPCS.values()) npc.remove();
        for (Hologram hologram : HOLOGRAMS) hologram.remove();

        NPCS.clear();
        HOLOGRAMS.clear();

        for (Map<?, ?> yaml : config.getMapList("npcs")) {
            NPC npc = new NPC((Map<String, Object>) yaml);
            npc.create();
            NPCS.put(npc.getId(), npc);
        }

        for (Map<?, ?> yaml : config.getMapList("holograms")) {
            Hologram hologram = new Hologram((Map<String, Object>) yaml);
            hologram.show();
            HOLOGRAMS.add(hologram);
        }
    }

    public static NPC getNPC(String id) {
        return NPCS.get(id);
    }

    public static NPC getNPCByEntityId(int entityId) {
        for (NPC npc : NPCS.values()) {
            if (npc.getEntityId() == entityId) return npc;
        }

        return null;
    }

    public static List<Hologram> getHolograms() {
        return HOLOGRAMS;
    }

    @EventHandler
    public void onNPCInteract(NPCInteractEvent event) {
        Player player = event.getPlayer();
        Commons.sendSpringMessage(
            player,
            "NPCInteract",
            event.getNPC().getId(),
            event.getAction().name()
        );
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        this.injectPlayer(player);

        // Adding a slight delay after joining helps with npcs and holograms not showing consistently
        TaskUtils.runLaterAsync(() -> {
            for (NPC npc : NPCS.values()) npc.show(player);
            for (Hologram hologram : HOLOGRAMS) hologram.show(player);
        }, 5L);
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event){
        this.removePlayer(event.getPlayer());
    }

    private void removePlayer(Player player) {
        Channel channel = ((CraftPlayer) player).getHandle().playerConnection.networkManager.channel;
        channel.eventLoop().submit(() -> {
            channel.pipeline().remove(player.getName());
            return null;
        });
    }

    private void injectPlayer(final Player player) {
        ChannelDuplexHandler channelDuplexHandler = new ChannelDuplexHandler() {

            @Override
            public void channelRead(ChannelHandlerContext channelHandlerContext, Object packet) throws Exception {
                // Fake entities don't trigger normal damage/interaction events, so we have to listen for PacketPlayInUseEntity packets
                if (packet instanceof PacketPlayInUseEntity) {
                    PacketPlayInUseEntity useEntityPacket = (PacketPlayInUseEntity) packet;
                    PacketPlayInUseEntity.EnumEntityUseAction entityUseAction = useEntityPacket.a();

                    // Right clicks trigger both INTERACT and sometimes INTERACT_AT,
                    // so we avoid double triggering the NPCInteractEvent by ingoring the INTERACT_AT actions
                    if (entityUseAction == PacketPlayInUseEntity.EnumEntityUseAction.INTERACT_AT) return;


                    NPCInteractAction action = entityUseAction == PacketPlayInUseEntity.EnumEntityUseAction.ATTACK ?
                        NPCInteractAction.LEFT_CLICK : NPCInteractAction.RIGHT_CLICK;

                    Field field = packet.getClass().getDeclaredField("a");
                    field.setAccessible(true);
                    int entityId = (int) field.get(packet);
                    NPC npc = NPCModule.getNPCByEntityId(entityId);

                    // only call the event if the entity interacted with is actually an NPC
                    if (npc != null) {
                        Commons.callEvent(new NPCInteractEvent(player, npc, action));
                    }

                }

                super.channelRead(channelHandlerContext, packet);
            }

            @Override
            public void write(ChannelHandlerContext channelHandlerContext, Object packet, ChannelPromise channelPromise) throws Exception {
                super.write(channelHandlerContext, packet, channelPromise);
            }

        };

        ChannelPipeline pipeline = ((CraftPlayer) player).getHandle().playerConnection.networkManager.channel.pipeline();
        pipeline.addBefore("packet_handler", player.getName(), channelDuplexHandler);
    }

}
