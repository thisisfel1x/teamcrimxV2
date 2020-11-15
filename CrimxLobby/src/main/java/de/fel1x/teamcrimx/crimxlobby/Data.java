package de.fel1x.teamcrimx.crimxlobby;

import de.fel1x.teamcrimx.crimxlobby.cosmetics.ICosmetic;
import de.fel1x.teamcrimx.crimxlobby.database.LobbyDatabasePlayer;
import de.fel1x.teamcrimx.crimxlobby.minigames.jumpandrun.JumpAndRunPlayer;
import net.jitse.npclib.api.NPC;
import org.bson.Document;

import java.util.*;

public class Data {

    private final List<UUID> builders;

    private final Map<UUID, NPC> playerNPCs;
    private final Map<UUID, Integer> playerHiderState;
    private final Map<UUID, Document> playerMongoDocument;
    private final Map<UUID, Document> playerMongoNetworkDocument;
    private final Map<UUID, LobbyDatabasePlayer> lobbyDatabasePlayer;

    private final Map<UUID, JumpAndRunPlayer> jumpAndRunPlayers;
    private final List<UUID> jumpers;

    private final Map<UUID, ICosmetic> cosmetic;
    private final Map<UUID, Float> hueMap;

    public Data() {

        this.builders = new ArrayList<>();

        this.playerNPCs = new HashMap<>();
        this.playerHiderState = new HashMap<>();

        this.playerMongoDocument = new HashMap<>();
        this.playerMongoNetworkDocument = new HashMap<>();
        this.lobbyDatabasePlayer = new HashMap<>();

        this.jumpAndRunPlayers = new HashMap<>();
        this.jumpers = new ArrayList<>();

        this.cosmetic = new HashMap<>();
        this.hueMap = new HashMap<>();

    }

    public List<UUID> getBuilders() {
        return this.builders;
    }

    public Map<UUID, NPC> getPlayerNPCs() {
        return this.playerNPCs;
    }

    public Map<UUID, Integer> getPlayerHiderState() {
        return this.playerHiderState;
    }

    public Map<UUID, Document> getPlayerMongoDocument() {
        return this.playerMongoDocument;
    }

    public Map<UUID, Document> getPlayerMongoNetworkDocument() {
        return this.playerMongoNetworkDocument;
    }

    public Map<UUID, LobbyDatabasePlayer> getLobbyDatabasePlayer() {
        return this.lobbyDatabasePlayer;
    }

    public Map<UUID, JumpAndRunPlayer> getJumpAndRunPlayers() {
        return this.jumpAndRunPlayers;
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
}
