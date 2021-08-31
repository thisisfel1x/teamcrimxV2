package de.fel1x.teamcrimx.crimxlobby.minigames.connectfour.objects;

import org.bukkit.entity.Player;

public class Invitation {

    private final Player whoInvited;
    private final Player target;
    private final long inviteTime;

    public Invitation(Player whoInvited, Player target) {
        this.whoInvited = whoInvited;
        this.target = target;
        this.inviteTime = System.currentTimeMillis();
    }

    public Player getWhoInvited() {
        return this.whoInvited;
    }

    public Player getTarget() {
        return this.target;
    }

    public long getInviteTime() {
        return this.inviteTime;
    }
}
