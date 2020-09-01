package de.fel1x.teamcrimx.crimxlobby;

import com.github.juliarn.npc.NPCPool;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import de.fel1x.teamcrimx.crimxapi.CrimxAPI;
import de.fel1x.teamcrimx.crimxapi.utils.Actionbar;
import de.fel1x.teamcrimx.crimxlobby.commands.BuildCommand;
import de.fel1x.teamcrimx.crimxlobby.commands.SetupCommand;
import de.fel1x.teamcrimx.crimxlobby.cosmetics.armor.RainbowArmor;
import de.fel1x.teamcrimx.crimxlobby.listeners.block.BlockBreakListener;
import de.fel1x.teamcrimx.crimxlobby.listeners.block.BlockPlaceListener;
import de.fel1x.teamcrimx.crimxlobby.listeners.entity.DamageListener;
import de.fel1x.teamcrimx.crimxlobby.listeners.entity.ProjectileHitListener;
import de.fel1x.teamcrimx.crimxlobby.listeners.player.*;
import de.fel1x.teamcrimx.crimxlobby.listeners.world.WeatherChangeListener;
import de.fel1x.teamcrimx.crimxlobby.manager.SpawnManager;
import de.fel1x.teamcrimx.crimxlobby.minigames.watermlg.WaterMlgHandler;
import de.fel1x.teamcrimx.crimxlobby.objects.Spawn;
import de.fel1x.teamcrimx.crimxlobby.scoreboard.LobbyScoreboard;
import fr.minuskube.inv.InventoryManager;
import net.jitse.npclib.NPCLib;
import net.labymod.serverapi.bukkit.LabyModPlugin;
import org.bukkit.*;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Player;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.List;
import java.util.UUID;

public final class CrimxLobby extends JavaPlugin {

    private static CrimxLobby instance;
    int actionBarCount;
    private String prefix = "§aCrimx§lLobby §8● §r";
    private int actionbarTimer = 0;
    private Data data;
    private CrimxAPI crimxAPI;
    private PluginManager pluginManager;
    private NPCPool npcPool;
    private NPCLib npcLib;
    private InventoryManager inventoryManager;
    private SpawnManager spawnManager = new SpawnManager();
    private WaterMlgHandler waterMlgHandler;
    private LobbyScoreboard lobbyScoreboard;

    public static CrimxLobby getInstance() {
        return instance;
    }

    @Override
    public void onEnable() {

        Bukkit.getConsoleSender().sendMessage("§aTrying to start CrimxLobby by fel1x");

        instance = this;
        if (!this.getConfig().contains("actionbar")) {
            this.saveDefaultConfig();
        }

        this.crimxAPI = CrimxAPI.getInstance();

        this.pluginManager = Bukkit.getPluginManager();
        this.data = new Data();

        this.npcPool = new NPCPool(this, 125, 25, 40);
        this.npcLib = new NPCLib(this);

        this.inventoryManager = new InventoryManager(this);
        this.inventoryManager.init();

        this.waterMlgHandler = new WaterMlgHandler();

        this.lobbyScoreboard = new LobbyScoreboard();

        this.registerCommands();
        this.registerListener();

        this.loadWorld();

        this.runMainScheduler();

    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }

    private void registerCommands() {

        new BuildCommand(this);
        new SetupCommand(this);

    }

    private void loadWorld() {

        Location lobby = Spawn.SPAWN.getPlayerSpawn();

        if (lobby == null) {
            Bukkit.getConsoleSender().sendMessage("§cKein Spawn gesetzt!");
            return;
        } else {
            Bukkit.createWorld(new WorldCreator(lobby.getWorld().getName()));
        }

        lobby.getWorld().getEntities().forEach(entity -> {
            if (!(entity instanceof ArmorStand)) {
                entity.remove();
            }
        });

        World world = lobby.getWorld();
        world.setSpawnLocation((int) lobby.getX(), (int) lobby.getY(), (int) lobby.getZ());
        world.setDifficulty(Difficulty.PEACEFUL);
        world.setGameRuleValue("doMobSpawning", "false");
        world.setGameRuleValue("doMobLoot", "false");
        world.setGameRuleValue("doWeatherCycle", "false");
        world.setStorm(false);
        world.setThunderDuration(0);
        world.setThundering(false);
        world.setTime(1200);

    }

    private void registerListener() {

        // PLAYER
        new JoinListener(this);
        new QuitListener(this);

        new LabyModPlayerJoinListener(this);

        new InventoryClickListener(this);
        new ChatListener(this);
        new InteractListener(this);
        new FishingHookListener(this);
        new ItemHeldListener(this);
        new MoveListener(this);
        new PickupListener(this);
        new NPCInteractListener(this);
        new WaterBucketEmptyListener(this);

        // BLOCK
        new BlockPlaceListener(this);
        new BlockBreakListener(this);

        // ENTITY
        new DamageListener(this);
        new ProjectileHitListener(this);

        // WORLD
        new WeatherChangeListener(this);

    }

    private void runMainScheduler() {

        actionBarCount = 0;
        List<String> actionBarMessages = this.getConfig().getStringList("actionbar");

        Bukkit.getScheduler().scheduleSyncRepeatingTask(this, () -> this.getData().getHueMap().forEach(((uuid, hue) -> {

            Player player = Bukkit.getPlayer(uuid);
            RainbowArmor rainbowArmor = new RainbowArmor();

            if (player != null && player.isOnline()) {
                hue = rainbowArmor.handleColor(hue, 0.005f);
                rainbowArmor.setArmor(player, hue, 0.02f);
                this.getData().getHueMap().put(uuid, hue);
            }

        })), 0L, 3L);

        Bukkit.getScheduler().runTaskTimer(this, () -> {
            Bukkit.getOnlinePlayers().forEach(player -> Actionbar.sendActiobar(player, actionBarMessages.get(actionBarCount).replace('&', '§')));

            this.actionbarTimer++;
            if (this.actionbarTimer % 10 == 0) {
                if (actionBarCount + 1 < actionBarMessages.size()) {
                    actionBarCount++;
                } else {
                    actionBarCount = 0;
                }
            }
        }, 0L, 20L);

    }

    public void forceEmote(Player receiver, UUID npcUUID, int emoteId) {
        // List of all forced emotes
        JsonArray array = new JsonArray();

        // Emote and target NPC
        JsonObject forcedEmote = new JsonObject();
        forcedEmote.addProperty("uuid", npcUUID.toString());
        forcedEmote.addProperty("emote_id", emoteId);
        array.add(forcedEmote);

        // Send to LabyMod using the API
        LabyModPlugin.getInstance().sendServerMessage(receiver, "emote_api", array);
    }

    public String getPrefix() {
        return prefix;
    }

    public CrimxAPI getCrimxAPI() {
        return crimxAPI;
    }

    public PluginManager getPluginManager() {
        return pluginManager;
    }

    public Data getData() {
        return data;
    }

    public NPCPool getNpcPool() {
        return npcPool;
    }

    public NPCLib getNpcLib() {
        return npcLib;
    }

    public InventoryManager getInventoryManager() {
        return inventoryManager;
    }

    public SpawnManager getSpawnManager() {
        return spawnManager;
    }

    public WaterMlgHandler getWaterMlgHandler() {
        return waterMlgHandler;
    }

    public LobbyScoreboard getLobbyScoreboard() {
        return lobbyScoreboard;
    }
}
