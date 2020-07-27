package de.fel1x.teamcrimx.crimxapi.support;

import de.fel1x.teamcrimx.crimxapi.CrimxAPI;
import net.md_5.bungee.api.plugin.Plugin;

public class CrimxBungeeAPI extends Plugin {

    @Override
    public void onEnable() {

        this.getProxy().getConsole().sendMessage("§eTrying to load CrimxAPI v1 by fel1x");

        new CrimxAPI();

        this.getProxy().getConsole().sendMessage("§aLoaded CrimxAPI v1 by fel1x");

    }

    @Override
    public void onDisable() {


    }

}
