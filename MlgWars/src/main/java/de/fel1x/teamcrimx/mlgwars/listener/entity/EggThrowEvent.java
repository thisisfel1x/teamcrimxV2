package de.fel1x.teamcrimx.mlgwars.listener.entity;

import de.fel1x.teamcrimx.mlgwars.MlgWars;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerEggThrowEvent;

public class EggThrowEvent implements Listener {

    private MlgWars mlgWars;

    public EggThrowEvent(MlgWars mlgWars) {
        this.mlgWars = mlgWars;
        this.mlgWars.getPluginManager().registerEvents(this, this.mlgWars);
    }

    @EventHandler
    public void on(PlayerEggThrowEvent event) {
        event.setHatching(false);
    }

}
