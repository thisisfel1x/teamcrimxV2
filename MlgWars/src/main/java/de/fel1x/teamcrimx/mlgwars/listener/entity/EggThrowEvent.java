package de.fel1x.teamcrimx.mlgwars.listener.entity;

import de.fel1x.teamcrimx.mlgwars.MlgWars;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerEggThrowEvent;

public class EggThrowEvent implements Listener {

    public EggThrowEvent(MlgWars mlgWars) {
        mlgWars.getPluginManager().registerEvents(this, mlgWars);
    }

    @EventHandler
    public void on(PlayerEggThrowEvent event) {
        event.setHatching(false);
    }

}
