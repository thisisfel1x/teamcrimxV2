package de.fel1x.teamcrimx.crimxlobby;

import com.destroystokyo.paper.entity.Pathfinder;
import com.github.juliarn.npc.NPC;
import com.github.juliarn.npc.NPCPool;
import com.github.juliarn.npc.modifier.MetadataModifier;
import com.github.juliarn.npc.profile.Profile;
import de.fel1x.teamcrimx.crimxapi.CrimxAPI;
import de.fel1x.teamcrimx.crimxapi.utils.Actionbar;
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
import de.fel1x.teamcrimx.crimxlobby.minigames.watermlg.WaterMlgHandler;
import de.fel1x.teamcrimx.crimxlobby.objects.Spawn;
import de.fel1x.teamcrimx.crimxlobby.scoreboard.LobbyScoreboard;
import fr.minuskube.inv.InventoryManager;
import org.bukkit.*;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Player;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.util.UUID;

public final class CrimxLobby extends JavaPlugin {

    private static CrimxLobby instance;
    int actionBarCount;
    private int actionbarTimer = 0;
    private Data data;
    private CrimxAPI crimxAPI;
    private PluginManager pluginManager;
    private NPCPool npcPool;
    private InventoryManager inventoryManager;
    private final SpawnManager spawnManager = new SpawnManager();
    private WaterMlgHandler waterMlgHandler;
    private LobbyScoreboard lobbyScoreboard;
    private NPC lobbyNpc;
    private final Random random = new Random();

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

        this.crimxAPI = CrimxAPI.getInstance();

        this.pluginManager = Bukkit.getPluginManager();
        this.data = new Data();

        this.npcPool = new NPCPool(this, 125, 25, 40);

        this.inventoryManager = new InventoryManager(this);
        this.inventoryManager.init();

        this.waterMlgHandler = new WaterMlgHandler();

        this.lobbyScoreboard = new LobbyScoreboard();

        this.registerCommands();
        this.registerListener();

        this.loadWorld();
        
        this.spawnNpc();

        this.runMainScheduler();

    }

    private void spawnNpc() {
        this.lobbyNpc = new NPC.Builder(new Profile(UUID.randomUUID(), "§a§lDein Profil",
                Collections.singletonList(new Profile.Property("textures",
                        "ewogICJ0aW1lc3RhbXAiIDogMTYwNTk1MzY1ODEzMiwKICAicHJvZmlsZUlkIiA6ICJhYTZhNDA5NjU4YTk0MDIwYmU3OGQwN2JkMzVlNTg5MyIsCiAgInByb2ZpbGVOYW1lIiA6ICJiejE0IiwKICAic2lnbmF0dXJlUmVxdWlyZWQiIDogdHJ1ZSwKICAidGV4dHVyZXMiIDogewogICAgIlNLSU4iIDogewogICAgICAidXJsIiA6ICJodHRwOi8vdGV4dHVyZXMubWluZWNyYWZ0Lm5ldC90ZXh0dXJlLzc2MjA2NjliZmY3MTJhYWE3OGQzMGQ5ZGNjNTU5MDYxMWU2YzEwZjIzY2Q2YWQ1MTc1Zjc1N2RlYTA1YWU3NjgiCiAgICB9CiAgfQp9",
                        "A0UUTGQgMlEeSEYc5H/mYn8GjKsq2fd4X+2mPnszJHRWH3GhO01QU7fO/HSHcccronEb1eWQPgkqCD6rtXWslh7nubKvORJZcBiVpOWTNpjJsDZH57XOghdKsDWp79jTUMM3AaY8irRMC8SlXdMK9RM8uMPofb0FgrHaMUI4D8vTpPEvEZXbyhEpuuCKs3Psb1BBu130x3xm7w1kGKOoyQYSZ1CE9JdYAGF3FOCeAsmGohpO6KMtGsrWJ/wR1lExzbAjlp5vPtMXpqpsQoCTEldtiZ8gxB8bU+5ms5FYxbIUy1I7N5YYcpNUFiBEwWbFHIkzYt5KoQEXZ2P3mTOgJsjyUlKxlDJOQRDeNZuuNMZDiuqL7JGrtSKNRkCwGAE+Pr91YOAjhymzL7t/Hhn9V6i6198HrDDXZzZqm9ebc6Y7bdSSTHtSDWYXD80B0CgGUyMTS9g0h8qN6rFN6taMxEeBxBuI8Hpj5ERchrqT9xhJzOapcQJEf59l60h2BcDjDJJxefq4N6hvvGPL+t1H/D/bPN5eF7HYJKSz/y/lpJutqWJfnolhoxTf7Qy0XPp7BNQxOFdDOqujLb0haPxpcBCrhgzd/QXUlcpYpMC9zov3jqL5muXg6aXTDkEU2UlLDhPNEXolM2nbvG55WXNvpe3ulbVBbCs3PQDi7bsdLb0="))))
                .lookAtPlayer(true)
                .imitatePlayer(true)
                .location(new Location(Bukkit.getWorlds().get(0), -168.5, 64, 138.5))
                .spawnCustomizer((npc1, player) -> npc1.metadata().queue(MetadataModifier.EntityMetadata.SKIN_LAYERS, true).send())
                .build(this.npcPool);
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
            return;
        } else {
            Bukkit.createWorld(new WorldCreator(lobby.getWorld().getName()));
        }

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
        world.setStorm(false);
        world.setThunderDuration(0);
        world.setThundering(false);
        world.setTime(1200);

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

                if(owner == null || !owner.isOnline() || entity.isDead()) {
                    return;
                }

                double distance = owner.getLocation().distanceSquared(entity.getLocation());

                if(distance > 10) {
                    entity.getPathfinder().moveTo(owner.getLocation().clone()
                            .add(this.random.nextBoolean() ? 1 : -1, 0, this.random.nextBoolean() ? 1 : -1));
                    if(owner.getLocation().getY() > entity.getLocation().getY() + 3 || distance > 60) {
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

    public WaterMlgHandler getWaterMlgHandler() {
        return this.waterMlgHandler;
    }

    public LobbyScoreboard getLobbyScoreboard() {
        return this.lobbyScoreboard;
    }

    public NPC getLobbyNpc() {
        return this.lobbyNpc;
    }
}
