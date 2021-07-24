package de.fel1x.teamcrimx.crimxlobby.listeners.entity;

import de.fel1x.teamcrimx.crimxlobby.CrimxLobby;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;

public class DamageListener implements Listener {

    public DamageListener(CrimxLobby crimxLobby) {
        crimxLobby.getPluginManager().registerEvents(this, crimxLobby);
    }

    @EventHandler
    public void on(EntityDamageEvent event) {
        event.setCancelled(true);
    }

}
