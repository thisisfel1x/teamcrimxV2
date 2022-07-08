package de.fel1x.teamcrimx.mlgwars;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.ProtocolManager;
import com.comphenix.protocol.events.PacketAdapter;
import com.comphenix.protocol.events.PacketContainer;
import com.comphenix.protocol.events.PacketEvent;
import com.comphenix.protocol.wrappers.*;
import com.google.common.collect.Lists;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.Sorts;
import com.sk89q.worldedit.extent.clipboard.Clipboard;
import com.sk89q.worldedit.extent.clipboard.io.ClipboardFormat;
import com.sk89q.worldedit.extent.clipboard.io.ClipboardFormats;
import com.sk89q.worldedit.extent.clipboard.io.ClipboardReader;
import de.dytanic.cloudnet.driver.service.ServiceInfoSnapshot;
import de.dytanic.cloudnet.ext.bridge.BridgeHelper;
import de.dytanic.cloudnet.ext.bridge.bukkit.BukkitCloudNetHelper;
import de.dytanic.cloudnet.wrapper.Wrapper;
import de.fel1x.teamcrimx.crimxapi.CrimxAPI;
import de.fel1x.teamcrimx.mlgwars.commands.*;
import de.fel1x.teamcrimx.mlgwars.enums.Size;
import de.fel1x.teamcrimx.mlgwars.gamestate.Gamestate;
import de.fel1x.teamcrimx.mlgwars.gamestate.GamestateHandler;
import de.fel1x.teamcrimx.mlgwars.inventories.rework.GameTypeVoteInventory;
import de.fel1x.teamcrimx.mlgwars.inventories.rework.TeamReworkInventory;
import de.fel1x.teamcrimx.mlgwars.kit.rework.InventoryKitManager;
import de.fel1x.teamcrimx.mlgwars.listener.block.BlockBreakListener;
import de.fel1x.teamcrimx.mlgwars.listener.block.BlockPlaceListener;
import de.fel1x.teamcrimx.mlgwars.listener.entity.*;
import de.fel1x.teamcrimx.mlgwars.listener.player.*;
import de.fel1x.teamcrimx.mlgwars.listener.world.WeatherChangeListener;
import de.fel1x.teamcrimx.mlgwars.maphandler.MapHandler;
import de.fel1x.teamcrimx.mlgwars.maphandler.WorldLoader;
import de.fel1x.teamcrimx.mlgwars.maphandler.gametype.GameType;
import de.fel1x.teamcrimx.mlgwars.maphandler.gametype.types.SoloGameType;
import de.fel1x.teamcrimx.mlgwars.maphandler.gametype.types.TeamGameType;
import de.fel1x.teamcrimx.mlgwars.objects.GamePlayer;
import de.fel1x.teamcrimx.mlgwars.objects.Map;
import de.fel1x.teamcrimx.mlgwars.scoreboard.ScoreboardHandler;
import de.fel1x.teamcrimx.mlgwars.timer.ITimer;
import de.fel1x.teamcrimx.mlgwars.timer.IdleTimer;
import fr.minuskube.inv.InventoryManager;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextDecoration;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.apache.commons.io.FilenameUtils;
import org.bson.Document;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.FileInputStream;
import java.lang.reflect.InvocationTargetException;
import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

public final class MlgWars extends JavaPlugin {

    private static MlgWars instance;
    private CrimxAPI crimxAPI;

    private ProtocolManager protocolManager;
    private MiniMessage miniMessage;
    private MiniMessage miniMessage0;

    private List<Map> availableMaps;

    private boolean inSetup;
    private boolean noMap;

    private int lobbyCountdown = 60;

    private Map selectedMap;
    private int teamSize = -1;

    private GameType gameType;

    private Data date;
    private GamestateHandler gamestateHandler;
    private ScoreboardHandler scoreboardHandler;
    private PluginManager pluginManager;

    private InventoryManager inventoryManager;
    private TeamReworkInventory teamReworkInventory;
    private GameTypeVoteInventory gameTypeVoteInventory;

    private InventoryKitManager inventoryKitManager;

    private ArrayList<Material> allMaterials;
    private ArrayList<Clipboard> allPossibleTrapperCages;

    private ITimer iTimer;

    private GameTypeVoteInventory.ImplementedMode forcedMode;

