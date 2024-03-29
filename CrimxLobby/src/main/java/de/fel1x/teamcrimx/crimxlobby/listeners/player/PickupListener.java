package de.fel1x.teamcrimx.crimxlobby.listeners.player;

import de.fel1x.teamcrimx.crimxlobby.CrimxLobby;
import de.fel1x.teamcrimx.crimxlobby.objects.LobbyPlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityPickupItemEvent;
import org.bukkit.event.player.PlayerDropItemEvent;

public class PickupListener implements Listener {

    private final CrimxLobby crimxLobby;

    public PickupListener(CrimxLobby crimxLobby) {
        this.crimxLobby = crimxLobby;
        this.crimxLobby.getPluginManager().registerEvents(this, this.crimxLobby);
    }

    @EventHandler
    public void on(EntityPickupItemEvent event) {

        if (!(event.getEntity() instanceof Player)) {
            return;
        }

        Player player = (Player) event.getEntity();
        LobbyPlayer lobbyPlayer = new LobbyPlayer(player);

        if (!lobbyPlayer.isInBuild()) {
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void on(PlayerDropItemEvent event) {

        Player player = event.getPlayer();
        LobbyPlayer lobbyPlayer = new LobbyPlayer(player);

        if (!lobbyPlayer.isInBuild()) {
            event.setCancelled(true);
        }

    }

}
