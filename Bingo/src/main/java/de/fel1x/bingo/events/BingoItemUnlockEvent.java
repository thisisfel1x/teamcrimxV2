package de.fel1x.bingo.events;

import de.fel1x.bingo.objects.BingoItem;
import de.fel1x.bingo.objects.BingoTeam;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class BingoItemUnlockEvent extends Event {

    private static final HandlerList handlers = new HandlerList();
    private final Player player;
    private final BingoTeam team;
    private final BingoItem bingoItem;

    public BingoItemUnlockEvent(Player player, BingoTeam team, BingoItem bingoItem) {
        this.player = player;
        this.team = team;
        this.bingoItem = bingoItem;
    }

    public BingoItemUnlockEvent(boolean isAsync, Player player, BingoTeam team, BingoItem bingoItem) {
        super(isAsync);
        this.player = player;
        this.team = team;
        this.bingoItem = bingoItem;
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }

    public Player getPlayer() {
        return this.player;
    }

    public BingoTeam getTeam() {
        return this.team;
    }

    public BingoItem getBingoItem() {
        return this.bingoItem;
    }

    public HandlerList getHandlers() {
        return handlers;
    }
}
