package de.fel1x.teamcrimx.mlgwars;

import com.mongodb.client.model.Sorts;
import de.dytanic.cloudnet.ext.bridge.BridgeHelper;
import de.dytanic.cloudnet.ext.bridge.bukkit.BukkitCloudNetHelper;
import de.fel1x.teamcrimx.crimxapi.CrimxAPI;
import de.fel1x.teamcrimx.mlgwars.commands.SetupCommand;
import de.fel1x.teamcrimx.mlgwars.commands.StartCommand;
import de.fel1x.teamcrimx.mlgwars.commands.StatsCommand;
import de.fel1x.teamcrimx.mlgwars.enums.Size;
import de.fel1x.teamcrimx.mlgwars.gamestate.GamestateHandler;
import de.fel1x.teamcrimx.mlgwars.listener.block.BlockBreakListener;
import de.fel1x.teamcrimx.mlgwars.listener.block.BlockPlaceListener;
import de.fel1x.teamcrimx.mlgwars.listener.entity.*;
import de.fel1x.teamcrimx.mlgwars.listener.player.*;
import de.fel1x.teamcrimx.mlgwars.listener.world.WeatherChangeListener;
import de.fel1x.teamcrimx.mlgwars.maphandler.MapHandler;
import de.fel1x.teamcrimx.mlgwars.maphandler.WorldLoader;
import de.fel1x.teamcrimx.mlgwars.timer.ITimer;
import de.fel1x.teamcrimx.mlgwars.timer.IdleTimer;
import fr.minuskube.inv.InventoryManager;
import org.apache.commons.io.FilenameUtils;
import org.bson.Document;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

public final class MlgWars extends JavaPlugin {

    private static MlgWars instance;
    private final String prefix = "§eMlgWars §8● §r";
    private CrimxAPI crimxAPI;

    private boolean inSetup;
    private boolean noMap;
    private boolean labor;

    private int lobbyCountdown = 60;
    private int teamSize = -1;

    private Data date;
    private GamestateHandler gamestateHandler;
    private PluginManager pluginManager;
    private InventoryManager inventoryManager;

    private WorldLoader worldLoader;

    private ArrayList<Material> allMaterials;

    private ITimer iTimer;

    public static MlgWars getInstance() {
        return instance;
    }

    @Override
    public void onEnable() {

        instance = this;

        File configFile = new File(this.getDataFolder(), "config.yml");
        if (!configFile.exists()) {
            this.saveDefaultConfig();
        }

        this.labor = this.getConfig().getBoolean("labor");

        this.inSetup = false;
        this.noMap = false;

        String mapName = this.selectMap();

        this.crimxAPI = new CrimxAPI();

        this.date = new Data();
        this.worldLoader = new WorldLoader(mapName);

        this.gamestateHandler = new GamestateHandler();
        this.pluginManager = Bukkit.getPluginManager();

        this.inventoryManager = new InventoryManager(this);
        this.inventoryManager.init();

        this.registerCommands();
        this.registerListener();

        if (!this.isNoMap() && !this.isInSetup()) {
            this.iTimer = new IdleTimer();
            this.iTimer.start();
        }

        this.allMaterials = new ArrayList<>(Arrays.asList(Material.values()));

        this.getServer().getScheduler().runTaskLater(this, () -> this.setMotd(mapName), 60L);

    }

    private String selectMap() {

        File[] files = new File("plugins/MlgWars/maps").listFiles();
        Random rand = new Random();

        if (files.length == 0) {
            Bukkit.getConsoleSender().sendMessage(this.getPrefix() + "§cKeine Map gefunden! Bitte erstellen!");
            this.setNoMap(true);
            return null;
        }

        File file = files[rand.nextInt(files.length)];

        return FilenameUtils.removeExtension(file.getName());

    }

    private void setMotd(String name) {

        MapHandler mapHandler = new MapHandler();

        Size size;

        try {
            size = mapHandler.getSize(name);
        } catch (Exception e) {
            e.printStackTrace();
            return;
        }

        BukkitCloudNetHelper.setApiMotd(name + " " + size.getName());
        BukkitCloudNetHelper.setMaxPlayers(size.getSize());
        BridgeHelper.updateServiceInfo();
    }

    private void registerListener() {

        // PLAYER
        new JoinListener(this);
        new QuitListener(this);
        new MoveListener(this);
        new InteractListener(this);
        new DeathListener(this);
        new FishListener(this);
        new ChatListener(this);
        new PickUpListener(this);
        new DropListener(this);
        new ToggleFlyListener(this);
        new InventoryClickListener(this);
        new RespawnListener(this);
        new PlayerSwapItemListener(this);

        // ENTITY
        new DamageListener(this);
        new EntityTargetListener(this);
        new EntityInteractListener(this);
        new ProjectileHitListener(this);
        new EggThrowEvent(this);
        new EntityExplodeListener(this);
        new ProjectileShootListener(this);

        // BLOCK
        new BlockBreakListener(this);
        new BlockPlaceListener(this);

        // WORLD
        new WeatherChangeListener(this);

    }

    private void registerCommands() {

        new StartCommand(this);
        new SetupCommand(this);
        new StatsCommand(this);

    }

    @Override
    public void onDisable() {
    }

    public void startTimerByClass(Class<?> clazz) {

        this.getiTimer().stop();

        try {
            if (!(clazz.newInstance() instanceof ITimer)) {
                return;
            }

            this.setiTimer((ITimer) clazz.newInstance());
            this.getiTimer().start();

        } catch (InstantiationException | IllegalAccessException e) {
            e.printStackTrace();
        }

    }

    public List<Document> getTop(int limit) {
        return StreamSupport.stream(this.crimxAPI.getMongoDB().getMlgWarsCollection().find()
                .sort(Sorts.descending("gamesWon")).limit(limit).spliterator(), false)
                .collect(Collectors.toList());
    }

    public String getPrefix() {
        return prefix;
    }

    public Data getData() {
        return date;
    }

    public PluginManager getPluginManager() {
        return pluginManager;
    }

    public GamestateHandler getGamestateHandler() {
        return gamestateHandler;
    }

    public ITimer getiTimer() {
        return iTimer;
    }

    public void setiTimer(ITimer iTimer) {
        this.iTimer = iTimer;
    }

    public boolean isInSetup() {
        return inSetup;
    }

    public void setInSetup(boolean inSetup) {
        this.inSetup = inSetup;
    }

    public boolean isNoMap() {
        return noMap;
    }

    public void setNoMap(boolean noMap) {
        this.noMap = noMap;
    }

    public InventoryManager getInventoryManager() {
        return inventoryManager;
    }

    public CrimxAPI getCrimxAPI() {
        return crimxAPI;
    }

    public WorldLoader getWorldLoader() {
        return worldLoader;
    }

    public int getLobbyCountdown() {
        return lobbyCountdown;
    }

    public void setLobbyCountdown(int lobbyCountdown) {
        this.lobbyCountdown = lobbyCountdown;
    }

    public int getTeamSize() {
        return teamSize;
    }

    public void setTeamSize(int teamSize) {
        this.teamSize = teamSize;
    }

    public ArrayList<Material> getAllMaterials() {
        return allMaterials;
    }

    public boolean isLabor() {
        return labor;
    }
}
