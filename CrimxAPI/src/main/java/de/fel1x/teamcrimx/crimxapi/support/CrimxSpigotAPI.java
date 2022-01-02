package de.fel1x.teamcrimx.crimxapi.support;

import com.github.juliarn.npc.NPCPool;
import de.dytanic.cloudnet.ext.bridge.BridgeHelper;
import de.dytanic.cloudnet.ext.bridge.bukkit.BukkitCloudNetHelper;
import de.dytanic.cloudnet.wrapper.Wrapper;
import de.fel1x.teamcrimx.crimxapi.CrimxAPI;
import de.fel1x.teamcrimx.crimxapi.clanSystem.commands.ClanCommand;
import de.fel1x.teamcrimx.crimxapi.commands.CoinsCommand;
import de.fel1x.teamcrimx.crimxapi.commands.JoinMeCommand;
import de.fel1x.teamcrimx.crimxapi.cosmetic.database.ActiveCosmetics;
import de.fel1x.teamcrimx.crimxapi.cosmetic.debug.CosmeticDebugCommand;
import de.fel1x.teamcrimx.crimxapi.friends.commands.FriendCommand;
import de.fel1x.teamcrimx.crimxapi.friends.listener.FriendListener;
import de.fel1x.teamcrimx.crimxapi.party.commands.PartyCommand;
import de.fel1x.teamcrimx.crimxapi.server.ServerType;
import de.fel1x.teamcrimx.crimxapi.support.listener.JoinQuitListener;
import org.bukkit.Bukkit;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.HashMap;
import java.util.UUID;

public class CrimxSpigotAPI extends JavaPlugin {

    private static CrimxSpigotAPI instance;
    private PluginManager pluginManager;

    private NPCPool npcPool;

    private HashMap<UUID, ActiveCosmetics> activeCosmeticsHashMap;

    public static CrimxSpigotAPI getInstance() {
        return instance;
    }

    @Override
    public void onEnable() {

        instance = this;
        this.pluginManager = Bukkit.getPluginManager();

        Bukkit.getConsoleSender().sendMessage("§eTrying to load CrimxAPI v2 by fel1x");

        new CrimxAPI();

        this.setServerType();

        if(CrimxAPI.getInstance().getServerType() == ServerType.GAME_SERVER) {
            // Prevent "loading" sign
            BukkitCloudNetHelper.setExtra("running");
            BridgeHelper.updateServiceInfo();
        }

        this.registerCommands();
        this.registerListener();

        this.activeCosmeticsHashMap = new HashMap<>();

        this.npcPool = NPCPool.builder(this)
                .spawnDistance(60)
                .actionDistance(30)
                .tabListRemoveTicks(100)
                .build();

        Bukkit.getConsoleSender().sendMessage("§aLoaded CrimxAPI v2 by fel1x");

    }

    private void registerListener() {
        // BUKKIT
        new JoinQuitListener(this);

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

        // DEBUG
        new CosmeticDebugCommand(this);

    }

    private void setServerType() {
        switch (Wrapper.getInstance().getServiceId().getTaskName()) {
            case "Proxy" -> CrimxAPI.getInstance().setServerType(ServerType.UNKNOWN);
            case "Lobby", "PremiumLobby", "SilentLobby" -> CrimxAPI.getInstance().setServerType(ServerType.LOBBY_SERVER);
            default -> CrimxAPI.getInstance().setServerType(ServerType.GAME_SERVER);
        }
    }

    @Override
    public void onDisable() {
        CrimxAPI.getInstance().getMongoDB().getMongoClient().close();
    }

    public PluginManager getPluginManager() {
        return this.pluginManager;
    }

    public NPCPool getNpcPool() {
        return this.npcPool;
    }

    public HashMap<UUID, ActiveCosmetics> getActiveCosmeticsHashMap() {
        return this.activeCosmeticsHashMap;
    }
}
