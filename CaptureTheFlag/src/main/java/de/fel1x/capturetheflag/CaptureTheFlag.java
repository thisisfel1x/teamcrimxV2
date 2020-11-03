package de.fel1x.capturetheflag;

import de.fel1x.capturetheflag.commands.SetupCommand;
import de.fel1x.capturetheflag.commands.StartCommand;
import de.fel1x.capturetheflag.flag.FlagHandler;
import de.fel1x.capturetheflag.gamestate.GamestateHandler;
import de.fel1x.capturetheflag.kits.KitHandler;
import de.fel1x.capturetheflag.listener.block.BlockBreakListener;
import de.fel1x.capturetheflag.listener.block.BlockPlaceListener;
import de.fel1x.capturetheflag.listener.block.InteractListener;
import de.fel1x.capturetheflag.listener.entity.DamageListener;
import de.fel1x.capturetheflag.listener.player.*;
import de.fel1x.capturetheflag.scoreboard.ScoreboardHandler;
import de.fel1x.capturetheflag.timers.*;
import de.fel1x.capturetheflag.world.WorldLoader;
import fr.minuskube.inv.InventoryManager;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.entity.Entity;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

public final class CaptureTheFlag extends JavaPlugin {

    public static CaptureTheFlag instance;

    private final String prefix = "§9CaptureTheFlag §8● §r";
    private final PluginManager pluginManager = Bukkit.getPluginManager();

    private ITimer iTimer;

    private Data data;
    private GamestateHandler gamestateHandler;
    private ScoreboardHandler scoreboardHandler;
    private KitHandler kitHandler;

    private InventoryManager inventoryManager;

    public static CaptureTheFlag getInstance() {
        return instance;
    }

    @Override
    public void onEnable() {

        instance = this;

        new WorldLoader();

        data = new Data();
        inventoryManager = new InventoryManager(this);
        inventoryManager.init();

        gamestateHandler = new GamestateHandler();
        kitHandler = new KitHandler();

        new FlagHandler();
        scoreboardHandler = new ScoreboardHandler();

        this.registerListener();
        this.registerCommands();

        for (World world : Bukkit.getWorlds()) {
            world.getEntities().forEach(Entity::remove);
        }

        this.iTimer = new IdleTimer();
        this.iTimer.start();

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

    }

    private void registerCommands() {

        this.getCommand("setup").setExecutor(new SetupCommand());
        new StartCommand(this);

    }

    public KitHandler getKitHandler() {
        return kitHandler;
    }

    public ITimer getiTimer() {
        return iTimer;
    }

    public void setiTimer(ITimer iTimer) {
        this.iTimer = iTimer;
    }

    public PluginManager getPluginManager() {
        return pluginManager;
    }

    public String getPrefix() {
        return prefix;
    }

    public GamestateHandler getGamestateHandler() {
        return gamestateHandler;
    }

    public Data getData() {
        return data;
    }

    public InventoryManager getInventoryManager() {
        return inventoryManager;
    }

    public ScoreboardHandler getScoreboardHandler() {
        return scoreboardHandler;
    }
}