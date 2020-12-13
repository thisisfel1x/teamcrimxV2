package de.fel1x.teamcrimx.floorislava.listener.player;

import de.fel1x.teamcrimx.floorislava.FloorIsLava;
import de.fel1x.teamcrimx.floorislava.gamehandler.Gamestate;
import de.fel1x.teamcrimx.floorislava.gameplayer.GamePlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.FoodLevelChangeEvent;

public class FoodListener implements Listener {
    private final FloorIsLava floorIsLava;

    public FoodListener(FloorIsLava floorIsLava) {
        this.floorIsLava = floorIsLava;
        floorIsLava.getPluginManager().registerEvents(this, floorIsLava);
    }

    @EventHandler
    public void on(FoodLevelChangeEvent event) {
        if (!(event.getEntity() instanceof Player))
            return;
        Player player = (Player) event.getEntity();
        GamePlayer gamePlayer = new GamePlayer(player);
        Gamestate gamestate = this.floorIsLava.getGamestateHandler().getGamestate();
        if (gamestate != Gamestate.RISING) {
            event.setCancelled(true);
        } else if (gamePlayer.isSpectator()) {
            event.setCancelled(true);
        }
    }
}
