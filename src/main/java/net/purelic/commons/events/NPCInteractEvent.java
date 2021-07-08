package net.purelic.commons.events;

import net.purelic.commons.utils.packets.NPC;
import net.purelic.commons.utils.packets.constants.NPCInteractAction;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class NPCInteractEvent extends Event {

    private static final HandlerList HANDLERS = new HandlerList();

    private final Player player;
    private final NPC npc;
    private final NPCInteractAction action;

    public NPCInteractEvent(Player player, NPC npc, NPCInteractAction action) {
        this.player = player;
        this.npc = npc;
        this.action = action;
    }

    public Player getPlayer() {
        return this.player;
    }

    public NPC getNPC() {
        return this.npc;
    }

    public NPCInteractAction getAction() {
        return this.action;
    }

    public HandlerList getHandlers() {
        return HANDLERS;
    }

    public static HandlerList getHandlerList() {
        return HANDLERS;
    }

}
