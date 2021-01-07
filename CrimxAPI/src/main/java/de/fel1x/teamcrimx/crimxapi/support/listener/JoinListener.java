package de.fel1x.teamcrimx.crimxapi.support.listener;

import de.fel1x.teamcrimx.crimxapi.CrimxAPI;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class JoinListener implements Listener {

    @EventHandler
    public void on(PlayerJoinEvent event) {
        event.getPlayer().sendMessage(CrimxAPI.getInstance().getPrefix() + "§7Reiche Vorschläge ein oder melde Bugs mit §a/suggest §7& §c/bug");
    }
}
