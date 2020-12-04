package de.fel1x.teamcrimx.floorislava;

import com.mongodb.client.model.Sorts;
import de.dytanic.cloudnet.ext.bridge.BridgeHelper;
import de.dytanic.cloudnet.ext.bridge.bukkit.BukkitCloudNetHelper;
import de.fel1x.teamcrimx.crimxapi.CrimxAPI;
import de.fel1x.teamcrimx.crimxapi.utils.Cuboid;
import de.fel1x.teamcrimx.floorislava.commands.StartCommand;
import de.fel1x.teamcrimx.floorislava.commands.StatsCommand;
import de.fel1x.teamcrimx.floorislava.commands.TestCommand;
import de.fel1x.teamcrimx.floorislava.gamehandler.GamestateHandler;
import de.fel1x.teamcrimx.floorislava.listener.block.BlockBreakListener;
import de.fel1x.teamcrimx.floorislava.listener.block.BlockPlaceListener;
import de.fel1x.teamcrimx.floorislava.listener.block.BlockTransformListener;
import de.fel1x.teamcrimx.floorislava.listener.entity.DamageListener;
import de.fel1x.teamcrimx.floorislava.listener.entity.EntityTargetListener;
import de.fel1x.teamcrimx.floorislava.listener.player.*;
import de.fel1x.teamcrimx.floorislava.tasks.IFloorIsLavaTask;
import de.fel1x.teamcrimx.floorislava.tasks.IdleTask;
import de.fel1x.teamcrimx.floorislava.utils.ArmorstandStatsLoader;
import fr.minuskube.inv.InventoryManager;
import org.apache.commons.lang.WordUtils;
import org.bson.Document;
import org.bukkit.*;
import org.bukkit.entity.Entity;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

public final class FloorIsLava extends JavaPlugin {

    public static FloorIsLava instance;
    private PluginManager pluginManager;
    private ArrayList<Entity> fallingAnvils;
    private CrimxAPI crimxAPI;
    private boolean pvpEnabled = false;

    public static final String ATTENTION = "⚠";
    public static final String SAVE = "✔";
    public static final String DOT = "●";

    private Location spawnLocation;
    private Cuboid lootDropCuboid;
    private InventoryManager inventoryManager;
    private IFloorIsLavaTask floorIsLavaTask;

    private Location worldSpawnLocation;
    private GamestateHandler gamestateHandler;

    private Data data;

    public static FloorIsLava getInstance() {
        return instance;
    }

    public void onEnable() {
        instance = this;
        this.data = new Data();
        this.crimxAPI = new CrimxAPI();
        this.fallingAnvils = new ArrayList<>();
        this.pluginManager = Bukkit.getPluginManager();
        this.inventoryManager = new InventoryManager(this);
        this.inventoryManager.init();
        this.registerCommands();
        this.registerListener();
        this.worldSpawnLocation = Bukkit.getWorlds().get(0).getSpawnLocation();
        this.setupWorlds();
        this.initCuboid();
        new ArmorstandStatsLoader(this);
        this.gamestateHandler = new GamestateHandler();
        this.floorIsLavaTask = new IdleTask();
        this.floorIsLavaTask.start();
        Bukkit.getScheduler().runTaskLater(this, this::setMotdAndUpdate, 2L);
    }

    private void initCuboid() {
        int size = 50;
        this.lootDropCuboid = new Cuboid(this.worldSpawnLocation.clone().subtract(size / 2.0D, 0, size / 2.0D),
        this.worldSpawnLocation.clone().add(size / 2.0D, 0, size / 2.0D));
    }

    public void onDisable() {
    }

    private void registerCommands() {
        new StartCommand(this);
        new StatsCommand(this);
        new TestCommand(this);
    }

    private void registerListener() {
        new JoinListener(this);
        new QuitListener(this);
        new PickupListener(this);
        new InteractListener(this);
        new InventoryClickListener(this);
        new FoodListener(this);
        new DeathListener(this);
        new ChatListener(this);
        new MoveListener(this);
        new ArmorstandInteractListener(this);
        new BlockTransformListener(this);
        new BlockBreakListener(this);
        new BlockPlaceListener(this);
        new DamageListener(this);
        new EntityTargetListener(this);
    }

