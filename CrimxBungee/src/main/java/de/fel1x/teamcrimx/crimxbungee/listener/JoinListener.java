package de.fel1x.teamcrimx.crimxbungee.listener;

import de.fel1x.teamcrimx.crimxbungee.CrimxBungee;
import net.md_5.bungee.api.event.PostLoginEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;

public class JoinListener implements Listener {

    public JoinListener(CrimxBungee crimxBungee) {
        crimxBungee.getProxy().getPluginManager().registerListener(crimxBungee, this);
    }

    @EventHandler
    public void on(PostLoginEvent event) {
    }

}
