package de.fel1x.teamcrimx.crimxapi.coins.events;

import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;

public class AsyncPlayerCoinsChangeEvent extends Event {

    private final Player player;
    private final int newCoins;

    private static final HandlerList handlers = new HandlerList();

    public AsyncPlayerCoinsChangeEvent(Player player, int newCoins) {
        this.player = player;
        this.newCoins = newCoins;
    }

    public AsyncPlayerCoinsChangeEvent(boolean isAsync, Player player, int newCoins) {
        super(isAsync);
        this.player = player;
        this.newCoins = newCoins;
    }

    public Player getPlayer() {
        return this.player;
    }

    public int getNewCoins() {
        return this.newCoins;
    }

    @NotNull
    @Override
    public HandlerList getHandlers() {
        return handlers;
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }
}
