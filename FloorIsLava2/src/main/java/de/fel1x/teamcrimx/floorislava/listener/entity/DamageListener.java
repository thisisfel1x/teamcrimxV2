package de.fel1x.teamcrimx.floorislava.listener.entity;

import de.fel1x.teamcrimx.floorislava.FloorIsLava;
import de.fel1x.teamcrimx.floorislava.gamehandler.Gamestate;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;

public class DamageListener implements Listener {
    private final FloorIsLava floorIsLava;

    public DamageListener(FloorIsLava floorIsLava) {
        this.floorIsLava = floorIsLava;
        floorIsLava.getPluginManager().registerEvents(this, floorIsLava);
    }

    @EventHandler
    public void on(EntityDamageByEntityEvent event) {
        Gamestate gamestate = this.floorIsLava.getGamestateHandler().getGamestate();
        if (gamestate != Gamestate.FARMING && gamestate != Gamestate.RISING) {
            event.setCancelled(true);
        } else if (gamestate == Gamestate.FARMING &&
                event.getEntity() instanceof org.bukkit.entity.Player) {
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void on(EntityDamageEvent event) {
        Gamestate gamestate = this.floorIsLava.getGamestateHandler().getGamestate();
        if (gamestate != Gamestate.FARMING && gamestate != Gamestate.RISING) {
            event.setCancelled(true);
        } else if (gamestate == Gamestate.FARMING &&
                event.getEntity() instanceof org.bukkit.entity.Player) {
            event.setCancelled(true);
        }
    }
}
