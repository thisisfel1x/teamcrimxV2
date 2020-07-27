package de.fel1x.teamcrimx.mlgwars;

import de.fel1x.teamcrimx.mlgwars.commands.SetupCommand;
import de.fel1x.teamcrimx.mlgwars.commands.StartCommand;
import de.fel1x.teamcrimx.mlgwars.gamestate.GamestateHandler;
import de.fel1x.teamcrimx.mlgwars.listener.block.BlockBreakListener;
import de.fel1x.teamcrimx.mlgwars.listener.block.BlockPlaceListener;
import de.fel1x.teamcrimx.mlgwars.listener.player.InteractListener;
import de.fel1x.teamcrimx.mlgwars.listener.player.JoinListener;
import de.fel1x.teamcrimx.mlgwars.listener.player.MoveListener;
import de.fel1x.teamcrimx.mlgwars.listener.player.QuitListener;
import de.fel1x.teamcrimx.mlgwars.listener.world.WeatherChangeListener;
import de.fel1x.teamcrimx.mlgwars.maphandler.WorldLoader;
import de.fel1x.teamcrimx.mlgwars.timer.ITimer;
import de.fel1x.teamcrimx.mlgwars.timer.IdleTimer;
import org.bukkit.Bukkit;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

public final class MlgWars extends JavaPlugin {

    private static MlgWars instance;
    private final String prefix = "§8| §eMlgWars §8» §r";

    private boolean inSetup;
    private boolean noMap;

    private Data date;
    private GamestateHandler gamestateHandler;
    private PluginManager pluginManager;

    private WorldLoader worldLoader;

    private ITimer iTimer;

    @Override
    public void onEnable() {

        instance = this;

        this.inSetup = false;
        this.noMap = false;

        this.date = new Data();
        this.gamestateHandler = new GamestateHandler();
        this.pluginManager = Bukkit.getPluginManager();

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
        int teamSize = 1;
        return teamSize;
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

    public WorldLoader getWorldLoader() {
        return worldLoader;
    }
}
