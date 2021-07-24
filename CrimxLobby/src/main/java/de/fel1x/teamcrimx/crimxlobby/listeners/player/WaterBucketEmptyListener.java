package de.fel1x.teamcrimx.crimxlobby.listeners.player;

import de.fel1x.teamcrimx.crimxlobby.CrimxLobby;
import de.fel1x.teamcrimx.crimxlobby.objects.LobbyPlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerBucketEmptyEvent;

public class WaterBucketEmptyListener implements Listener {

    final CrimxLobby crimxLobby;

    public WaterBucketEmptyListener(CrimxLobby crimxLobby) {
        this.crimxLobby = crimxLobby;
        this.crimxLobby.getPluginManager().registerEvents(this, this.crimxLobby);
    }

    @EventHandler(ignoreCancelled = true)
    public void on(PlayerBucketEmptyEvent event) {

        Player player = event.getPlayer();
        LobbyPlayer lobbyPlayer = new LobbyPlayer(player);

        // TODO: either rework this whole shit or do something different

    }

}
