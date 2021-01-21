package de.fel1x.bingo.listener.block;

import de.fel1x.bingo.Bingo;
import org.bukkit.entity.Entity;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityChangeBlockEvent;

public class BlockTransformListener implements Listener {

    private final Bingo bingo;

    public BlockTransformListener(Bingo bingo) {
        this.bingo = bingo;
        bingo.getPluginManager().registerEvents(this, bingo);
    }

    @EventHandler
    public void on(EntityChangeBlockEvent event) {

        Entity entity = event.getEntity();

    }

}
