package de.fel1x.teamcrimx.crimxlobby;

import com.github.juliarn.npc.NPC;
import com.github.juliarn.npc.NPCPool;
import com.github.juliarn.npc.modifier.MetadataModifier;
import com.github.juliarn.npc.profile.Profile;
import de.fel1x.teamcrimx.crimxapi.CrimxAPI;
import de.fel1x.teamcrimx.crimxapi.utils.Actionbar;
import de.fel1x.teamcrimx.crimxapi.utils.npc.NPCCreator;
import de.fel1x.teamcrimx.crimxlobby.commands.BuildCommand;
import de.fel1x.teamcrimx.crimxlobby.commands.SetupCommand;
import de.fel1x.teamcrimx.crimxlobby.cosmetics.armor.RainbowArmor;
import de.fel1x.teamcrimx.crimxlobby.listeners.block.BlockBreakListener;
import de.fel1x.teamcrimx.crimxlobby.listeners.block.BlockPlaceListener;
import de.fel1x.teamcrimx.crimxlobby.listeners.entity.DamageListener;
import de.fel1x.teamcrimx.crimxlobby.listeners.entity.ProjectileHitListener;
import de.fel1x.teamcrimx.crimxlobby.listeners.player.*;
import de.fel1x.teamcrimx.crimxlobby.listeners.world.WeatherChangeListener;
import de.fel1x.teamcrimx.crimxlobby.manager.SpawnManager;
import de.fel1x.teamcrimx.crimxlobby.objects.Spawn;
import de.fel1x.teamcrimx.crimxlobby.scoreboard.LobbyScoreboard;
import fr.minuskube.inv.InventoryManager;
import org.bukkit.*;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.CreatureSpawnEvent;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.util.UUID;

public final class CrimxLobby extends JavaPlugin {

    private static CrimxLobby instance;
    private final SpawnManager spawnManager = new SpawnManager();
    private final Random random = new Random();
    private int actionBarCount;
    private int actionbarTimer = 0;
    private Data data;
    private CrimxAPI crimxAPI;
    private PluginManager pluginManager;
    private NPCPool npcPool;
    private InventoryManager inventoryManager;
    private LobbyScoreboard lobbyScoreboard;
    private NPC lobbyNpc;
    private NPC perksNpc;

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

        Bukkit.createWorld(new WorldCreator("lobbysommer"));

        this.crimxAPI = CrimxAPI.getInstance();

        this.pluginManager = Bukkit.getPluginManager();
        this.data = new Data();

        this.npcPool = new NPCPool(this, 125, 25, 40);

        this.inventoryManager = new InventoryManager(this);
        this.inventoryManager.init();

        this.lobbyScoreboard = new LobbyScoreboard();

        this.registerCommands();
        this.registerListener();

        this.loadWorld();

        this.spawnNpc();

