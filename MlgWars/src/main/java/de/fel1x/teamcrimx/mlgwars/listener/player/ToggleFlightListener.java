package de.fel1x.teamcrimx.mlgwars.listener.player;

import de.fel1x.teamcrimx.mlgwars.MlgWars;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityToggleGlideEvent;

public class ToggleFlightListener implements Listener {

    private MlgWars mlgWars;

    public ToggleFlightListener(MlgWars mlgWars) {
        this.mlgWars = mlgWars;
        this.mlgWars.getPluginManager().registerEvents(this, this.mlgWars);
    }

    @EventHandler
    public void on(EntityToggleGlideEvent event) {

        if (!event.getEntity().isOnGround()) {
            event.setCancelled(true);
        }

    }

}
