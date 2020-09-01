package de.fel1x.capturetheflag;

import de.fel1x.capturetheflag.filehandler.SpawnHandler;
import de.fel1x.capturetheflag.world.Cuboid;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Data {

    Location redSpawnLocation;
    Location blueSpawnLocation;
    private List<Player> players;
    private List<Player> spectators;
    private Map<Player, Player> lastHit;
    private Player redFlagHolder;
    private Location redFlagLocation;
    private Location redFlagBaseLocation;
    private Player blueFlagHolder;
    private Location blueFlagLocation;
    private Location blueFlagBaseLocation;
    private List<Block> placedBlocks;
    private Cuboid redSpawnCuboid;
    private Cuboid blueSpawnCuboid;

    public Data() {

        players = new ArrayList<>();
        spectators = new ArrayList<>();

        placedBlocks = new ArrayList<>();

        lastHit = new HashMap<>();

        redFlagHolder = null;

        try {
            redSpawnLocation = SpawnHandler.loadLocation("redSpawn");
            blueSpawnLocation = SpawnHandler.loadLocation("blueSpawn");
        } catch (Exception exception) {
            Bukkit.getConsoleSender().sendMessage("§cERROR: NO_SPAWN_FOUND");
        }

        try {
            redFlagLocation = SpawnHandler.loadBannerLocation("redFlag");
            redFlagBaseLocation = SpawnHandler.loadBannerLocation("redFlag");
            blueFlagLocation = SpawnHandler.loadBannerLocation("blueFlag");
            blueFlagBaseLocation = SpawnHandler.loadBannerLocation("blueFlag");
        } catch (Exception ignored) {
            Bukkit.getConsoleSender().sendMessage("§cERROR: FLAG_STORE_LOCATION_ERROR");
        }

        blueFlagHolder = null;

        redSpawnCuboid = new Cuboid(redSpawnLocation.clone().add(4, -4, 4), redSpawnLocation.clone().add(-4, 4, -4));
        blueSpawnCuboid = new Cuboid(blueSpawnLocation.clone().add(4, -4, 4), blueSpawnLocation.clone().add(-4, 4, -4));

    }

    public List<Player> getPlayers() {
        return players;
    }

    public void setPlayers(List<Player> players) {
        this.players = players;
    }

    public List<Player> getSpectators() {
        return spectators;
    }

    public void setSpectators(List<Player> spectators) {
        this.spectators = spectators;
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

    public Location getRedFlagLocation() {
        return redFlagLocation;
    }

    public void setRedFlagLocation(Location redFlagLocation) {
        this.redFlagLocation = redFlagLocation;
    }

    public Location getBlueFlagLocation() {
        return blueFlagLocation;
    }

    public void setBlueFlagLocation(Location blueFlagLocation) {
        this.blueFlagLocation = blueFlagLocation;
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
}
