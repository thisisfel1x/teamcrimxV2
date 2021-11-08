package de.fel1x.teamcrimx.mlgwars.listener.player;

import de.fel1x.teamcrimx.mlgwars.MlgWars;
import de.fel1x.teamcrimx.mlgwars.enums.Spawns;
import de.fel1x.teamcrimx.mlgwars.gamestate.Gamestate;
import de.fel1x.teamcrimx.mlgwars.objects.GamePlayer;
import de.fel1x.teamcrimx.mlgwars.utils.WinDetection;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerRespawnEvent;

public class RespawnListener implements Listener {

    private final MlgWars mlgWars;

    public RespawnListener(MlgWars mlgWars) {
        this.mlgWars = mlgWars;
        this.mlgWars.getPluginManager().registerEvents(this, this.mlgWars);
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void on(PlayerRespawnEvent event) {

        Player player = event.getPlayer();
        GamePlayer gamePlayer = this.mlgWars.getData().getGamePlayers().get(player.getUniqueId());

        Gamestate gamestate = this.mlgWars.getGamestateHandler().getGamestate();

        if (gamestate == Gamestate.DELAY || gamestate == Gamestate.PREGAME || gamestate == Gamestate.INGAME) {
            event.setRespawnLocation(Spawns.SPECTATOR.getLocation());
            Bukkit.getScheduler().runTaskLater(this.mlgWars, () -> {
                gamePlayer.activateSpectatorMode();
                player.setAllowFlight(true);
                player.setFlying(true);
            }, 2L);
        } else {
            event.setRespawnLocation(Spawns.LOBBY.getLocation());
        }
    }
}
