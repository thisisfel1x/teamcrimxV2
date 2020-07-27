package de.fel1x.teamcrimx.crimxbungee;

import de.fel1x.teamcrimx.crimxbungee.listener.JoinListener;
import net.md_5.bungee.api.plugin.Plugin;

public final class CrimxBungee extends Plugin {

    private String prefix = "§eCrimx§lBungee §8» §r";

    @Override
    public void onEnable() {

        new JoinListener(this);

    }

    @Override
    public void onDisable() {


    }
}
