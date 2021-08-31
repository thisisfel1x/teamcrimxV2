package de.fel1x.teamcrimx.crimxlobby.listeners.player;

import de.fel1x.teamcrimx.crimxlobby.CrimxLobby;
import de.fel1x.teamcrimx.crimxlobby.Data;
import de.fel1x.teamcrimx.crimxlobby.objects.LobbyPlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;

@Deprecated
public class MoveListener implements Listener {

    final CrimxLobby crimxLobby;
    final Data data;

    public MoveListener(CrimxLobby crimxLobby) {
        this.crimxLobby = crimxLobby;
        this.data = crimxLobby.getData();

        this.crimxLobby.getPluginManager().registerEvents(this, this.crimxLobby);
    }

    @EventHandler
    public void on(PlayerMoveEvent event) {

        Player player = event.getPlayer();
        LobbyPlayer lobbyPlayer = new LobbyPlayer(player);

    }
}
