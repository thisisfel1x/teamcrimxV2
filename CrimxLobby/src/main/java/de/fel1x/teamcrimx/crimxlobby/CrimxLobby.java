package de.fel1x.teamcrimx.crimxlobby;

import com.github.juliarn.npc.NPC;
import de.fel1x.teamcrimx.crimxapi.CrimxAPI;
import de.fel1x.teamcrimx.crimxapi.utils.Actionbar;
import de.fel1x.teamcrimx.crimxapi.utils.npc.NPCCreator;
import de.fel1x.teamcrimx.crimxlobby.commands.BuildCommand;
import de.fel1x.teamcrimx.crimxlobby.commands.SetupCommand;
import de.fel1x.teamcrimx.crimxlobby.inventories.NavigatorInventory;
import de.fel1x.teamcrimx.crimxlobby.inventories.rework.LobbySwitcherInventory;
import de.fel1x.teamcrimx.crimxlobby.listeners.block.BlockBreakListener;
import de.fel1x.teamcrimx.crimxlobby.listeners.block.BlockPlaceListener;
import de.fel1x.teamcrimx.crimxlobby.listeners.entity.DamageListener;
import de.fel1x.teamcrimx.crimxlobby.listeners.player.*;
import de.fel1x.teamcrimx.crimxlobby.listeners.world.WeatherChangeListener;
import de.fel1x.teamcrimx.crimxlobby.manager.SpawnManager;
import de.fel1x.teamcrimx.crimxlobby.minigames.connectfour.ConnectFourGameManager;
import de.fel1x.teamcrimx.crimxlobby.npc.gamesupport.MlgWarsNPC;
import de.fel1x.teamcrimx.crimxlobby.objects.Spawn;
import de.fel1x.teamcrimx.crimxlobby.scoreboard.LobbyScoreboard;
import fr.minuskube.inv.InventoryManager;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextColor;
import net.kyori.adventure.text.format.TextDecoration;
import org.bukkit.*;
import org.bukkit.entity.Entity;
import org.bukkit.entity.WanderingTrader;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.List;

public final class CrimxLobby extends JavaPlugin {

    private static CrimxLobby instance;
    private final SpawnManager spawnManager = new SpawnManager();
    private int actionBarCount;
    private int actionbarTimer = 0;
    private Data data;
    private CrimxAPI crimxAPI;
    private PluginManager pluginManager;
    private InventoryManager inventoryManager;
    private LobbyScoreboard lobbyScoreboard;
    private Entity wanderingTraderNPCEntity;
    private NPC perksNpc;

    private ConnectFourGameManager connectFourGameManager;

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

        Bukkit.createWorld(new WorldCreator(this.spawnManager.getWorldName(Spawn.SPAWN.name())));

        this.crimxAPI = CrimxAPI.getInstance();

        this.pluginManager = Bukkit.getPluginManager();
        this.data = new Data();

        this.inventoryManager = new InventoryManager(this);
        this.inventoryManager.init();

        this.lobbyScoreboard = new LobbyScoreboard();

        this.loadWorld();

        this.registerCommands();
        this.registerListener();
        this.loadInventories();

        this.connectFourGameManager = new ConnectFourGameManager(this);

