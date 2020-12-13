package de.fel1x.teamcrimx.floorislava.listener.player;

import de.fel1x.teamcrimx.floorislava.FloorIsLava;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerArmorStandManipulateEvent;

public class ArmorstandInteractListener implements Listener {
    public ArmorstandInteractListener(FloorIsLava floorIsLava) {
        floorIsLava.getPluginManager().registerEvents(this, floorIsLava);
    }

    @EventHandler
    public void on(PlayerArmorStandManipulateEvent event) {
        event.setCancelled(true);
    }
}
