package de.fel1x.bingo;

import com.mongodb.client.model.Sorts;
import de.dytanic.cloudnet.ext.bridge.BridgeHelper;
import de.dytanic.cloudnet.ext.bridge.bukkit.BukkitCloudNetHelper;
import de.fel1x.bingo.commands.*;
import de.fel1x.bingo.gamehandler.GamestateHandler;
import de.fel1x.bingo.generation.ItemGenerator;
import de.fel1x.bingo.generation.Items;
import de.fel1x.bingo.listener.block.BlockBreakListener;
import de.fel1x.bingo.listener.block.BlockPlaceListener;
import de.fel1x.bingo.listener.block.BlockTransformListener;
import de.fel1x.bingo.listener.entity.DamageListener;
import de.fel1x.bingo.listener.entity.EntityTargetListener;
import de.fel1x.bingo.listener.player.*;
import de.fel1x.bingo.scenarios.IBingoScenario;
import de.fel1x.bingo.tasks.IBingoTask;
import de.fel1x.bingo.tasks.IdleTask;
import de.fel1x.bingo.utils.scoreboard.GameScoreboard;
import de.fel1x.bingo.utils.scoreboard.LobbyScoreboard;
import de.fel1x.bingo.utils.world.StatsArmorstand;
import de.fel1x.bingo.utils.world.WorldGenerator;
import de.fel1x.teamcrimx.crimxapi.CrimxAPI;
import fr.minuskube.inv.InventoryManager;
import org.apache.commons.lang.WordUtils;
import org.bson.Document;
import org.bukkit.*;
import org.bukkit.entity.Entity;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;
import org.reflections.Reflections;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

public final class Bingo extends JavaPlugin {

    public static Bingo instance;
    private final String prefix = "§aBingo §8● §r";
    ArrayList<Entity> fallingGlassBlocks, fallingAnvils;
    private CrimxAPI crimxAPI;
    private String formattedBiomeName;

    private PluginManager pluginManager;
    private Data data;
    private Items items;
    private GamestateHandler gamestateHandler;
    private ItemGenerator itemGenerator;
    private InventoryManager inventoryManager;
    private WorldGenerator worldGenerator;

    private LobbyScoreboard lobbyScoreboard;
    private GameScoreboard gameScoreboard;

    private IBingoTask bingoTask;
    private Location spawnLocation;

    public static Bingo getInstance() {
        return instance;
    }

    @Override
    public void onLoad() {

        instance = this;

    }

    @Override
    public void onEnable() {

        Bukkit.getConsoleSender().sendMessage(this.prefix + "§6Trying to load Bingo");

        this.crimxAPI = new CrimxAPI();

        this.setupWorlds();

        this.fallingGlassBlocks = new ArrayList<>();
        this.fallingAnvils = new ArrayList<>();

        this.pluginManager = Bukkit.getPluginManager();

        this.data = new Data();
        this.items = new Items();
        this.gamestateHandler = new GamestateHandler();
        this.itemGenerator = new ItemGenerator();

        this.lobbyScoreboard = new LobbyScoreboard();

        this.inventoryManager = new InventoryManager(this);
        this.inventoryManager.init();

        this.registerListener();
        this.registerCommands();

        this.worldGenerator = new WorldGenerator();

        new StatsArmorstand(this);

        this.bingoTask = new IdleTask();
        this.bingoTask.start();

        Bukkit.getConsoleSender().sendMessage(this.prefix + "§aThe plugin was successfully enabled!");

        this.getServer().getScheduler().runTaskLater(this, this::setMotdAndUpdate, 10);

    }

    @Override
    public void onDisable() {

        Bukkit.getConsoleSender().sendMessage(this.prefix + "§cThe plugin was successfully disabled!");

    }

    private void registerCommands() {

        new ItemsCommand(this);
        new BackpackCommand(this);
        new SkipItemCommand(this);
        new StatsCommand(this);
        new StartCommand(this);

    }

    private void setMotdAndUpdate() {

        String unformatted = new Location(Bukkit.getWorlds().get(0), 0.5, 125, 0.5).getChunk()
                .getChunkSnapshot(false, true, false)
                .getBiome(0, 125, 0).name();
        String unformattedIgnoredLength = unformatted.replace("_", " ");
        String formattedIgnoredLength = WordUtils.capitalizeFully(unformattedIgnoredLength);

        String formatted;

        if (formattedIgnoredLength.length() > 10) {
            formatted = formattedIgnoredLength.substring(0, 8) + "...";
        } else {
            formatted = formattedIgnoredLength;
        }

        this.formattedBiomeName = formatted;

        BukkitCloudNetHelper.setApiMotd(formatted + " 6x2");
        BridgeHelper.updateServiceInfo();

    }

