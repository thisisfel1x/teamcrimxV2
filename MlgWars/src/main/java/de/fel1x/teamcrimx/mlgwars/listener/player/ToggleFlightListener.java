package de.fel1x.teamcrimx.mlgwars.listener.player;

import de.fel1x.teamcrimx.mlgwars.MlgWars;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityToggleGlideEvent;

public class ToggleFlightListener implements Listener {

    public ToggleFlightListener(MlgWars mlgWars) {
        mlgWars.getPluginManager().registerEvents(this, mlgWars);
    }

    @EventHandler
    public void on(EntityToggleGlideEvent event) {

        if (!event.getEntity().isOnGround()) {
            event.setCancelled(true);
        }

    }

}
