package de.fel1x.teamcrimx.crimxlobby.listeners.player;

import de.fel1x.teamcrimx.crimxlobby.CrimxLobby;
import de.fel1x.teamcrimx.crimxlobby.objects.LobbyPlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

public class QuitListener implements Listener {

    private CrimxLobby crimxLobby;

    public QuitListener(CrimxLobby crimxLobby) {
        this.crimxLobby = crimxLobby;
        this.crimxLobby.getPluginManager().registerEvents(this, this.crimxLobby);
    }

    @EventHandler
    public void on(PlayerQuitEvent event) {

        Player player = event.getPlayer();
        LobbyPlayer lobbyPlayer = new LobbyPlayer(player);

        event.setQuitMessage(null);

        lobbyPlayer.saveNewLocation();

        if(lobbyPlayer.isInBuild()) {
            lobbyPlayer.removeFromBuild();
        }

        if(lobbyPlayer.isInJumpAndRun()) {
            lobbyPlayer.endJumpAndRun();
        }

    }

}