    public static MlgWars getInstance() {
        return instance;
    }

    @Override
    public void onEnable() {
        instance = this;

        this.protocolManager = ProtocolLibrary.getProtocolManager();
        this.miniMessage = MiniMessage.builder().build();
        this.miniMessage0 = MiniMessage.builder().build();

        File configFile = new File(this.getDataFolder(), "config.yml");
        if (!configFile.exists()) {
            this.saveDefaultConfig();
        }

        this.inSetup = false;
        this.noMap = false;

        this.availableMaps = this.cacheMaps();

        GameType gameType = this.selectMap();

        this.crimxAPI = new CrimxAPI();

        this.date = new Data();

        // Load just lobby
        new WorldLoader(this).loadLobby();
        // New GameType Impl
        if(gameType != null) this.gameType = gameType;
        this.gameType.loadMap(this.selectedMap.getMapName());

        this.inventoryKitManager = new InventoryKitManager();

        this.gamestateHandler = new GamestateHandler();
        this.scoreboardHandler = new ScoreboardHandler(this);
        this.pluginManager = Bukkit.getPluginManager();

        this.inventoryManager = new InventoryManager(this);
        this.inventoryManager.init();
        this.teamReworkInventory = new TeamReworkInventory(this);
        this.gameTypeVoteInventory = new GameTypeVoteInventory(this);

        this.registerCommands();
        this.registerListener();

        if (!this.isNoMap() && !this.isInSetup()) {
            this.iTimer = new IdleTimer();
            this.iTimer.start();
        }

        this.initializeArrayLists();
        this.tablistPacketListener();

        this.getServer().getScheduler().runTaskLater(this, this::setMotd, 10L);
    }

    private void initializeArrayLists() {
        this.allMaterials = new ArrayList<>(Arrays.asList(Material.values()));

        this.allPossibleTrapperCages = new ArrayList<>();
        File[] files = new File("plugins/MlgWars/schematics/trapper").listFiles();
        
        if(files == null || files.length == 0) {
            Bukkit.getConsoleSender().sendMessage(this.getPrefix() + "§cKein Schematic für 'Trapper' gefunden"); // TODO: Kit deaktivieren ?
            return;
        }

        for (File file : files) {
            ClipboardFormat format = ClipboardFormats.findByFile(file);
            if(format == null) {
                continue;
            }

            try (ClipboardReader reader = format.getReader(new FileInputStream(file))) {
                Clipboard clipboard = reader.read();
                if(clipboard != null) {
                    this.allPossibleTrapperCages.add(clipboard);
                }
            } catch (Exception ignored) {}
        }
    }

    private ArrayList<Map> cacheMaps() {
        MapHandler mapHandler = new MapHandler();

        ArrayList<Map> toReturn = new ArrayList<>();
        File[] files = new File("plugins/MlgWars/maps").listFiles();

        if (files == null || files.length == 0) {
            Bukkit.getConsoleSender().sendMessage(this.getPrefix() + "§cKeine Map gefunden! Bitte erstellen!");
            this.setNoMap(true);
            return toReturn;
        }

        for (File file : files) {
            String mapName = FilenameUtils.removeExtension(file.getName());
            String builder = mapHandler.getBuilder(mapName);
            Size size;

            try {
                size = new MapHandler().getSize(mapName);
            } catch (Exception ignored) {
                this.setNoMap(true);
                return toReturn;
            }
            toReturn.add(new Map(mapName, builder, size));
        }
        return toReturn;
    }

