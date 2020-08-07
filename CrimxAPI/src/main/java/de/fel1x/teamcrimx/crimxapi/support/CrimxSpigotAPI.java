package de.fel1x.teamcrimx.crimxapi.support;

import de.fel1x.teamcrimx.crimxapi.CrimxAPI;
import de.fel1x.teamcrimx.crimxapi.commands.CoinsCommand;
import de.fel1x.teamcrimx.crimxapi.commands.JoinMeCommand;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

public class CrimxSpigotAPI extends JavaPlugin {

    @Override
    public void onEnable() {

        Bukkit.getConsoleSender().sendMessage("§eTrying to load CrimxAPI v1 by fel1x");

        new CrimxAPI();
        getCommand("coins").setExecutor(new CoinsCommand());
        getCommand("joinme").setExecutor(new JoinMeCommand());

        Bukkit.getConsoleSender().sendMessage("§aLoaded CrimxAPI v1 by fel1x");

    }

    @Override
    public void onDisable() {


    }

}
