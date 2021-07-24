package de.fel1x.teamcrimx.crimxapi.support;

import com.github.juliarn.npc.NPCPool;
import de.dytanic.cloudnet.driver.CloudNetDriver;
import de.dytanic.cloudnet.wrapper.Wrapper;
import de.fel1x.teamcrimx.crimxapi.CrimxAPI;
import de.fel1x.teamcrimx.crimxapi.clanSystem.commands.ClanCommand;
import de.fel1x.teamcrimx.crimxapi.commands.CoinsCommand;
import de.fel1x.teamcrimx.crimxapi.commands.JoinMeCommand;
import de.fel1x.teamcrimx.crimxapi.cosmetic.runnable.CosmeticTask;
import de.fel1x.teamcrimx.crimxapi.friends.commands.FriendCommand;
import de.fel1x.teamcrimx.crimxapi.friends.listener.FriendListener;
import de.fel1x.teamcrimx.crimxapi.party.commands.PartyCommand;
import de.fel1x.teamcrimx.crimxapi.server.ServerType;
import de.fel1x.teamcrimx.crimxapi.support.listener.JoinListener;
import org.bukkit.Bukkit;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Locale;

public class CrimxSpigotAPI extends JavaPlugin {

    private static CrimxSpigotAPI instance;
    private PluginManager pluginManager;

    private NPCPool npcPool;

    private CosmeticTask cosmeticTask;

    @Override
    public void onEnable() {

        instance = this;
        this.pluginManager = Bukkit.getPluginManager();

        Bukkit.getConsoleSender().sendMessage("§eTrying to load CrimxAPI v2 by fel1x");

        new CrimxAPI();

        this.setServerType();
        this.registerCommands();
        this.registerListener();

        this.npcPool = NPCPool.builder(this)
                .spawnDistance(60)
                .actionDistance(30)
                .tabListRemoveTicks(20)
                .build();

        this.cosmeticTask = new CosmeticTask(this);

        Bukkit.getConsoleSender().sendMessage("§aLoaded CrimxAPI v2 by fel1x");

    }

    private void registerListener() {
        // BUKKIT
        new JoinListener(this);

        // CLOUDNET
        new FriendListener();
    }

    private void registerCommands() {

        // BASIC COMMANDS
        new CoinsCommand(this);
        new JoinMeCommand(this);
        new ClanCommand(this);
        new FriendCommand(this);
        new PartyCommand(this);

    }

    private void setServerType() {
        switch (Wrapper.getInstance().getServiceId().getTaskName()) {
            case "Proxy":
                CrimxAPI.getInstance().setServerType(ServerType.UNKNOWN);
                break;
            case "Lobby": case "PremiumLobby": case "SilentLobby":
                CrimxAPI.getInstance().setServerType(ServerType.LOBBY_SERVER);
                break;
            default:
                CrimxAPI.getInstance().setServerType(ServerType.GAME_SERVER);
                break;
        }
    }

    @Override
    public void onDisable() {
        CrimxAPI.getInstance().getMongoDB().getMongoClient().close();
    }

    public static CrimxSpigotAPI getInstance() {
        return instance;
    }

    public PluginManager getPluginManager() {
        return this.pluginManager;
    }

    public NPCPool getNpcPool() {
        return this.npcPool;
    }

    public CosmeticTask getCosmeticTask() {
        return this.cosmeticTask;
    }
}
