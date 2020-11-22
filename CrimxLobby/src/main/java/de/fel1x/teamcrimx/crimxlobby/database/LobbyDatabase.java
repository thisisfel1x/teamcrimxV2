package de.fel1x.teamcrimx.crimxlobby.database;

import de.fel1x.teamcrimx.crimxapi.CrimxAPI;
import de.fel1x.teamcrimx.crimxapi.database.mongodb.MongoDB;
import de.fel1x.teamcrimx.crimxlobby.CrimxLobby;
import de.fel1x.teamcrimx.crimxlobby.cosmetics.Cosmetic;
import de.fel1x.teamcrimx.crimxlobby.objects.Spawn;
import org.bson.Document;
import org.bukkit.entity.Player;

public class LobbyDatabase {

    final CrimxLobby crimxLobby = CrimxLobby.getInstance();
    final CrimxAPI crimxAPI = this.crimxLobby.getCrimxAPI();
    final MongoDB mongoDB = this.crimxAPI.getMongoDB();

    public void createPlayerData(Player player) {

        player.sendMessage(this.crimxLobby.getPrefix() + "§7Nutzerdaten werden angelegt...");

        Document basicDBObject = new Document("_id", player.getUniqueId().toString())
                .append("name", player.getName())
                .append("lastLocationWorld", Spawn.SPAWN.getPlayerSpawn().getWorld().getName())
                .append("lastLocationX", Spawn.SPAWN.getPlayerSpawn().getX())
                .append("lastLocationY", Spawn.SPAWN.getPlayerSpawn().getY())
                .append("lastLocationZ", Spawn.SPAWN.getPlayerSpawn().getZ())
                .append("lastLocationPitch", Spawn.SPAWN.getPlayerSpawn().getPitch())
                .append("lastLocationYaw", Spawn.SPAWN.getPlayerSpawn().getYaw())
                .append("lastReward", 0L)
                .append("playerhiderState", 0)
                .append("defaultSpawn", true)
                .append("hotbarSound", true)
                .append("discordTag", "")
                .append("twitter", "")
                .append("instagram", "")
                .append("selectedCosmetic", null);

        for (Cosmetic cosmetic : Cosmetic.values()) {
            basicDBObject.append(cosmetic.name(), false);
        }

        this.mongoDB.getLobbyCollection().insertOne(basicDBObject);

        player.sendMessage(this.crimxLobby.getPrefix() + "§7Nutzerdaten wurden erfolgreich angelegt!");

    }

}
