package de.fel1x.capturetheflag.database;

import de.fel1x.capturetheflag.CaptureTheFlag;
import de.fel1x.capturetheflag.kit.Kit;
import de.fel1x.teamcrimx.crimxapi.CrimxAPI;
import de.fel1x.teamcrimx.crimxapi.database.mongodb.MongoDB;
import org.bson.Document;
import org.bukkit.entity.Player;

public class CaptureTheFlagDatabase {

    CaptureTheFlag captureTheFlag = CaptureTheFlag.getInstance();
    CrimxAPI crimxAPI = this.captureTheFlag.getCrimxAPI();
    MongoDB mongoDB = this.crimxAPI.getMongoDB();

    public void createPlayerData(Player player) {

        player.sendMessage(this.captureTheFlag.getPrefix() + "§7Nutzerdaten werden angelegt...");

        Document basicDBObject = new Document("_id", player.getUniqueId().toString())
                .append("name", player.getName())
                .append("kills", 0)
                .append("deaths", 0)
                .append("gamesPlayed", 0)
                .append("gamesWon", 0)
                .append("selectedKit", Kit.NONE.name());

        this.mongoDB.getCaptureTheFlagCollection().insertOne(basicDBObject);

        player.sendMessage(this.captureTheFlag.getPrefix() + "§7Nutzerdaten wurden erfolgreich angelegt!");

    }

}
