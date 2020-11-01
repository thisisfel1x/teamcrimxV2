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
import de.fel1x.capturetheflag.timers.EndingTimer;
import de.fel1x.capturetheflag.timers.InGameTimer;
import de.fel1x.capturetheflag.timers.LobbyTimer;
import de.fel1x.capturetheflag.world.WorldLoader;
import fr.minuskube.inv.InventoryManager;
import org.bukkit.Bukkit;
import org.bukkit.GameRule;
import org.bukkit.World;
import org.bukkit.entity.Entity;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

public final class CaptureTheFlag extends JavaPlugin {

    public static CaptureTheFlag instance;
    private final String prefix = "§9CaptureTheFlag §8● §r";
    private final PluginManager pluginManager = Bukkit.getPluginManager();
    private Data data;
    private GamestateHandler gamestateHandler;
    private ScoreboardHandler scoreboardHandler;
    private KitHandler kitHandler;
    private LobbyTimer lobbyTimer;
    private InGameTimer inGameTimer;
    private EndingTimer endingTimer;
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

        lobbyTimer = new LobbyTimer();
        inGameTimer = new InGameTimer();
        endingTimer = new EndingTimer();

        this.registerListener();
        this.registerCommands();

        for (World world : Bukkit.getWorlds()) {
            world.getEntities().forEach(Entity::remove);

            world.setGameRule(GameRule.DO_MOB_SPAWNING, false);
            world.setGameRule(GameRule.DO_DAYLIGHT_CYCLE, false);
            world.setGameRule(GameRule.DO_WEATHER_CYCLE, false);

        }
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
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
        this.getCommand("start").setExecutor(new StartCommand());

    }

    public KitHandler getKitHandler() {
        return kitHandler;
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

    public LobbyTimer getLobbyTimer() {
        return lobbyTimer;
    }

    public EndingTimer getEndingTimer() {
        return endingTimer;
    }

    public InventoryManager getInventoryManager() {
        return inventoryManager;
    }

    public ScoreboardHandler getScoreboardHandler() {
        return scoreboardHandler;
    }
}
