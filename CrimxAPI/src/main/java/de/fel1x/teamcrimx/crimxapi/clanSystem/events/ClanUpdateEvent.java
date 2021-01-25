package de.fel1x.teamcrimx.crimxapi.clanSystem.events;

import de.fel1x.teamcrimx.crimxapi.clanSystem.player.IClanPlayer;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;

public class ClanUpdateEvent extends Event {

    private final IClanPlayer iClanPlayer;
    private static final HandlerList handlers = new HandlerList();

    public ClanUpdateEvent(IClanPlayer iClanPlayer) {
        this.iClanPlayer = iClanPlayer;
    }

    public ClanUpdateEvent(boolean isAsync, IClanPlayer iClanPlayer) {
        super(isAsync);
        this.iClanPlayer = iClanPlayer;
    }

    public IClanPlayer getiClanPlayer() {
        return this.iClanPlayer;
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
