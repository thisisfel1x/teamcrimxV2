package de.fel1x.teamcrimx.floorislava;

import de.fel1x.teamcrimx.floorislava.database.Stats;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class Data {
    private final ArrayList<Player> players = new ArrayList<>();

    private final ArrayList<Player> spectators = new ArrayList<>();

    private final HashMap<UUID, Boolean> playerGG = new HashMap<>();

    private final HashMap<Player, Stats> cachedStats = new HashMap<>();

    private final Map<UUID, Long> playTime = new HashMap<>();

    public ArrayList<Player> getPlayers() {
        return this.players;
    }

    public ArrayList<Player> getSpectators() {
        return this.spectators;
    }

    public HashMap<UUID, Boolean> getPlayerGG() {
        return this.playerGG;
    }

    public HashMap<Player, Stats> getCachedStats() {
        return this.cachedStats;
    }

    public Map<UUID, Long> getPlayTime() {
        return this.playTime;
    }
}
