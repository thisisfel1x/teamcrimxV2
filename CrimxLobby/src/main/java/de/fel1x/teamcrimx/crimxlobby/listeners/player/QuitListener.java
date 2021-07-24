package de.fel1x.teamcrimx.crimxlobby.listeners.player;

import de.fel1x.teamcrimx.crimxlobby.CrimxLobby;
import de.fel1x.teamcrimx.crimxlobby.objects.LobbyPlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

public class QuitListener implements Listener {

    private final CrimxLobby crimxLobby;

    public QuitListener(CrimxLobby crimxLobby) {
        this.crimxLobby = crimxLobby;
        crimxLobby.getPluginManager().registerEvents(this, crimxLobby);
    }

    @EventHandler
    public void on(PlayerQuitEvent event) {

        Player player = event.getPlayer();
        LobbyPlayer lobbyPlayer = new LobbyPlayer(player);

        event.quitMessage(null);

        lobbyPlayer.saveNewLocation();

        if (this.crimxLobby.getData().getPlayerPet().containsKey(player.getUniqueId())) {
            this.crimxLobby.getData().getPlayerPet().get(player.getUniqueId()).remove();
            this.crimxLobby.getData().getPlayerPet().remove(player.getUniqueId());
        }

        if (lobbyPlayer.isInBuild()) {
            lobbyPlayer.removeFromBuild();
        }

    }

}
