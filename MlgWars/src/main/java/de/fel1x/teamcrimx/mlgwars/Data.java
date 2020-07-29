package de.fel1x.teamcrimx.mlgwars;

import de.fel1x.teamcrimx.crimxapi.utils.Cuboid;
import de.fel1x.teamcrimx.mlgwars.kit.Kit;
import org.bson.Document;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;

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

    private final Map<UUID, Boolean> playerGg;

    private Cuboid middleRegion;
    private Cuboid mapRegion;

    // KIT-STORAGE
    private final HashMap<UUID, ArrayList<Block>> placedExploderTnt;

    public Data() {
        this.players = new ArrayList<>();
        this.spectators = new ArrayList<>();

        this.playTime = new HashMap<>();

        this.selectedKit = new HashMap<>();

        this.lastHit = new HashMap<>();

        this.playerSpawns = new ArrayList<>();

        this.mlgWarsPlayerDocument = new HashMap<>();
        this.networkPlayerDocument = new HashMap<>();

        this.playerGg = new HashMap<>();

        this.placedExploderTnt = new HashMap<>();
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

    public HashMap<UUID, ArrayList<Block>> getPlacedExploderTnt() {
        return placedExploderTnt;
    }
}