        this.runMainScheduler();

    }

    private void spawnNpc() {
        this.perksNpc = new NPCCreator(new Location(Bukkit.getWorld("lobbysommer"), 16.5, 13, -27.5))
                .shouldImitatePlayer(true)
                .shouldLookAtPlayer(true)
                .addHeaders(new String[]{"§bteamcrimx§lDE §7- §bCoinshop", "§7Heute gute Pleis" })
                .createProfile("eyJ0aW1lc3RhbXAiOjE1ODU3NDMzODM2ODcsInByb2ZpbGVJZCI6IjkxZmUxOTY4N2M5MDQ2NTZhYTFmYzA1OTg2ZGQzZmU3IiwicHJvZmlsZU5hbWUiOiJoaGphYnJpcyIsInNpZ25hdHVyZVJlcXVpcmVkIjp0cnVlLCJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvOWU1YmQ2ZDVjOWQ1NDM0ODc1MTZjYzNmY2FiNDczNWUxZGFlMjEyYzM5MjdhYjllNDBlNmFlM2U1OWI5NThhYSJ9fX0=",
                        "amJcsYzBIKpdlkKnwDNJ9iI0jMh675fmxQkRtKXquQk1xMFUy7wlsOBnNI6qHH5aNWKnKbq+eID6u3XHe3fcbEShCQyMtydyMzdYjyro5LWl9XkRs3LhbQDUllnJmoG6sIeCNHZ+VAqDLeHh0ahhSqHhg9C+4831C71uCoL2ah1+mPh7GA9CAnyp09ZZ3t1eHW9fvwwVMiDONgZjB2LLom6QW+rMHV3eltQojhkulniomKEApPi0qKvr97X6FKYznysNEw51jsw1ndkRVfNgF4DBEWwoC4yYw3MTYOFpjcCn0t3NLFwRus9wInjciD2jM1W/tPZKFltSdhT98PH7H2uQCq/+/uXgrwCnhZaGRqKSjAcqKaLjCd2EmfVLmMkmEGVOe04H3zmJCbA0IhdU2R2EHbMMoyZJxkLW1RwGayn+OHF7Cdmnemd1nIVFhdqE3kjf8SKhqsHRpnD7biqkIkazNsZa2W4bp00yBwHssEBOmZQt/VAzh1bRTn5lmiQ1HSrUzPTMdQ/z3dSSwkAu0FPwFtEGTvtDQOKlBWSST/lMUQ+wcOKkAKK5QX8l0s5nxPUBpHt5mpt0Ezea37av41UY/ZkuQ3s3nlUOGe8QjzYUNo8mkBvV93+r81CFoAODmQmH2bvLyb7RvW4a/mXW1BDsOw0zuqUrAT2m7oEmehM=")
                .spawn();
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }

    private void registerCommands() {

        new BuildCommand(this);
        new SetupCommand(this);

    }

    private void loadWorld() {

        Location lobby = Spawn.SPAWN.getPlayerSpawn();

        if (lobby == null) {
            Bukkit.getConsoleSender().sendMessage("§cKein Spawn gesetzt!");
        } else {
            lobby.getWorld().getEntities().forEach(entity -> {
                if (!(entity instanceof ArmorStand)) {
                    entity.remove();
                }
            });

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
        }
    }

    private void registerListener() {

        // PLAYER
        new JoinListener(this);
        new QuitListener(this);

        new InventoryClickListener(this);
        new ChatListener(this);
        new InteractListener(this);
        new FishingHookListener(this);
        new ItemHeldListener(this);
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
        new ProjectileHitListener(this);

        // WORLD
        new WeatherChangeListener(this);

    }

    private void runMainScheduler() {

        this.actionBarCount = 0;
        List<String> actionBarMessages = this.getConfig().getStringList("actionbar");

        Bukkit.getScheduler().scheduleSyncRepeatingTask(this, () -> this.getData().getHueMap().forEach(((uuid, hue) -> {

            Player player = Bukkit.getPlayer(uuid);
            RainbowArmor rainbowArmor = new RainbowArmor();

            if (player != null && player.isOnline()) {
                hue = rainbowArmor.handleColor(hue, 0.005f);
                rainbowArmor.setArmor(player, hue, 0.02f);
                this.getData().getHueMap().put(uuid, hue);
            }

        })), 0L, 3L);

        Bukkit.getScheduler().runTaskTimer(this, () -> {
            this.data.getPlayerPet().forEach((uuid, entity) -> {
                Player owner = Bukkit.getPlayer(uuid);

                if (owner == null || !owner.isOnline() || entity.isDead()) {
                    return;
                }

                double distance = owner.getLocation().distanceSquared(entity.getLocation());

                if (distance > 10) {
                    //Bukkit.broadcastMessage("pathfinding " + owner.getName() + " distance > 10");
                    entity.getPathfinder().moveTo(owner.getLocation().clone()
                            .add(this.random.nextBoolean() ? 1 : -1, 0, this.random.nextBoolean() ? 1 : -1));
                    if (owner.getLocation().getY() > entity.getLocation().getY() + 3 || distance > 150) {
                        entity.teleport(owner.getLocation().
                                add(this.random.nextBoolean() ? 1 : -1, 0, this.random.nextBoolean() ? 1 : -1));
                        entity.getPathfinder().stopPathfinding();
                    }
                }

            });

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

    public NPCPool getNpcPool() {
        return this.npcPool;
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

    public NPC getLobbyNpc() {
        return this.lobbyNpc;
    }
}
