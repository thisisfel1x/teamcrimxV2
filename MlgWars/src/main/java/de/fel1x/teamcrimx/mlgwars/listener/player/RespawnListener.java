package de.fel1x.teamcrimx.mlgwars.listener.player;

import de.fel1x.teamcrimx.mlgwars.MlgWars;
import de.fel1x.teamcrimx.mlgwars.enums.Spawns;
import de.fel1x.teamcrimx.mlgwars.gamestate.Gamestate;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerRespawnEvent;

public class RespawnListener implements Listener {

    private MlgWars mlgWars;

    public RespawnListener(MlgWars mlgWars) {
        this.mlgWars = mlgWars;
        this.mlgWars.getPluginManager().registerEvents(this, this.mlgWars);
    }

    @EventHandler
    public void on(PlayerRespawnEvent event) {

        Gamestate gamestate = this.mlgWars.getGamestateHandler().getGamestate();

        if(gamestate == Gamestate.DELAY || gamestate == Gamestate.PREGAME || gamestate == Gamestate.INGAME) {
            event.setRespawnLocation(Spawns.SPECTATOR.getLocation());
        } else {
            event.setRespawnLocation(Spawns.LOBBY.getLocation());
        }

    }
}
