package de.fel1x.teamcrimx.crimxlobby.listeners.player;

import de.fel1x.teamcrimx.crimxapi.utils.Actionbar;
import de.fel1x.teamcrimx.crimxlobby.CrimxLobby;
import de.fel1x.teamcrimx.crimxlobby.objects.LobbyPlayer;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerBucketEmptyEvent;

public class WaterBucketEmptyListener implements Listener {

    CrimxLobby crimxLobby;

    public WaterBucketEmptyListener(CrimxLobby crimxLobby) {
        this.crimxLobby = crimxLobby;
        this.crimxLobby.getPluginManager().registerEvents(this, this.crimxLobby);
    }

    @EventHandler(ignoreCancelled = true)
    public void on(PlayerBucketEmptyEvent event) {

        Player player = event.getPlayer();
        LobbyPlayer lobbyPlayer = new LobbyPlayer(player);

        if (lobbyPlayer.isInWaterMLG()) {
            Bukkit.getScheduler().scheduleSyncDelayedTask(CrimxLobby.getInstance(), () -> {
                if (!lobbyPlayer.isMlgFailed()) {

                    Actionbar.sendTitle(player, "§a§l✔", 2, 5, 3);
                    player.playSound(player.getLocation(), Sound.ORB_PICKUP, 2, 4);
                    lobbyPlayer.endWaterMLG();

                    event.getBlockClicked().getLocation().clone().add(0, 1, 0).getBlock().setType(Material.STATIONARY_WATER);
                }

            }, 5L);

            Bukkit.getScheduler().scheduleSyncDelayedTask(CrimxLobby.getInstance(), () -> event.getBlockClicked().getLocation().clone().add(0, 1, 0).getBlock().setType(Material.AIR), 15L);

        }

    }

}