    /*
    This is a bit hard-coded because you don't need such a big setup like in mlgwars
     but it's working xD
     */
    private void setupWorlds() {
        World waitingLobby = Bukkit.createWorld(new WorldCreator("Wartelobby"));
        this.spawnLocation = new Location(waitingLobby, -23.5, 5, 51.5, -149, -1.1f);

        if (waitingLobby == null) {
            return;
        }

        waitingLobby.setGameRule(GameRule.DO_DAYLIGHT_CYCLE, false);
        waitingLobby.setGameRule(GameRule.DO_WEATHER_CYCLE, false);
        waitingLobby.setGameRule(GameRule.DO_MOB_SPAWNING, false);
        waitingLobby.setGameRule(GameRule.DO_MOB_LOOT, false);

        waitingLobby.getEntities().forEach(Entity::remove);

    }

    private void registerListener() {

        // PLAYER
        new JoinListener(this);
        new QuitListener(this);
        new PickupListener(this);
        new InteractListener(this);
        new BingoItemListener(this);
        new InventoryClickListener(this);
        new FoodListener(this);
        new DeathListener(this);
        new RespawnListener(this);
        new ChatListener(this);
        new BucketListener(this);
        new ArmorstandInteractListener(this);

        // BLOCK
        new BlockTransformListener(this);
        new BlockBreakListener(this);
        new BlockPlaceListener(this);

        // ENTITY
        new DamageListener(this);
        new EntityTargetListener(this);

    }

    public void startTimerByClass(Class<?> clazz) {

        this.getBingoTask().stop();

        try {
            if (!(clazz.newInstance() instanceof IBingoTask)) {
                return;
            }

            this.setBingoTask((IBingoTask) clazz.newInstance());
            this.getBingoTask().start();

        } catch (InstantiationException | IllegalAccessException e) {
            e.printStackTrace();
        }

    }

    public ArrayList<Class<? extends IBingoScenario>> startRandomScenario() {

        Reflections reflections = new Reflections("de.fel1x.bingo.scenarios");
        return new ArrayList<>(reflections.getSubTypesOf(IBingoScenario.class));

    }

    public List<Document> getTop(int limit) {
        return StreamSupport.stream(this.crimxAPI.getMongoDB().getBingoCollection().find()
                .sort(Sorts.descending("gamesWon")).limit(limit).spliterator(), false)
                .collect(Collectors.toList());
    }

    public Location getSpawnLocation() {
        return this.spawnLocation;
    }

    public GameScoreboard getGameScoreboard() {
        return this.gameScoreboard;
    }

    public void setGameScoreboard(GameScoreboard gameScoreboard) {
        this.gameScoreboard = gameScoreboard;
    }

    public String getFormattedBiomeName() {
        return this.formattedBiomeName;
    }

    public String getPrefix() {
        return this.prefix;
    }

    public CrimxAPI getCrimxAPI() {
        return this.crimxAPI;
    }

    public Data getData() {
        return this.data;
    }

    public Items getItems() {
        return this.items;
    }

    public GamestateHandler getGamestateHandler() {
        return this.gamestateHandler;
    }

    public ItemGenerator getItemGenerator() {
        return this.itemGenerator;
    }

    public PluginManager getPluginManager() {
        return this.pluginManager;
    }

    public InventoryManager getInventoryManager() {
        return this.inventoryManager;
    }

    public IBingoTask getBingoTask() {
        return this.bingoTask;
    }

    public void setBingoTask(IBingoTask bingoTask) {
        this.bingoTask = bingoTask;
    }

    public WorldGenerator getWorldGenerator() {
        return this.worldGenerator;
    }

    public ArrayList<Entity> getFallingGlassBlocks() {
        return this.fallingGlassBlocks;
    }

    public LobbyScoreboard getLobbyScoreboard() {
        return this.lobbyScoreboard;
    }

    public ArrayList<Entity> getFallingAnvils() {
        return this.fallingAnvils;
    }
}
