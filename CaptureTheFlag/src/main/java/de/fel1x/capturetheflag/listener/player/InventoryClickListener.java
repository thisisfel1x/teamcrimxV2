package de.fel1x.capturetheflag.listener.player;

import de.fel1x.capturetheflag.CaptureTheFlag;
import de.fel1x.capturetheflag.gamestate.Gamestate;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;

public class InventoryClickListener implements Listener {

    @EventHandler
    public void on(InventoryClickEvent event) {

        if (!CaptureTheFlag.getInstance().getGamestateHandler().getGamestate().equals(Gamestate.INGAME)) {
            event.setCancelled(true);
        }

    }

}
