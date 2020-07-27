package de.fel1x.teamcrimx.crimxlobby.database;

import de.fel1x.teamcrimx.crimxapi.CrimxAPI;
import de.fel1x.teamcrimx.crimxapi.database.mongodb.MongoDB;
import de.fel1x.teamcrimx.crimxlobby.CrimxLobby;
import de.fel1x.teamcrimx.crimxlobby.cosmetics.Cosmetic;
import de.fel1x.teamcrimx.crimxlobby.objects.Spawn;
import org.bson.Document;
import org.bukkit.entity.Player;

public class LobbyDatabase {

    CrimxLobby crimxLobby = CrimxLobby.getInstance();
    CrimxAPI crimxAPI = this.crimxLobby.getCrimxAPI();
    MongoDB mongoDB = this.crimxAPI.getMongoDB();

    public void createPlayerData(Player player) {

        player.sendMessage(this.crimxLobby.getPrefix() + "§7Nutzerdaten werden angelegt...");

        Document basicDBObject = new Document("_id", player.getUniqueId().toString())
                .append("name", player.getName())
                .append("last-location-world", Spawn.SPAWN.getPlayerSpawn().getWorld().getName())
                .append("last-location-x", Spawn.SPAWN.getPlayerSpawn().getX())
                .append("last-location-y", Spawn.SPAWN.getPlayerSpawn().getY())
                .append("last-location-z", Spawn.SPAWN.getPlayerSpawn().getZ())
                .append("last-location-pitch", Spawn.SPAWN.getPlayerSpawn().getPitch())
                .append("last-location-yaw", Spawn.SPAWN.getPlayerSpawn().getYaw())
                .append("last-reward", 0L)
                .append("playerhider-state", 0)
                .append("defaultSpawn", true)
                .append("hotbarSound", true)
                .append("discord-tag", "")
                .append("twitter", "")
                .append("instagram", "");

        for (Cosmetic cosmetic : Cosmetic.values()) {
            basicDBObject.append(cosmetic.name(), false);
        }

        this.mongoDB.getLobbyCollection().insertOne(basicDBObject);

        player.sendMessage(this.crimxLobby.getPrefix() + "§7Nutzerdaten wurden erfolgreich angelegt!");

    }

}
