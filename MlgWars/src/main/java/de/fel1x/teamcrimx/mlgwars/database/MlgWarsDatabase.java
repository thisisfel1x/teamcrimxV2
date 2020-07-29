package de.fel1x.teamcrimx.mlgwars.database;

import de.fel1x.teamcrimx.crimxapi.CrimxAPI;
import de.fel1x.teamcrimx.crimxapi.database.mongodb.MongoDB;
import de.fel1x.teamcrimx.mlgwars.MlgWars;
import org.bson.Document;
import org.bukkit.entity.Player;

public class MlgWarsDatabase {

    MlgWars mlgWars = MlgWars.getInstance();
    CrimxAPI crimxAPI = this.mlgWars.getCrimxAPI();
    MongoDB mongoDB = this.crimxAPI.getMongoDB();

    public void createPlayerData(Player player) {

        player.sendMessage(this.mlgWars.getPrefix() + "§7Nutzerdaten werden angelegt...");

        Document basicDBObject = new Document("_id", player.getUniqueId().toString())
                .append("name", player.getName())
                .append("kills", 0)
                .append("deaths", 0)
                .append("games-played", 0)
                .append("games-won", 0);

        this.mongoDB.getMlgWarsCollection().insertOne(basicDBObject);

        player.sendMessage(this.mlgWars.getPrefix() + "§7Nutzerdaten wurden erfolgreich angelegt!");

    }
}
