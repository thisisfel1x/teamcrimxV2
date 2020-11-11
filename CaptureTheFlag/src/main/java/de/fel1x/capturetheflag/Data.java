package de.fel1x.capturetheflag;

import de.fel1x.capturetheflag.database.Stats;
import de.fel1x.capturetheflag.filehandler.SpawnHandler;
import de.fel1x.capturetheflag.kit.Kit;
import de.fel1x.capturetheflag.world.Cuboid;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;

import java.util.*;

public class Data {

    Location redSpawnLocation;
    Location blueSpawnLocation;
    private final List<Player> players;
    private final List<Player> spectators;
    private final Map<Player, Player> lastHit;
    private final Map<Player, Kit> selectedKit;
    private final Map<Player, Stats> cachedStats;
    private final Map<UUID, Boolean> playerGG;
    private final Map<UUID, Long> playTime;
    private Player redFlagHolder;
    private Location redFlagBaseLocation;
    private Player blueFlagHolder;
    private Location blueFlagBaseLocation;
    private final List<Block> placedBlocks;
    private final Cuboid redSpawnCuboid;
    private final Cuboid blueSpawnCuboid;

    public Data() {

        this.players = new ArrayList<>();
        this.spectators = new ArrayList<>();

        this.placedBlocks = new ArrayList<>();

        this.selectedKit = new HashMap<>();
        this.lastHit = new HashMap<>();
        this.cachedStats = new HashMap<>();
        this.playerGG = new HashMap<>();
        this.playTime = new HashMap<>();

        this.redFlagHolder = null;

        try {
            this.redSpawnLocation = SpawnHandler.loadLocation("redSpawn");
            this.blueSpawnLocation = SpawnHandler.loadLocation("blueSpawn");
        } catch (Exception exception) {
            Bukkit.getConsoleSender().sendMessage("§cERROR: NO_SPAWN_FOUND");
        }

        try {
            this.redFlagBaseLocation = SpawnHandler.loadBannerLocation("redFlag").toCenterLocation();
            this.blueFlagBaseLocation = SpawnHandler.loadBannerLocation("blueFlag").toCenterLocation();
        } catch (Exception ignored) {
            Bukkit.getConsoleSender().sendMessage("§cERROR: FLAG_STORE_LOCATION_ERROR");
        }

        this.blueFlagHolder = null;

        this.redSpawnCuboid = new Cuboid(this.redSpawnLocation.clone().add(4, -4, 4), this.redSpawnLocation.clone().add(-4, 4, -4));
        this.blueSpawnCuboid = new Cuboid(this.blueSpawnLocation.clone().add(4, -4, 4), this.blueSpawnLocation.clone().add(-4, 4, -4));

    }

    public Map<Player, Kit> getSelectedKit() {
        return this.selectedKit;
    }

    public List<Player> getPlayers() {
        return this.players;
    }

    public List<Player> getSpectators() {
        return this.spectators;
    }

    public Player getBlueFlagHolder() {
        return this.blueFlagHolder;
    }

    public void setBlueFlagHolder(Player blueFlagHolder) {
        this.blueFlagHolder = blueFlagHolder;
    }

    public Player getRedFlagHolder() {
        return this.redFlagHolder;

    }

    public void setRedFlagHolder(Player redFlagHolder) {
        this.redFlagHolder = redFlagHolder;
    }

    public Location getRedFlagBaseLocation() {
        return this.redFlagBaseLocation;
    }

    public void setRedFlagBaseLocation(Location redFlagBaseLocation) {
        this.redFlagBaseLocation = redFlagBaseLocation;
    }

    public Location getBlueFlagBaseLocation() {
        return this.blueFlagBaseLocation;
    }

    public void setBlueFlagBaseLocation(Location blueFlagBaseLocation) {
        this.blueFlagBaseLocation = blueFlagBaseLocation;
    }

    public Map<Player, Player> getLastHit() {
        return this.lastHit;
    }

    public List<Block> getPlacedBlocks() {
        return this.placedBlocks;
    }

    public Cuboid getBlueSpawnCuboid() {
        return this.blueSpawnCuboid;
    }

    public Cuboid getRedSpawnCuboid() {
        return this.redSpawnCuboid;
    }

    public Map<Player, Stats> getCachedStats() {
        return this.cachedStats;
    }

    public Map<UUID, Boolean> getPlayerGG() {
        return this.playerGG;
    }

    public Map<UUID, Long> getPlayTime() {
        return this.playTime;
    }
}
