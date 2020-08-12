package de.fel1x.teamcrimx.mlgwars;

import de.fel1x.teamcrimx.crimxapi.utils.Cuboid;
import de.fel1x.teamcrimx.mlgwars.kit.Kit;
import de.fel1x.teamcrimx.mlgwars.objects.ScoreboardTeam;
import org.bson.Document;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.*;

public class Data {

    private final List<Player> players;
    private final List<Player> spectators;

    private final Map<UUID, Long> playTime;

    private final Map<Player, Kit> selectedKit;

    private final Map<Player, Player> lastHit;

    private final List<Location> playerSpawns;

    private final Map<UUID, Document> mlgWarsPlayerDocument;
    private final Map<UUID, Document> networkPlayerDocument;

    private final Map<Integer, ScoreboardTeam> gameTeams;

    private final Map<UUID, Boolean> playerGg;

    private Cuboid middleRegion;
    private Cuboid mapRegion;

    // KIT-STORAGE
    private final Map<UUID, ArrayList<Block>> placedExploderTnt;
    private final Map<UUID, BukkitRunnable> thorTask;
    private final Map<UUID, BukkitRunnable> kangarooTask;
    private final Map<UUID, BukkitRunnable> farmerTask;
    private final Map<UUID, BukkitRunnable> teleporterTask;
    private final Map<UUID, ArrayList<BukkitRunnable>> eggTask;
    private final Map<UUID, ArrayList<BukkitRunnable>> turtleTask;
    private final Map<UUID, ArrayList<BukkitRunnable>> csgoTasks;

    public Data() {
        this.players = new ArrayList<>();
        this.spectators = new ArrayList<>();

        this.playTime = new HashMap<>();

        this.selectedKit = new HashMap<>();

        this.lastHit = new HashMap<>();

        this.playerSpawns = new ArrayList<>();

        this.mlgWarsPlayerDocument = new HashMap<>();
        this.networkPlayerDocument = new HashMap<>();

        this.gameTeams = new HashMap<>();

        this.playerGg = new HashMap<>();

        this.placedExploderTnt = new HashMap<>();
        this.thorTask = new HashMap<>();
        this.kangarooTask = new HashMap<>();
        this.farmerTask = new HashMap<>();
        this.teleporterTask = new HashMap<>();
        this.eggTask = new HashMap<>();
        this.turtleTask = new HashMap<>();
        this.csgoTasks = new HashMap<>();
    }

    public List<Player> getPlayers() {
        return players;
    }

    public List<Player> getSpectators() {
        return spectators;
    }

    public Map<Player, Player> getLastHit() {
        return lastHit;
    }

    public List<Location> getPlayerSpawns() {
        return playerSpawns;
    }

    public Cuboid getMiddleRegion() {
        return middleRegion;
    }

    public void setMiddleRegion(Cuboid middleRegion) {
        this.middleRegion = middleRegion;
    }

    public Cuboid getMapRegion() {
        return mapRegion;
    }

    public void setMapRegion(Cuboid mapRegion) {
        this.mapRegion = mapRegion;
    }

    public Map<UUID, Long> getPlayTime() {
        return playTime;
    }

    public Map<UUID, Document> getMlgWarsPlayerDocument() {
        return mlgWarsPlayerDocument;
    }

    public Map<UUID, Document> getNetworkPlayerDocument() {
        return networkPlayerDocument;
    }

    public Map<Player, Kit> getSelectedKit() {
        return selectedKit;
    }

    public Map<UUID, Boolean> getPlayerGg() {
        return playerGg;
    }

    public Map<UUID, ArrayList<Block>> getPlacedExploderTnt() {
        return placedExploderTnt;
    }

    public Map<UUID, BukkitRunnable> getThorTask() {
        return thorTask;
    }

    public Map<UUID, BukkitRunnable> getKangarooTask() {
        return kangarooTask;
    }

    public Map<UUID, ArrayList<BukkitRunnable>> getEggTask() {
        return eggTask;
    }

    public Map<UUID, BukkitRunnable> getFarmerTask() {
        return farmerTask;
    }

    public Map<UUID, ArrayList<BukkitRunnable>> getTurtleTask() {
        return turtleTask;
    }

    public Map<UUID, BukkitRunnable> getTeleporterTask() {
        return teleporterTask;
    }

    public Map<UUID, ArrayList<BukkitRunnable>> getCsgoTasks() {
        return csgoTasks;
    }

    public Map<Integer, ScoreboardTeam> getGameTeams() {
        return gameTeams;
    }
}
