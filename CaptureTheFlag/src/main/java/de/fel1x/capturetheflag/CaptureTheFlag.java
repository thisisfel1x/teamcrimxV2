package de.fel1x.capturetheflag;

import com.mongodb.client.model.Sorts;
import de.fel1x.capturetheflag.commands.SetupCommand;
import de.fel1x.capturetheflag.commands.StartCommand;
import de.fel1x.capturetheflag.commands.StatsCommand;
import de.fel1x.capturetheflag.flag.FlagHandler;
import de.fel1x.capturetheflag.gamestate.GamestateHandler;
import de.fel1x.capturetheflag.listener.block.BlockBreakListener;
import de.fel1x.capturetheflag.listener.block.BlockPlaceListener;
import de.fel1x.capturetheflag.listener.block.InteractListener;
import de.fel1x.capturetheflag.listener.entity.ArmorStandInteractListener;
import de.fel1x.capturetheflag.listener.entity.DamageListener;
import de.fel1x.capturetheflag.listener.player.*;
import de.fel1x.capturetheflag.scoreboard.ScoreboardHandler;
import de.fel1x.capturetheflag.timers.ITimer;
import de.fel1x.capturetheflag.timers.IdleTimer;
import de.fel1x.capturetheflag.world.WorldLoader;
import de.fel1x.teamcrimx.crimxapi.CrimxAPI;
import fr.minuskube.inv.InventoryManager;
import org.bson.Document;
import org.bukkit.Bukkit;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

public final class CaptureTheFlag extends JavaPlugin {

    public static CaptureTheFlag instance;
    private final PluginManager pluginManager = Bukkit.getPluginManager();
    private CrimxAPI crimxAPI;
    private ITimer iTimer;

    private Data data;
    private GamestateHandler gamestateHandler;
    private ScoreboardHandler scoreboardHandler;

    private InventoryManager inventoryManager;

    public static CaptureTheFlag getInstance() {
        return instance;
    }

    @Override
    public void onEnable() {

        instance = this;

        this.crimxAPI = new CrimxAPI();

        new WorldLoader();

        this.data = new Data();
        this.inventoryManager = new InventoryManager(this);
        this.inventoryManager.init();

        this.gamestateHandler = new GamestateHandler();

        new FlagHandler();
        this.scoreboardHandler = new ScoreboardHandler();

        this.registerListener();
        this.registerCommands();

        this.iTimer = new IdleTimer();
        this.iTimer.start();

    }

    @Override
    public void onDisable() {
    }

    public List<Document> getTop(int limit) {
        return StreamSupport.stream(this.crimxAPI.getMongoDB().getCaptureTheFlagCollection().find()
                .sort(Sorts.descending("gamesWon")).limit(limit).spliterator(), false)
                .collect(Collectors.toList());
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

    private void registerListener() {

        // PLAYER
        new JoinListener(this);
        new QuitListener(this);
        new DeathListener(this);
        new RespawnListener(this);
        new FoodListener(this);
        new PickupListener(this);
        new DropListener(this);
        new InventoryClickListener(this);
        new ChatListener(this);

        // BLOCK
        new InteractListener(this);
        new BlockBreakListener(this);
        new BlockPlaceListener(this);

        // ENTITY
        new DamageListener(this);
        new ArmorStandInteractListener(this);

    }

    private void registerCommands() {

        this.getCommand("setup").setExecutor(new SetupCommand());

        new StartCommand(this);
        new StatsCommand(this);

    }

    public CrimxAPI getCrimxAPI() {
        return this.crimxAPI;
    }

    public ITimer getiTimer() {
        return this.iTimer;
    }

    public void setiTimer(ITimer iTimer) {
        this.iTimer = iTimer;
    }

    public PluginManager getPluginManager() {
        return this.pluginManager;
    }

    public String getPrefix() {
        String prefix = "§9CaptureTheFlag §8● §r";
        return prefix;
    }

    public GamestateHandler getGamestateHandler() {
        return this.gamestateHandler;
    }

    public Data getData() {
        return this.data;
    }

    public InventoryManager getInventoryManager() {
        return this.inventoryManager;
    }

    public ScoreboardHandler getScoreboardHandler() {
        return this.scoreboardHandler;
    }
}
