package de.fel1x.bingo;

import de.fel1x.bingo.objects.stats.Stats;
import org.bukkit.Material;
import org.bukkit.entity.Player;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class Data {

    private final List<Player> players;
    private final List<Player> spectators;

    private final Map<Player, Stats> cachedStats;
    private final Map<UUID, Long> playTime;
    private final Map<UUID, Boolean> playerGG;

    private final Map<Material, Material> randomizedBlocks;

    public Data() {

        this.players = new ArrayList<>();
        this.spectators = new ArrayList<>();

        this.cachedStats = new HashMap<>();
        this.playTime = new HashMap<>();
        this.playerGG = new HashMap<>();

        List<Material> normalBlocks = Arrays.stream(Material.values()).filter(Material::isBlock)
                .filter(Material::isItem).collect(Collectors.toList());
        List<Material> randomizedBlocksList = Arrays.stream(Material.values()).filter(Material::isBlock)
                .filter(Material::isItem).collect(Collectors.toList());

        Collections.shuffle(randomizedBlocksList);

        this.randomizedBlocks = IntStream.range(0, normalBlocks.size()).boxed().collect(Collectors.toMap(normalBlocks::get, randomizedBlocksList::get));

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

    public Map<Material, Material> getRandomizedBlocks() {
        return this.randomizedBlocks;
    }
}
