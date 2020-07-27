package de.fel1x.teamcrimx.crimxlobby.listeners.world;

import de.fel1x.teamcrimx.crimxlobby.CrimxLobby;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.weather.WeatherChangeEvent;

public class WeatherChangeListener implements Listener {

    private CrimxLobby crimxLobby;

    public WeatherChangeListener(CrimxLobby crimxLobby) {
        this.crimxLobby = crimxLobby;
        this.crimxLobby.getPluginManager().registerEvents(this, this.crimxLobby);
    }

    @EventHandler
    public void on(WeatherChangeEvent event) {
        event.setCancelled(true);
    }

}
