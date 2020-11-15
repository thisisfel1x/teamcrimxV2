package de.fel1x.teamcrimx.crimxlobby.listeners.player;

import de.fel1x.teamcrimx.crimxlobby.CrimxLobby;
import de.fel1x.teamcrimx.crimxlobby.Data;
import de.fel1x.teamcrimx.crimxlobby.objects.LobbyPlayer;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.FishHook;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerFishEvent;
import org.bukkit.util.Vector;

public class FishingHookListener implements Listener {

    CrimxLobby crimxLobby;
    Data data;

    public FishingHookListener(CrimxLobby crimxLobby) {
        this.crimxLobby = crimxLobby;
        this.data = crimxLobby.getData();

        this.crimxLobby.getPluginManager().registerEvents(this, this.crimxLobby);
    }

    @EventHandler
    public void on(PlayerFishEvent event) {

        Player player = event.getPlayer();
        LobbyPlayer lobbyPlayer = new LobbyPlayer(player);
        FishHook h = event.getHook();

        if (lobbyPlayer.isInBuild()) {
            return;
        }

        if ((event.getState().equals(PlayerFishEvent.State.IN_GROUND)) ||
                (event.getState().equals(PlayerFishEvent.State.CAUGHT_ENTITY)) ||
                (event.getState().equals(PlayerFishEvent.State.FAILED_ATTEMPT))) {
            if (player.getWorld().getBlockAt(h.getLocation().getBlockX(), h.getLocation().getBlockY() - 1, h.getLocation().getBlockZ()).getType() != Material.WATER) {
                Location from = player.getLocation();
                Location to = event.getHook().getLocation();

                double g = -0.08D;
                double t = to.distance(from);
                double v_x = (1.0D + 0.07D * t) * (to.getX() - from.getX()) / t;
                double v_y = (1.0D + 0.07D * t) * (to.getY() - from.getY()) / t - 1.2D * g * t; // Höhe; 0.5 standard
                double v_z = (1.0D + 0.07D * t) * (to.getZ() - from.getZ()) / t;

                Vector v = player.getVelocity();
                v.setX(v_x);
                v.setY(v_y);
                v.setZ(v_z);
                v.multiply(2);
                player.setVelocity(v);

                player.playSound(player.getLocation(), Sound.ENTITY_ENDER_DRAGON_FLAP, 3.0F, 2.0F);
            }
        }
    }
}
