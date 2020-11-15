package de.fel1x.capturetheflag.listener.player;

import de.fel1x.capturetheflag.CaptureTheFlag;
import de.fel1x.capturetheflag.gameplayer.GamePlayer;
import de.fel1x.capturetheflag.gamestate.Gamestate;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityPickupItemEvent;

public class PickupListener implements Listener {

    private final CaptureTheFlag captureTheFlag;

    public PickupListener(CaptureTheFlag captureTheFlag) {
        this.captureTheFlag = captureTheFlag;
        this.captureTheFlag.getPluginManager().registerEvents(this, this.captureTheFlag);
    }

    @EventHandler
    public void on(EntityPickupItemEvent event) {

        if (!(event.getEntity() instanceof Player)) {
            return;
        }

        Player player = (Player) event.getEntity();
        GamePlayer gamePlayer = new GamePlayer(player);

        Gamestate gamestate = this.captureTheFlag.getGamestateHandler().getGamestate();

        if (!gamePlayer.isPlayer()) {
            event.setCancelled(true);
            return;
        }

        if (!gamestate.equals(Gamestate.INGAME)) {
            event.setCancelled(true);
        }

    }

}
