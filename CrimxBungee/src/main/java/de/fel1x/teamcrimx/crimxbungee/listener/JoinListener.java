package de.fel1x.teamcrimx.crimxbungee.listener;

import de.fel1x.teamcrimx.crimxbungee.CrimxBungee;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.event.PostLoginEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;

import java.awt.*;

public class JoinListener implements Listener {

    private CrimxBungee crimxBungee;

    public JoinListener(CrimxBungee crimxBungee) {
        this.crimxBungee = crimxBungee;
        this.crimxBungee.getProxy().getPluginManager().registerListener(this.crimxBungee, this);
    }

    @EventHandler
    public void on(PostLoginEvent event) {
    }

}
