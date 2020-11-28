package de.fel1x.teamcrimx.floorislava.listener.player;

import de.fel1x.teamcrimx.floorislava.FloorIsLava;
import de.fel1x.teamcrimx.floorislava.gamehandler.Gamestate;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryDragEvent;

public class InventoryMoveListener implements Listener {
    private final FloorIsLava floorIsLava;

    public InventoryMoveListener(FloorIsLava floorIsLava) {
        this.floorIsLava = floorIsLava;
        floorIsLava.getPluginManager().registerEvents(this, floorIsLava);
    }

    @Deprecated
    @EventHandler
    public void on(InventoryDragEvent event) {
        Gamestate gamestate = this.floorIsLava.getGamestateHandler().getGamestate();
    }
}
