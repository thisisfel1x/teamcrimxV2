package de.fel1x.bingo.database;

import de.fel1x.bingo.Bingo;
import de.fel1x.teamcrimx.crimxapi.CrimxAPI;
import de.fel1x.teamcrimx.crimxapi.database.mongodb.MongoDB;
import org.bson.Document;
import org.bukkit.entity.Player;

public class BingoDatabase {

    Bingo bingo = Bingo.getInstance();
    CrimxAPI crimxAPI = this.bingo.getCrimxAPI();
    MongoDB mongoDB = this.crimxAPI.getMongoDB();

    public void createPlayerData(Player player) {

        player.sendMessage(this.bingo.getPrefix() + "§7Nutzerdaten werden angelegt...");

        Document basicDBObject = new Document("_id", player.getUniqueId().toString())
                .append("name", player.getName())
                .append("itemsPickedUp", 0)
                .append("itemsCrafted", 0)
                .append("gamesPlayed", 0)
                .append("gamesWon", 0);

        this.mongoDB.getBingoCollection().insertOne(basicDBObject);

        player.sendMessage(this.bingo.getPrefix() + "§7Nutzerdaten wurden erfolgreich angelegt!");

    }

}
