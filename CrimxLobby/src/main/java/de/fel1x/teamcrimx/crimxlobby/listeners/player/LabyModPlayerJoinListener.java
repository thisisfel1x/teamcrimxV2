package de.fel1x.teamcrimx.crimxlobby.listeners.player;

import de.fel1x.teamcrimx.crimxlobby.CrimxLobby;
import net.labymod.serverapi.bukkit.event.LabyModPlayerJoinEvent;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.metadata.FixedMetadataValue;

public class LabyModPlayerJoinListener implements Listener {

    private CrimxLobby crimxLobby;

    public LabyModPlayerJoinListener(CrimxLobby crimxLobby) {
        this.crimxLobby = crimxLobby;
        this.crimxLobby.getPluginManager().registerEvents(this, this.crimxLobby);
    }

    @EventHandler
    public void on(LabyModPlayerJoinEvent event) {
        event.getPlayer().setMetadata("labymod", new FixedMetadataValue(this.crimxLobby, true));
    }
}