    private void setupWorlds() {
        World waitingLobby = Bukkit.createWorld(new WorldCreator("Wartelobby"));
        this.spawnLocation = new Location(waitingLobby, -23.5D, 5.0D, 51.5D, -149.0F, -1.1F);
        if (waitingLobby == null)
            return;
        waitingLobby.setGameRule(GameRule.DO_DAYLIGHT_CYCLE, Boolean.FALSE);
        waitingLobby.setGameRule(GameRule.DO_WEATHER_CYCLE, Boolean.FALSE);
        waitingLobby.setGameRule(GameRule.DO_MOB_SPAWNING, Boolean.FALSE);
        waitingLobby.setGameRule(GameRule.DO_MOB_LOOT, Boolean.FALSE);
        waitingLobby.getEntities().forEach(Entity::remove);
        WorldBorder worldBorder = this.worldSpawnLocation.getWorld().getWorldBorder();
        worldBorder.setCenter(this.worldSpawnLocation);
        worldBorder.setSize(50.0D);
        worldBorder.setDamageAmount(1.0D);
        this.worldSpawnLocation.getWorld().setDifficulty(Difficulty.NORMAL);
    }

    private void setMotdAndUpdate() {
        String formatted, unformatted = this.worldSpawnLocation.getChunk().getChunkSnapshot(false, true, false).getBiome(0, 125, 0).name();
        String unformattedIgnoredLength = unformatted.replace("_", " ");
        String formattedIgnoredLength = WordUtils.capitalizeFully(unformattedIgnoredLength);
        if (formattedIgnoredLength.length() > 10) {
            formatted = formattedIgnoredLength.substring(0, 8) + "...";
        } else {
            formatted = formattedIgnoredLength;
        }
        BukkitCloudNetHelper.setApiMotd(formatted + " 12x1");
        BridgeHelper.updateServiceInfo();
    }

    public void startTimerByClass(Class<?> clazz) {
        getFloorIsLavaTask().stop();
        try {
            if (!(clazz.newInstance() instanceof IFloorIsLavaTask))
                return;
            setFloorIsLavaTask((IFloorIsLavaTask) clazz.newInstance());
            getFloorIsLavaTask().start();
        } catch (InstantiationException | IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    public List<Document> getTop(int limit) {
        return StreamSupport.stream(this.crimxAPI.getMongoDB().getFloorIsLavaCollection().find()
                .sort(Sorts.descending("gamesWon")).limit(limit).spliterator(), false)
                .collect(Collectors.toList());
    }

    public Cuboid getLootDropCuboid() {
        return this.lootDropCuboid;
    }

    public boolean isPvpEnabled() {
        return this.pvpEnabled;
    }

    public void setPvpEnabled(boolean pvpEnabled) {
        this.pvpEnabled = pvpEnabled;
    }

    public ArrayList<Entity> getFallingAnvils() {
        return this.fallingAnvils;
    }

    public Location getWorldSpawnLocation() {
        return this.worldSpawnLocation;
    }

    /**
    Returns the waiting hub spawn location
     */
    public Location getSpawnLocation() {
        return this.spawnLocation;
    }

    public InventoryManager getInventoryManager() {
        return this.inventoryManager;
    }

    public CrimxAPI getCrimxAPI() {
        return this.crimxAPI;
    }

    public PluginManager getPluginManager() {
        return this.pluginManager;
    }

    public IFloorIsLavaTask getFloorIsLavaTask() {
        return this.floorIsLavaTask;
    }

    public void setFloorIsLavaTask(IFloorIsLavaTask floorIsLavaTask) {
        this.floorIsLavaTask = floorIsLavaTask;
    }

    public String getPrefix() {
        return "§6TheFloorIsLava §8● §r";
    }

    public Data getData() {
        return this.data;
    }

    public GamestateHandler getGamestateHandler() {
        return this.gamestateHandler;
    }
}
