package de.fel1x.teamcrimx.floorislava.listener.block;

import de.fel1x.teamcrimx.floorislava.FloorIsLava;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockFromToEvent;

public class BlockFromToListener implements Listener {

    private FloorIsLava floorIsLava;

    public BlockFromToListener(FloorIsLava floorIsLava) {
        this.floorIsLava = floorIsLava;
        this.floorIsLava.getPluginManager().registerEvents(this, this.floorIsLava);
    }

    @EventHandler
    public void on(BlockFromToEvent event) {

        if (!this.floorIsLava.getAreaCuboid().contains(event.getToBlock())) {
            event.setCancelled(true);
        }

    }
}
