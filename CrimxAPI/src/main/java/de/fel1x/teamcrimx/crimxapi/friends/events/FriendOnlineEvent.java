package de.fel1x.teamcrimx.crimxapi.friends.events;

import de.fel1x.teamcrimx.crimxapi.friends.database.IFriendPlayer;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;

public class FriendOnlineEvent extends Event {

    private static final HandlerList handlers = new HandlerList();
    private final IFriendPlayer friendPlayer;

    public FriendOnlineEvent(IFriendPlayer friendPlayer) {
        this.friendPlayer = friendPlayer;
    }

    public FriendOnlineEvent(boolean isAsync, IFriendPlayer friendPlayer) {
        super(isAsync);
        this.friendPlayer = friendPlayer;
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }

    public IFriendPlayer getFriendPlayer() {
        return this.friendPlayer;
    }

    @Override
    public @NotNull HandlerList getHandlers() {
        return handlers;
    }

}
