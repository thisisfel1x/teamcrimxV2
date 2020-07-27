package de.fel1x.teamcrimx.mlgwars;

import de.fel1x.teamcrimx.crimxapi.utils.Cuboid;
import de.fel1x.teamcrimx.mlgwars.enums.Spawns;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import java.util.*;

public class Data {

    private final List<Player> players;
    private final List<Player> spectators;

    private final Map<Player, Player> lastHit;

    private final List<Location> playerSpawns;

    private Cuboid middleRegion;
    private Cuboid mapRegion;

    public Data() {
        this.players = new ArrayList<>();
        this.spectators = new ArrayList<>();

        this.lastHit = new HashMap<>();

        this.playerSpawns = new ArrayList<>();
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
}
