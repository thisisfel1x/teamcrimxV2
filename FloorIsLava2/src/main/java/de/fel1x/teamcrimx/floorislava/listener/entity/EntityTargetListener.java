package de.fel1x.teamcrimx.floorislava.listener.entity;

import de.fel1x.teamcrimx.floorislava.FloorIsLava;
import de.fel1x.teamcrimx.floorislava.gameplayer.GamePlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityTargetEvent;

public class EntityTargetListener implements Listener {
    public EntityTargetListener(FloorIsLava floorIsLava) {
        floorIsLava.getPluginManager().registerEvents(this, floorIsLava);
    }

    @EventHandler
    public void on(EntityTargetEvent event) {
        if (event.getTarget() instanceof Player) {
            Player player = (Player) event.getTarget();
            GamePlayer gamePlayer = new GamePlayer(player);
            if (gamePlayer.isSpectator())
                event.setCancelled(true);
        }
    }
}
