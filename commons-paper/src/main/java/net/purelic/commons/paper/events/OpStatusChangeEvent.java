package net.purelic.commons.paper.events;

import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class OpStatusChangeEvent extends Event implements Cancellable {

    private static final HandlerList HANDLERS = new HandlerList();

    private final Player player;
    private final Player changedBy;
    private final boolean op;
    private final boolean ownerJoin;
    private boolean cancelled = false;

    public OpStatusChangeEvent(Player player, Player changedBy, boolean op) {
        this(player, changedBy, op, false);
    }

    public OpStatusChangeEvent(Player player, Player changedBy, boolean op, boolean ownerJoin) {
        this.player = player;
        this.changedBy = changedBy;
        this.op = op;
        this.ownerJoin = ownerJoin;
    }

    public Player getPlayer() {
        return this.player;
    }

    public Player getChangedBy() {
        return this.changedBy;
    }

    public boolean isOp() {
        return this.op;
    }

    public boolean isOwnerJoin() {
        return this.ownerJoin;
    }

    public HandlerList getHandlers() {
        return HANDLERS;
    }

    public static HandlerList getHandlerList() {
        return HANDLERS;
    }

    @Override
    public boolean isCancelled() {
        return this.cancelled;
    }

    @Override
    public void setCancelled(boolean b) {
        this.cancelled = b;
    }

}
