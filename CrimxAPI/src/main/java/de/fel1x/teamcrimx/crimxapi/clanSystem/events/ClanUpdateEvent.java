package de.fel1x.teamcrimx.crimxapi.clanSystem.events;

import de.fel1x.teamcrimx.crimxapi.clanSystem.player.IClanPlayer;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;

public class ClanUpdateEvent extends Event {

    private static final HandlerList handlers = new HandlerList();
    private final IClanPlayer iClanPlayer;

    public ClanUpdateEvent(IClanPlayer iClanPlayer) {
        this.iClanPlayer = iClanPlayer;
    }

    public ClanUpdateEvent(boolean isAsync, IClanPlayer iClanPlayer) {
        super(isAsync);
        this.iClanPlayer = iClanPlayer;
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }

    public IClanPlayer getiClanPlayer() {
        return this.iClanPlayer;
    }

    @NotNull
    @Override
    public HandlerList getHandlers() {
        return handlers;
    }
}
