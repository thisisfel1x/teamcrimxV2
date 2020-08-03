package de.fel1x.teamcrimx.mlgwars.database;

import de.fel1x.teamcrimx.crimxapi.CrimxAPI;
import de.fel1x.teamcrimx.crimxapi.database.mongodb.MongoDB;
import de.fel1x.teamcrimx.mlgwars.MlgWars;
import de.fel1x.teamcrimx.mlgwars.kit.Kit;
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
                .append("gamesPlayed", 0)
                .append("gamesWon", 0)
                .append("selectedKit", Kit.STARTER.name());

       if(player.hasPermission("mlgwars.kits")) {
           for (Kit kit : Kit.values()) {
               basicDBObject.append(kit.name(), true);
           }
       } else {
           for (Kit kit : Kit.values()) {
               basicDBObject.append(kit.name(), (kit == Kit.STARTER));
           }
       }

        this.mongoDB.getMlgWarsCollection().insertOne(basicDBObject);

        player.sendMessage(this.mlgWars.getPrefix() + "§7Nutzerdaten wurden erfolgreich angelegt!");

    }
}
