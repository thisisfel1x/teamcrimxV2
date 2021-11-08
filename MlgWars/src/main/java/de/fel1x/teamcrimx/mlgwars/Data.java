package de.fel1x.teamcrimx.mlgwars;

import de.fel1x.teamcrimx.crimxapi.utils.Cuboid;
import de.fel1x.teamcrimx.mlgwars.objects.GamePlayer;
import de.fel1x.teamcrimx.mlgwars.objects.MlgWarsTeam;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.*;

public class Data {

    private final List<Player> players;
    private final List<Player> spectators;

    private final HashMap<UUID, GamePlayer> gamePlayers;

    private final Map<Player, Player> lastHit;

    private final List<Location> playerSpawns;

    private final Map<Integer, MlgWarsTeam> gameTeams;

    // KIT-STORAGE
    private final Map<UUID, ArrayList<Block>> placedExploderTnt;
    private final Map<UUID, BukkitRunnable> thorTask;
    private final Map<UUID, BukkitRunnable> kangarooTask;
    private final Map<UUID, BukkitRunnable> farmerTask;
    private final Map<UUID, BukkitRunnable> teleporterTask;
    private final Map<UUID, ArrayList<BukkitRunnable>> eggTask;
    private final Map<UUID, ArrayList<BukkitRunnable>> turtleTask;
    private final Map<UUID, ArrayList<BukkitRunnable>> csgoTasks;
    private Cuboid middleRegion;
    private Cuboid mapRegion;

    public Data() {
        this.players = new ArrayList<>();
        this.spectators = new ArrayList<>();

        this.lastHit = new HashMap<>();

        this.playerSpawns = new ArrayList<>();

        this.gameTeams = new HashMap<>();

        this.placedExploderTnt = new HashMap<>();
        this.thorTask = new HashMap<>();
        this.kangarooTask = new HashMap<>();
        this.farmerTask = new HashMap<>();
        this.teleporterTask = new HashMap<>();
        this.eggTask = new HashMap<>();
        this.turtleTask = new HashMap<>();
        this.csgoTasks = new HashMap<>();

        this.gamePlayers = new HashMap<>();
    }

    public HashMap<UUID, GamePlayer> getGamePlayers() {
        return this.gamePlayers;
    }

    public List<Player> getPlayers() {
        return this.players;
    }

    public List<Player> getSpectators() {
        return this.spectators;
    }

    public Map<Player, Player> getLastHit() {
        return this.lastHit;
    }

    public List<Location> getPlayerSpawns() {
        return this.playerSpawns;
    }

    public Cuboid getMiddleRegion() {
        return this.middleRegion;
    }

    public void setMiddleRegion(Cuboid middleRegion) {
        this.middleRegion = middleRegion;
    }

    public Cuboid getMapRegion() {
        return this.mapRegion;
    }

    public void setMapRegion(Cuboid mapRegion) {
        this.mapRegion = mapRegion;
    }

    public Map<UUID, ArrayList<Block>> getPlacedExploderTnt() {
        return this.placedExploderTnt;
    }

    public Map<UUID, BukkitRunnable> getThorTask() {
        return this.thorTask;
    }

    public Map<UUID, BukkitRunnable> getKangarooTask() {
        return this.kangarooTask;
    }

    public Map<UUID, ArrayList<BukkitRunnable>> getEggTask() {
        return this.eggTask;
    }

    public Map<UUID, BukkitRunnable> getFarmerTask() {
        return this.farmerTask;
    }

    public Map<UUID, ArrayList<BukkitRunnable>> getTurtleTask() {
        return this.turtleTask;
    }

    public Map<UUID, BukkitRunnable> getTeleporterTask() {
        return this.teleporterTask;
    }

    public Map<UUID, ArrayList<BukkitRunnable>> getCsgoTasks() {
        return this.csgoTasks;
    }

    public Map<Integer, MlgWarsTeam> getGameTeams() {
        return this.gameTeams;
    }
}
