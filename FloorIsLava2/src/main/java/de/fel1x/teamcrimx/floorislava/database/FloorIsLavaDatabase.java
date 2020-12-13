package de.fel1x.teamcrimx.floorislava.database;

import de.fel1x.teamcrimx.crimxapi.CrimxAPI;
import de.fel1x.teamcrimx.crimxapi.database.mongodb.MongoDB;
import de.fel1x.teamcrimx.floorislava.FloorIsLava;
import org.bson.Document;
import org.bukkit.entity.Player;

public class FloorIsLavaDatabase {
    final FloorIsLava floorIsLava = FloorIsLava.getInstance();

    final CrimxAPI crimxAPI = this.floorIsLava.getCrimxAPI();

    final MongoDB mongoDB = this.crimxAPI.getMongoDB();

    public void createPlayerData(Player player) {
        player.sendMessage(this.floorIsLava.getPrefix() + "§7Nutzerdaten werden angelegt...");
        Document basicDBObject = (new Document("_id", player.getUniqueId().toString())).append("name", player.getName()).append("kills", 0).append("deaths", 0).append("gamesPlayed", 0).append("gamesWon", 0);
        this.mongoDB.getFloorIsLavaCollection().insertOne(basicDBObject);
        player.sendMessage(this.floorIsLava.getPrefix() + "§7Nutzerdaten wurden erfolgreich angelegt!");
    }
}
