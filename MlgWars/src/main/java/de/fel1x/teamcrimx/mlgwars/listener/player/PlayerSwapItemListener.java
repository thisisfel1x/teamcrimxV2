package de.fel1x.teamcrimx.mlgwars.listener.player;

import de.fel1x.teamcrimx.mlgwars.MlgWars;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerSwapHandItemsEvent;

public class PlayerSwapItemListener implements Listener {

    public PlayerSwapItemListener(MlgWars mlgWars) {
        mlgWars.getPluginManager().registerEvents(this, mlgWars);
    }

    @EventHandler
    public void on(PlayerSwapHandItemsEvent event) {
        event.setCancelled(true);
    }

}
