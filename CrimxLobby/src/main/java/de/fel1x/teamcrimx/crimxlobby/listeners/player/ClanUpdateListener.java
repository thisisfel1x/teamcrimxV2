package de.fel1x.teamcrimx.crimxlobby.listeners.player;

import de.fel1x.teamcrimx.crimxapi.clanSystem.events.ClanUpdateEvent;
import de.fel1x.teamcrimx.crimxapi.clanSystem.player.IClanPlayer;
import de.fel1x.teamcrimx.crimxlobby.CrimxLobby;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

public class ClanUpdateListener implements Listener {

    private final CrimxLobby crimxLobby;

    public ClanUpdateListener(CrimxLobby crimxLobby) {
        this.crimxLobby = crimxLobby;
        this.crimxLobby.getPluginManager().registerEvents(this, this.crimxLobby);
    }

    @EventHandler
    public void on(ClanUpdateEvent event) {

        IClanPlayer clanPlayer = event.getiClanPlayer();

    }

}