    private GameType selectMap() {
        Random random = new Random();

        if (this.availableMaps == null || this.availableMaps.size() == 0) {
            Bukkit.getConsoleSender().sendMessage(this.getPrefix() + "§cKeine Map gefunden! Bitte erstellen!");
            this.setNoMap(true);
            return null;
        }

        try {
            if(Wrapper.getInstance().getServiceConfiguration().getProperties().get("team", Boolean.class)) {
                this.availableMaps = this.availableMaps.stream().filter(map -> map.getSize().getTeamSize() > 1)
                        .collect(Collectors.toList());
            } else {
                this.availableMaps = this.availableMaps.stream().filter(map -> map.getSize().getTeamSize() == 1)
                        .collect(Collectors.toList());
            }
        } catch (NullPointerException ignored) {
            Bukkit.getConsoleSender().sendMessage(this.getPrefix() + "No size forced");
        }

        Map selectedMap = this.availableMaps.get(random.nextInt(this.availableMaps.size()));
        this.setSelectedMap(selectedMap);

        Size size = selectedMap.getSize();

        Bukkit.getConsoleSender().sendMessage(this.getPrefix() + "§aAusgewählte Map: "
                + this.selectedMap.getMapName() + " von "  // this -> check if map is cached properly
                + selectedMap.getMapBuilder());

        this.setTeamSize(size.getTeamSize());

        ServiceInfoSnapshot serviceInfoSnapshot = Wrapper.getInstance().getCurrentServiceInfoSnapshot();
        serviceInfoSnapshot.getProperties().append("teamSize", size.getTeamSize());
        Wrapper.getInstance().publishServiceInfoUpdate(serviceInfoSnapshot);

        if(size.getTeamSize() == 1) {
            return new SoloGameType(this);
        }

        return new TeamGameType(this);
    }

    public void setMotd() {
        BukkitCloudNetHelper.setMotd(this.selectedMap.getMapName() + " " + this.selectedMap.getSize().getName());
        BukkitCloudNetHelper.setMaxPlayers(this.selectedMap.getSize().getSize());
        BukkitCloudNetHelper.setExtra("lobby");
        BukkitCloudNetHelper.setState("LOBBY");
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
        new ToggleFlightListener(this);
        new AreaEffectCloudListener(this);

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
        new ForceModeCommand(this);
        new ItemMultiplierCommand(this);
    }

    public void startTimerByClass(Class<?> clazz) {
        this.getiTimer().stop();

        try {
            if (!(clazz.getDeclaredConstructor().newInstance() instanceof ITimer)) {
                return;
            }
            this.setiTimer((ITimer) clazz.getDeclaredConstructor().newInstance());
            this.getiTimer().start();
        } catch (InstantiationException | IllegalAccessException | NoSuchMethodException | InvocationTargetException e) {
            e.printStackTrace();
        }
    }

    public List<Document> getTop(int limit) {
        return this.getTop(limit, false);
    }

    public List<Document> getTop(int limit, boolean tournament) {
        MongoCollection<Document> collection = tournament ? this.crimxAPI.getMongoDB().getMlgWarsTournamentCollection()
                : this.crimxAPI.getMongoDB().getMlgWarsCollection();
        return StreamSupport.stream(collection.find()
                        .sort(Sorts.descending(tournament ? "points" : "gamesWon")).limit(limit).spliterator(), false)
                .collect(Collectors.toList());
    }

    public CompletableFuture<Integer> getRankingPosition(UUID uuid) {
        return CompletableFuture.supplyAsync(() -> {
            List<Document> documents = this.crimxAPI.getMongoDB().getMlgWarsCollection().find()
                    .sort(Sorts.descending("gamesWon")).into(Lists.newArrayList());

            for (Document document : documents) {
                if (document.getString("_id").equalsIgnoreCase(uuid.toString())) {
                    return documents.indexOf(document) + 1;
                }
            }

            return -1;
        });
    }

