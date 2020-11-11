package de.fel1x.teamcrimx.mlgwars.listener.world;

import de.fel1x.teamcrimx.mlgwars.MlgWars;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.weather.WeatherChangeEvent;

public class WeatherChangeListener implements Listener {

    public WeatherChangeListener(MlgWars mlgWars) {
        mlgWars.getPluginManager().registerEvents(this, mlgWars);
    }

    @EventHandler
    public void on(WeatherChangeEvent event) {
        event.setCancelled(true);
    }

}
