package de.fel1x.teamcrimx.crimxlobby.listeners.player;

import de.fel1x.teamcrimx.crimxapi.coins.events.AsyncPlayerCoinsChangeEvent;
import de.fel1x.teamcrimx.crimxlobby.CrimxLobby;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

public class PlayerCoinsChangeListener implements Listener {

    private final CrimxLobby crimxLobby;

    public PlayerCoinsChangeListener(CrimxLobby crimxLobby) {
        this.crimxLobby = crimxLobby;
        this.crimxLobby.getPluginManager().registerEvents(this, this.crimxLobby);
    }

    @EventHandler
    public void on(AsyncPlayerCoinsChangeEvent event) {
        this.crimxLobby.getLobbyScoreboard().updateBoard(event.getPlayer(), String.format("§8● §e%s Coins",
                event.getNewCoins()), "coins");
    }
}