    public void tablistPacketListener() {
        this.getProtocolManager().addPacketListener(new PacketAdapter(this, PacketType.Play.Server.PLAYER_INFO) {
            @Override
            public void onPacketSending(PacketEvent event) {
                if(event.getPacket().getPlayerInfoAction().read(0) != EnumWrappers.PlayerInfoAction.UPDATE_DISPLAY_NAME) {
                    event.setPacket(event.getPacket());
                    return;
                }

                if(MlgWars.this.gamestateHandler.getGamestate() != Gamestate.DELAY) {
                    event.setPacket(event.getPacket());
                    return;
                }

                Player target = event.getPlayer();
                GamePlayer gamePlayer = MlgWars.this.getData().getGamePlayers().get(target.getUniqueId());

                PacketContainer packetContainer = event.getPacket();

                List<PlayerInfoData> list = packetContainer.getPlayerInfoDataLists().read(0);
                List<PlayerInfoData> newPlayerInfoDataList = Lists.newArrayList();

                for (PlayerInfoData playerInfoData : list) {
                    if (playerInfoData == null || playerInfoData.getProfile() == null) {
                        newPlayerInfoDataList.add(playerInfoData);
                        continue;
                    }

                    GamePlayer loopGamePlayer = MlgWars.this.getData().getGamePlayers().get(playerInfoData.getProfile().getUUID());
                    int teamId = loopGamePlayer.getPlayerMlgWarsTeamId();

                    WrappedGameProfile profile = playerInfoData.getProfile();
                    String prefix = "<white>";

                    if(target.getUniqueId() == profile.getUUID()
                            || gamePlayer.getPlayerMlgWarsTeamId() == loopGamePlayer.getPlayerMlgWarsTeamId()) {
                        prefix = "<#" + MlgWars.this.date.getGameTeams().get(teamId).getColor().getRGB()
                                + ">";
                    }

                    String newDisplayName = prefix + "T" + (teamId + 1) + " | " + playerInfoData.getProfile().getName();

                    PlayerInfoData newPlayerInfoData = new PlayerInfoData(profile, playerInfoData.getLatency(), playerInfoData.getGameMode(),
                            AdventureComponentConverter.fromComponent(MiniMessage.builder().build().deserialize(newDisplayName)));
                    newPlayerInfoDataList.add(newPlayerInfoData);
                }

                packetContainer.getPlayerInfoDataLists().write(0, newPlayerInfoDataList);
                event.setPacket(packetContainer);
            }
        });
    }

    public Component prefix() {
        return Component.text("MlgWars", NamedTextColor.YELLOW).decoration(TextDecoration.ITALIC, false)
                .append(Component.text(" ● ", NamedTextColor.DARK_GRAY));
    }

    public String getPrefix() {
        return "§eMlgWars §8● §r";
    }

    public Data getData() {
        return this.date;
    }

    public PluginManager getPluginManager() {
        return this.pluginManager;
    }

    public GamestateHandler getGamestateHandler() {
        return this.gamestateHandler;
    }

    public ITimer getiTimer() {
        return this.iTimer;
    }

    public void setiTimer(ITimer iTimer) {
        this.iTimer = iTimer;
    }

    public boolean isInSetup() {
        return this.inSetup;
    }

    public void setInSetup(boolean inSetup) {
        this.inSetup = inSetup;
    }

    public boolean isNoMap() {
        return this.noMap;
    }

    public void setNoMap(boolean noMap) {
        this.noMap = noMap;
    }

    public InventoryManager getInventoryManager() {
        return this.inventoryManager;
    }

    public CrimxAPI getCrimxAPI() {
        return this.crimxAPI;
    }

    public int getLobbyCountdown() {
        return this.lobbyCountdown;
    }

    public void setLobbyCountdown(int lobbyCountdown) {
        this.lobbyCountdown = lobbyCountdown;
    }

    public int getTeamSize() {
        return this.teamSize;
    }

    public void setTeamSize(int teamSize) {
        this.teamSize = teamSize;
    }

    public ArrayList<Material> getAllMaterials() {
        return this.allMaterials;
    }

    public InventoryKitManager getInventoryKitManager() {
        return this.inventoryKitManager;
    }

    public GameType getGameType() {
        return this.gameType;
    }

    public void setGameType(GameType gameType) {
        this.gameType = gameType;
    }

    public List<Map> getAvailableMaps() {
        return this.availableMaps;
    }

    public ArrayList<Clipboard> getAllPossibleTrapperCages() {
        return this.allPossibleTrapperCages;
    }

    public ProtocolManager getProtocolManager() {
        return this.protocolManager;
    }

    public MiniMessage miniMessage() {
        return this.miniMessage;
    }

    public TeamReworkInventory getTeamReworkInventory() {
        return this.teamReworkInventory;
    }

    public ScoreboardHandler getScoreboardHandler() {
        return this.scoreboardHandler;
    }

    public Map getSelectedMap() {
        return this.selectedMap;
    }

    public void setSelectedMap(Map selectedMap) {
        this.selectedMap = selectedMap;
    }

    public GameTypeVoteInventory getGameTypeVoteInventory() {
        return this.gameTypeVoteInventory;
    }

    public GameTypeVoteInventory.ImplementedMode getForcedMode() {
        return this.forcedMode;
    }

    public void setForcedMode(GameTypeVoteInventory.ImplementedMode implementedMode) {
        this.forcedMode = implementedMode;
    }
}
