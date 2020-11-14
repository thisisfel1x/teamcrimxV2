package de.fel1x.bingo;

import de.fel1x.bingo.objects.stats.Stats;
import org.bukkit.entity.Player;

import java.util.*;

public class Data {

    private final List<Player> players;
    private final List<Player> spectators;

    private final Map<Player, Stats> cachedStats;
    private final Map<UUID, Long> playTime;
    private final Map<UUID, Boolean> playerGG;

    public Data() {

        this.players = new ArrayList<>();
        this.spectators = new ArrayList<>();

        this.cachedStats = new HashMap<>();
        this.playTime = new HashMap<>();
        this.playerGG = new HashMap<>();

    }

    public List<Player> getPlayers() {
        return this.players;
    }

    public List<Player> getSpectators() {
        return this.spectators;
    }

    public Map<Player, Stats> getCachedStats() {
        return this.cachedStats;
    }

    public Map<UUID, Long> getPlayTime() {
        return this.playTime;
    }

    public Map<UUID, Boolean> getPlayerGG() {
        return this.playerGG;
    }
}
