package de.fel1x.capturetheflag.listener.player;

import de.fel1x.capturetheflag.CaptureTheFlag;
import de.fel1x.capturetheflag.gameplayer.GamePlayer;
import de.fel1x.capturetheflag.gamestate.Gamestate;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerPickupItemEvent;

public class PickupListener implements Listener {

    @EventHandler
    public void on(PlayerPickupItemEvent event) {

        Player player = event.getPlayer();
        GamePlayer gamePlayer = new GamePlayer(player);

        Gamestate gamestate = CaptureTheFlag.getInstance().getGamestateHandler().getGamestate();

        if(!gamePlayer.isPlayer()) {
            event.setCancelled(true);
            return;
        }

        if(!gamestate.equals(Gamestate.INGAME)) {
            event.setCancelled(true);
        }

    }

}
