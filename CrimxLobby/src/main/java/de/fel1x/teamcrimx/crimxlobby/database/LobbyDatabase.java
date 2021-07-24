package de.fel1x.teamcrimx.crimxlobby.database;

import de.fel1x.teamcrimx.crimxapi.CrimxAPI;
import de.fel1x.teamcrimx.crimxapi.database.mongodb.MongoDB;
import de.fel1x.teamcrimx.crimxapi.database.mongodb.MongoDBCollection;
import de.fel1x.teamcrimx.crimxlobby.CrimxLobby;
import de.fel1x.teamcrimx.crimxlobby.objects.Spawn;
import org.bson.Document;
import org.bukkit.Location;
import org.bukkit.entity.Player;

public class LobbyDatabase {

    private final CrimxLobby crimxLobby = CrimxLobby.getInstance();
    private final CrimxAPI crimxAPI = this.crimxLobby.getCrimxAPI();
    private final MongoDB mongoDB = this.crimxAPI.getMongoDB();

    public void createPlayerData(Player player) {
        player.sendMessage(this.crimxLobby.getPrefix() + "§7Nutzerdaten werden angelegt...");

        final Location spawnLocation = Spawn.SPAWN.getPlayerSpawn();
        String locationSerialized = spawnLocation.getWorld().getName() + ":" + spawnLocation.getX()
                + ":" + spawnLocation.getY() + ":" + spawnLocation.getZ()
                + ":" + spawnLocation.getYaw() + ":" + spawnLocation.getPitch();

        Document basicDBObject = new Document("_id", player.getUniqueId().toString())
                .append("name", player.getName())
                .append("lastLocation", locationSerialized)
                .append("lastReward", 0L)
                .append("playerhiderState", 0)
                .append("defaultSpawn", true)
                .append("hotbarSound", true);

        this.mongoDB.insertDocumentInCollectionSync(basicDBObject, MongoDBCollection.LOBBY);

        player.sendMessage(this.crimxLobby.getPrefix() + "§7Nutzerdaten wurden erfolgreich angelegt!");

    }

}
