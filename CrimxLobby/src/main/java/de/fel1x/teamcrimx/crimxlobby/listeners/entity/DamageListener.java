package de.fel1x.teamcrimx.crimxlobby.listeners.entity;

import de.fel1x.teamcrimx.crimxapi.utils.Actionbar;
import de.fel1x.teamcrimx.crimxlobby.CrimxLobby;
import de.fel1x.teamcrimx.crimxlobby.objects.LobbyPlayer;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;

public class DamageListener implements Listener {

    private final CrimxLobby crimxLobby;

    public DamageListener(CrimxLobby crimxLobby) {
        this.crimxLobby = crimxLobby;
        this.crimxLobby.getPluginManager().registerEvents(this, this.crimxLobby);
    }

    @EventHandler
    public void on(EntityDamageEvent event) {

        if (!(event.getEntity() instanceof Player)) {
            event.setCancelled(true);
        }

        Player player = (Player) event.getEntity();
        LobbyPlayer lobbyPlayer = new LobbyPlayer(player);

        if (!event.getCause().equals(EntityDamageEvent.DamageCause.FALL)) {
            event.setCancelled(true);
        } else if (lobbyPlayer.isInWaterMLG()) {
            event.setDamage(0D);

            Actionbar.sendOnlyTitle(player, "§c§l✘", 2, 5, 3);
            player.playSound(player.getLocation(), Sound.BLOCK_NOTE_BLOCK_BASS, 5, 7);
            lobbyPlayer.endWaterMLG();

            this.crimxLobby.getWaterMlgHandler().getFailed().put(player, true);

        } else {
            event.setCancelled(true);
        }

    }

}
