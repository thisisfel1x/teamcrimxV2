package de.fel1x.teamcrimx.crimxlobby;

import de.fel1x.teamcrimx.crimxlobby.cosmetics.ICosmetic;
import de.fel1x.teamcrimx.crimxlobby.database.LobbyDatabasePlayer;
import org.bukkit.entity.Mob;

import java.util.*;

public class Data {

    private final List<UUID> builders;

    private final Map<UUID, Integer> playerHiderState;
    private final Map<UUID, LobbyDatabasePlayer> lobbyDatabasePlayer;

    private final List<UUID> jumpers;

    private final Map<UUID, ICosmetic> cosmetic;
    private final Map<UUID, Mob> playerPet;
    private final Map<UUID, Float> hueMap;

    public Data() {

        this.builders = new ArrayList<>();

        this.playerHiderState = new HashMap<>();

        this.lobbyDatabasePlayer = new HashMap<>();

        this.jumpers = new ArrayList<>();

        this.cosmetic = new HashMap<>();
        this.playerPet = new HashMap<>();
        this.hueMap = new HashMap<>();

    }

    public List<UUID> getBuilders() {
        return this.builders;
    }

    public Map<UUID, Integer> getPlayerHiderState() {
        return this.playerHiderState;
    }

    public Map<UUID, LobbyDatabasePlayer> getLobbyDatabasePlayer() {
        return this.lobbyDatabasePlayer;
    }

    public List<UUID> getJumpers() {
        return this.jumpers;
    }

    public Map<UUID, ICosmetic> getCosmetic() {
        return this.cosmetic;
    }

    public Map<UUID, Float> getHueMap() {
        return this.hueMap;
    }

    public Map<UUID, Mob> getPlayerPet() {
        return this.playerPet;
    }
}
