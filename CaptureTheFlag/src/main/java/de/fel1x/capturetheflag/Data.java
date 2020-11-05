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
    private List<Player> players;
    private List<Player> spectators;
    private Map<Player, Player> lastHit;
    private Map<Player, Kit> selectedKit;
    private Map<Player, Stats> cachedStats;
    private Map<UUID, Boolean> playerGG;
    private Map<UUID, Long> playTime;
    private Player redFlagHolder;
    private Location redFlagBaseLocation;
    private Player blueFlagHolder;
    private Location blueFlagBaseLocation;
    private List<Block> placedBlocks;
    private Cuboid redSpawnCuboid;
    private Cuboid blueSpawnCuboid;

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

        this.redSpawnCuboid = new Cuboid(redSpawnLocation.clone().add(4, -4, 4), redSpawnLocation.clone().add(-4, 4, -4));
        this.blueSpawnCuboid = new Cuboid(blueSpawnLocation.clone().add(4, -4, 4), blueSpawnLocation.clone().add(-4, 4, -4));

    }

    public Map<Player, Kit> getSelectedKit() {
        return selectedKit;
    }

    public List<Player> getPlayers() {
        return players;
    }

    public List<Player> getSpectators() {
        return spectators;
    }

    public Player getBlueFlagHolder() {
        return blueFlagHolder;
    }

    public void setBlueFlagHolder(Player blueFlagHolder) {
        this.blueFlagHolder = blueFlagHolder;
    }

    public Player getRedFlagHolder() {
        return redFlagHolder;

    }

    public void setRedFlagHolder(Player redFlagHolder) {
        this.redFlagHolder = redFlagHolder;
    }

    public Location getRedFlagBaseLocation() {
        return redFlagBaseLocation;
    }

    public void setRedFlagBaseLocation(Location redFlagBaseLocation) {
        this.redFlagBaseLocation = redFlagBaseLocation;
    }

    public Location getBlueFlagBaseLocation() {
        return blueFlagBaseLocation;
    }

    public void setBlueFlagBaseLocation(Location blueFlagBaseLocation) {
        this.blueFlagBaseLocation = blueFlagBaseLocation;
    }

    public Map<Player, Player> getLastHit() {
        return lastHit;
    }

    public List<Block> getPlacedBlocks() {
        return placedBlocks;
    }

    public Cuboid getBlueSpawnCuboid() {
        return blueSpawnCuboid;
    }

    public Cuboid getRedSpawnCuboid() {
        return redSpawnCuboid;
    }

    public Map<Player, Stats> getCachedStats() {
        return cachedStats;
    }

    public Map<UUID, Boolean> getPlayerGG() {
        return playerGG;
    }

    public Map<UUID, Long> getPlayTime() {
        return playTime;
    }
}