        this.runMainScheduler();

    }

    private void spawnNpc() {
        this.perksNpc = new NPCCreator(Spawn.SHOP_NPC.getSpawn())
                .shouldImitatePlayer(true)
                .shouldLookAtPlayer(true)
                .addHeaders(new String[]{"§bteamcrimx§lDE §7- §bCoinshop", "§7Heute gute Pleis"})
                .createProfile("eyJ0aW1lc3RhbXAiOjE1ODU3NDMzODM2ODcsInByb2ZpbGVJZCI6IjkxZmUxOTY4N2M5MDQ2NTZhYTFmYzA1OTg2ZGQzZmU3IiwicHJvZmlsZU5hbWUiOiJoaGphYnJpcyIsInNpZ25hdHVyZVJlcXVpcmVkIjp0cnVlLCJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvOWU1YmQ2ZDVjOWQ1NDM0ODc1MTZjYzNmY2FiNDczNWUxZGFlMjEyYzM5MjdhYjllNDBlNmFlM2U1OWI5NThhYSJ9fX0=",
                        "amJcsYzBIKpdlkKnwDNJ9iI0jMh675fmxQkRtKXquQk1xMFUy7wlsOBnNI6qHH5aNWKnKbq+eID6u3XHe3fcbEShCQyMtydyMzdYjyro5LWl9XkRs3LhbQDUllnJmoG6sIeCNHZ+VAqDLeHh0ahhSqHhg9C+4831C71uCoL2ah1+mPh7GA9CAnyp09ZZ3t1eHW9fvwwVMiDONgZjB2LLom6QW+rMHV3eltQojhkulniomKEApPi0qKvr97X6FKYznysNEw51jsw1ndkRVfNgF4DBEWwoC4yYw3MTYOFpjcCn0t3NLFwRus9wInjciD2jM1W/tPZKFltSdhT98PH7H2uQCq/+/uXgrwCnhZaGRqKSjAcqKaLjCd2EmfVLmMkmEGVOe04H3zmJCbA0IhdU2R2EHbMMoyZJxkLW1RwGayn+OHF7Cdmnemd1nIVFhdqE3kjf8SKhqsHRpnD7biqkIkazNsZa2W4bp00yBwHssEBOmZQt/VAzh1bRTn5lmiQ1HSrUzPTMdQ/z3dSSwkAu0FPwFtEGTvtDQOKlBWSST/lMUQ+wcOKkAKK5QX8l0s5nxPUBpHt5mpt0Ezea37av41UY/ZkuQ3s3nlUOGe8QjzYUNo8mkBvV93+r81CFoAODmQmH2bvLyb7RvW4a/mXW1BDsOw0zuqUrAT2m7oEmehM=")
                .spawn();

        this.wanderingTraderNPCEntity = Spawn.PROFILE_NPC.getSpawn().getWorld().spawn(Spawn.PROFILE_NPC.getSpawn(), WanderingTrader.class, wanderingTrader -> {
           wanderingTrader.setAI(false);
           wanderingTrader.setSilent(true);
           wanderingTrader.setInvulnerable(true);
           wanderingTrader.setCustomNameVisible(true);
           wanderingTrader.customName(Component.text("Selbsthilfezentrale", TextColor.fromHexString("#09a0db"))
                   .decoration(TextDecoration.ITALIC, false));
        });

        new MlgWarsNPC(this);
    }

    @Override
    public void onDisable() {
    }

    private void registerCommands() {

        new BuildCommand(this);
        new SetupCommand(this);

    }

    private void loadWorld() {

        for (Spawn spawn : Spawn.values()) {
            try {
                spawn.setPlayerSpawn(this.spawnManager.loadLocation(spawn.name()));
            } catch (NullPointerException nullPointerException) {
                Bukkit.getConsoleSender().sendMessage("Der Spawn " + spawn.name() + " wurde nicht gesetzt!");
            }
        }

        Location lobby = Spawn.SPAWN.getSpawn();

        if (lobby == null) {
            Bukkit.getConsoleSender().sendMessage("§cKein Spawn gesetzt!");
        } else {
            lobby.getWorld().getEntities().forEach(Entity::remove);

            World world = lobby.getWorld();
            world.setSpawnLocation((int) lobby.getX(), (int) lobby.getY(), (int) lobby.getZ());
            world.setDifficulty(Difficulty.PEACEFUL);
            world.setGameRule(GameRule.DO_MOB_SPAWNING, false);
            world.setGameRule(GameRule.DO_MOB_LOOT, false);
            world.setGameRule(GameRule.DO_WEATHER_CYCLE, false);
            world.setGameRule(GameRule.DO_DAYLIGHT_CYCLE, false);
            world.setTime(4000);
            world.setWeatherDuration(0);
            world.setThundering(false);
            world.setClearWeatherDuration(Integer.MAX_VALUE);

            this.spawnNpc();
        }
    }

    private void registerListener() {

        // PLAYER
        new JoinListener(this);
        new QuitListener(this);

        new InventoryClickListener(this);
        new ChatListener(this);
        new InteractListener(this);
        new MoveListener(this);
        new PickupListener(this);
        new NPCInteractListener(this);
        new WaterBucketEmptyListener(this);

        // PLAYER - CrimxAPI
        new PlayerCoinsChangeListener(this);

        // BLOCK
        new BlockPlaceListener(this);
        new BlockBreakListener(this);

        // ENTITY
        new DamageListener(this);

        // WORLD
        new WeatherChangeListener(this);

    }

    private void loadInventories() {
        new LobbySwitcherInventory(this);
        new NavigatorInventory(this);
    }

    private void runMainScheduler() {

        this.actionBarCount = 0;
        List<String> actionBarMessages = this.getConfig().getStringList("actionbar");

        Bukkit.getScheduler().runTaskTimer(this, () -> {
            Bukkit.getOnlinePlayers().forEach(player -> Actionbar.sendActionbar(player, actionBarMessages.get(this.actionBarCount).replace('&', '§')));

            this.actionbarTimer++;
            if (this.actionbarTimer % 10 == 0) {
                if (this.actionBarCount + 1 < actionBarMessages.size()) {
                    this.actionBarCount++;
                } else {
                    this.actionBarCount = 0;
                }
            }
        }, 0L, 20L);

    }

    public NPC getPerksNpc() {
        return this.perksNpc;
    }

    public String getPrefix() {
        return "§aCrimx§lLobby §8● §r";
    }

    public CrimxAPI getCrimxAPI() {
        return this.crimxAPI;
    }

    public PluginManager getPluginManager() {
        return this.pluginManager;
    }

    public Data getData() {
        return this.data;
    }

    public InventoryManager getInventoryManager() {
        return this.inventoryManager;
    }

    public SpawnManager getSpawnManager() {
        return this.spawnManager;
    }

    public LobbyScoreboard getLobbyScoreboard() {
        return this.lobbyScoreboard;
    }

    public Entity getWanderingTraderNPCEntity() {
        return this.wanderingTraderNPCEntity;
    }

    public ConnectFourGameManager getConnectFourGameManager() {
        return this.connectFourGameManager;
    }
}
