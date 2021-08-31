package de.fel1x.teamcrimx.crimxlobby;

import de.fel1x.teamcrimx.crimxlobby.database.LobbyDatabasePlayer;

import java.util.*;

public class Data {

    private final List<UUID> builders;

    private final Map<UUID, Integer> playerHiderState;
    private final Map<UUID, LobbyDatabasePlayer> lobbyDatabasePlayer;

    public Data() {

        this.builders = new ArrayList<>();

        this.playerHiderState = new HashMap<>();

        this.lobbyDatabasePlayer = new HashMap<>();
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

}
