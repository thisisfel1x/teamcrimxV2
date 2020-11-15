package de.fel1x.teamcrimx.crimxbungee;

import de.fel1x.teamcrimx.crimxapi.CrimxAPI;
import de.fel1x.teamcrimx.crimxbungee.listener.JoinListener;
import net.md_5.bungee.api.plugin.Plugin;

public final class CrimxBungee extends Plugin {

    private static CrimxBungee instance;
    private final String prefix = "§eCrimx§lBungee §8» §r";
    private CrimxAPI crimxAPI;

    public static CrimxBungee getInstance() {
        return instance;
    }

    @Override
    public void onEnable() {

        instance = this;

        this.crimxAPI = CrimxAPI.getInstance();

        new JoinListener(this);

    }

    @Override
    public void onDisable() {


    }

    public CrimxAPI getCrimxAPI() {
        return this.crimxAPI;
    }
}
