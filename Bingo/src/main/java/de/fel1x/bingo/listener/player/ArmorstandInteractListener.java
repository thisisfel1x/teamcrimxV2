package de.fel1x.bingo.listener.player;

import de.fel1x.bingo.Bingo;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerArmorStandManipulateEvent;

public class ArmorstandInteractListener implements Listener {

    public ArmorstandInteractListener(Bingo bingo) {
        bingo.getPluginManager().registerEvents(this, bingo);
    }

    @EventHandler
    public void on(PlayerArmorStandManipulateEvent event) {
        event.setCancelled(true);
    }

}
