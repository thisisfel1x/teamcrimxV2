package de.fel1x.teamcrimx.crimxapi.support;

import de.fel1x.teamcrimx.crimxapi.CrimxAPI;
import de.fel1x.teamcrimx.crimxapi.commands.BugCommand;
import de.fel1x.teamcrimx.crimxapi.commands.CoinsCommand;
import de.fel1x.teamcrimx.crimxapi.commands.JoinMeCommand;
import de.fel1x.teamcrimx.crimxapi.commands.SuggestCommand;
import de.fel1x.teamcrimx.crimxapi.support.listener.JoinListener;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

public class CrimxSpigotAPI extends JavaPlugin {

    @Override
    public void onEnable() {

        Bukkit.getConsoleSender().sendMessage("§eTrying to load CrimxAPI v1 by fel1x");

        new CrimxAPI();

        getCommand("coins").setExecutor(new CoinsCommand());
        getCommand("joinme").setExecutor(new JoinMeCommand());

        new SuggestCommand(this);
        new BugCommand(this);

        Bukkit.getPluginManager().registerEvents(new JoinListener(), this);

        Bukkit.getConsoleSender().sendMessage("§aLoaded CrimxAPI v1 by fel1x");

    }

    @Override
    public void onDisable() {


    }

}
