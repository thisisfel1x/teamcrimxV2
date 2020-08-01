package de.fel1x.teamcrimx.mlgwars;

import de.fel1x.teamcrimx.crimxapi.CrimxAPI;
import de.fel1x.teamcrimx.mlgwars.commands.SetupCommand;
import de.fel1x.teamcrimx.mlgwars.commands.StartCommand;
import de.fel1x.teamcrimx.mlgwars.gamestate.GamestateHandler;
import de.fel1x.teamcrimx.mlgwars.listener.block.BlockBreakListener;
import de.fel1x.teamcrimx.mlgwars.listener.block.BlockPlaceListener;
import de.fel1x.teamcrimx.mlgwars.listener.entity.DamageListener;
import de.fel1x.teamcrimx.mlgwars.listener.entity.EntityInteractListener;
import de.fel1x.teamcrimx.mlgwars.listener.entity.EntityTargetListener;
import de.fel1x.teamcrimx.mlgwars.listener.player.*;
import de.fel1x.teamcrimx.mlgwars.listener.world.WeatherChangeListener;
import de.fel1x.teamcrimx.mlgwars.maphandler.WorldLoader;
import de.fel1x.teamcrimx.mlgwars.timer.ITimer;
import de.fel1x.teamcrimx.mlgwars.timer.IdleTimer;
import fr.minuskube.inv.InventoryManager;
import org.bukkit.Bukkit;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

public final class MlgWars extends JavaPlugin {

    private static MlgWars instance;
    private final String prefix = "§eMlgWars §8● §r";
    private CrimxAPI crimxAPI;

    private boolean inSetup;
    private boolean noMap;

    private String mapName;

    private Data date;
    private GamestateHandler gamestateHandler;
    private PluginManager pluginManager;
    private InventoryManager inventoryManager;

    private WorldLoader worldLoader;

    private ITimer iTimer;

    @Override
    public void onEnable() {

        instance = this;

        this.inSetup = false;
        this.noMap = false;

        this.crimxAPI = new CrimxAPI();

        this.date = new Data();
        this.gamestateHandler = new GamestateHandler();
        this.pluginManager = Bukkit.getPluginManager();

        this.inventoryManager = new InventoryManager(this);
        this.inventoryManager.init();

        this.worldLoader = new WorldLoader();

        this.registerCommands();
        this.registerListener();

        if(!this.isNoMap() && !this.isInSetup()) {
            this.iTimer = new IdleTimer();
            this.iTimer.start();
        }

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
        new ToggleFlyListener(this);

        // ENTITY
        new DamageListener(this);
        new EntityTargetListener(this);
        new EntityInteractListener(this);

        // BLOCK
        new BlockBreakListener(this);
        new BlockPlaceListener(this);

        // WORLD
        new WeatherChangeListener(this);

    }

    private void registerCommands() {

        new StartCommand(this);
        new SetupCommand(this);

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

    public static MlgWars getInstance() {
        return instance;
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

    public int getTeamSize() {
        return 1;
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

    public String getMapName() {
        return mapName;
    }

    public void setMapName(String mapName) {
        this.mapName = mapName;
    }
}
