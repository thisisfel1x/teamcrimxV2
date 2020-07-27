package de.fel1x.teamcrimx.crimxlobby;

import de.fel1x.teamcrimx.crimxlobby.cosmetics.ICosmetic;
import de.fel1x.teamcrimx.crimxlobby.database.LobbyDatabasePlayer;
import de.fel1x.teamcrimx.crimxlobby.minigames.jumpandrun.JumpAndRunPlayer;
import net.jitse.npclib.api.NPC;
import org.bson.Document;

import java.util.*;

public class Data {

    private List<UUID> builders;

    private Map<UUID, NPC> playerNPCs;
    private Map<UUID, Integer> playerHiderState;
    private Map<UUID, Document> playerMongoDocument;
    private Map<UUID, Document> playerMongoNetworkDocument;
    private Map<UUID, LobbyDatabasePlayer> lobbyDatabasePlayer;

    private Map<UUID, JumpAndRunPlayer> jumpAndRunPlayers;
    private List<UUID> jumpers;

    private Map<UUID, ICosmetic> cosmetic;
    private Map<UUID, Float> hueMap;

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
        return builders;
    }

    public Map<UUID, NPC> getPlayerNPCs() {
        return playerNPCs;
    }

    public Map<UUID, Integer> getPlayerHiderState() {
        return playerHiderState;
    }

    public Map<UUID, Document> getPlayerMongoDocument() {
        return playerMongoDocument;
    }

    public Map<UUID, Document> getPlayerMongoNetworkDocument() {
        return playerMongoNetworkDocument;
    }

    public Map<UUID, LobbyDatabasePlayer> getLobbyDatabasePlayer() {
        return lobbyDatabasePlayer;
    }

    public Map<UUID, JumpAndRunPlayer> getJumpAndRunPlayers() {
        return jumpAndRunPlayers;
    }

    public List<UUID> getJumpers() {
        return jumpers;
    }

    public Map<UUID, ICosmetic> getCosmetic() {
        return cosmetic;
    }

    public Map<UUID, Float> getHueMap() {
        return hueMap;
    }
}
