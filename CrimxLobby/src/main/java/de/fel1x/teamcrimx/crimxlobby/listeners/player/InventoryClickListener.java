package de.fel1x.teamcrimx.crimxlobby.listeners.player;

import de.fel1x.teamcrimx.crimxlobby.CrimxLobby;
import de.fel1x.teamcrimx.crimxlobby.objects.LobbyPlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;

public class InventoryClickListener implements Listener {

    public InventoryClickListener(CrimxLobby crimxLobby) {
        crimxLobby.getPluginManager().registerEvents(this, crimxLobby);
    }

    @EventHandler
    public void on(InventoryClickEvent event) {

        if (!(event.getWhoClicked() instanceof Player)) return;

        Player player = (Player) event.getWhoClicked();
        LobbyPlayer lobbyPlayer = new LobbyPlayer(player);

        if (!lobbyPlayer.isInBuild()) {
            event.setCancelled(true);
        }
    }
}
