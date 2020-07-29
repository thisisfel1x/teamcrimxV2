package de.fel1x.teamcrimx.crimxapi.objects;

import de.dytanic.cloudnet.driver.CloudNetDriver;
import de.dytanic.cloudnet.ext.bridge.player.ICloudPlayer;
import de.dytanic.cloudnet.ext.bridge.player.IPlayerManager;
import de.fel1x.teamcrimx.crimxapi.CrimxAPI;
import de.fel1x.teamcrimx.crimxapi.database.mongodb.MongoDBCollection;
import de.fel1x.teamcrimx.crimxapi.utils.Skin;
import org.bson.Document;
import org.bson.conversions.Bson;

import javax.annotation.Nullable;
import java.util.Arrays;
import java.util.UUID;

public class CrimxPlayer {

    private final CrimxAPI crimxAPI = CrimxAPI.getInstance();

    private final IPlayerManager playerManager = CloudNetDriver.getInstance().getServicesRegistry().getFirstService(IPlayerManager.class);
    ICloudPlayer cloudPlayer;

    public CrimxPlayer(ICloudPlayer cloudPlayer) {
        this.cloudPlayer = cloudPlayer;
    }

    public boolean checkIfPlayerExistsInCollection(UUID uuid) {
        return this.checkIfPlayerExistsInCollection(uuid, null);
    }

    public boolean checkIfPlayerExistsInCollection(UUID uuid, @Nullable MongoDBCollection mongoDBCollection) {
        if (mongoDBCollection == null) {
            return Arrays.stream(MongoDBCollection.values()).allMatch(collection -> this.crimxAPI.getMongoDB().getNetworkDatabase().getCollection(collection.getName()).countDocuments(new Document("_id", uuid.toString())) > 0);
        } else {
            return this.crimxAPI.getMongoDB().getNetworkDatabase().getCollection(mongoDBCollection.getName()).countDocuments(new Document("_id", uuid.toString())) > 0;
        }
    }

    public void createPlayerData() {

        this.sendMessage(this.crimxAPI.getPrefix() + "§7Nutzerdaten werden angelegt...");

        String[] skin = Skin.getSkinFromName(this.cloudPlayer.getName());

        if (skin == null) {
            this.sendMessage(this.crimxAPI.getPrefix() + "§cEin Fehler ist aufgetreten! §4(ERROR: SKIN_TIMEOUT)");
            return;
        }

        Document basicDBObject = new Document("_id", this.cloudPlayer.getUniqueId().toString())
                .append("name", this.cloudPlayer.getName())
                .append("coins", 0)
                .append("firstJoin", System.currentTimeMillis())
                .append("lastJoin", System.currentTimeMillis())
                .append("onlinetime", 0L)
                .append("skin-texture", skin[0])
                .append("skin-signature", skin[1]);

        this.crimxAPI.getMongoDB().getUserCollection().insertOne(basicDBObject);

        this.sendMessage(this.crimxAPI.getPrefix() + "§7Nutzerdaten wurden erfolgreich angelegt!");

    }

    public void updateUserData() {

        Document found = this.crimxAPI.getMongoDB().getUserCollection().
                find(new Document("_id", this.cloudPlayer.getUniqueId().toString())).first();

        Bson updatedValue = new Document("lastJoin", System.currentTimeMillis());
        Bson updateOperation = new Document("$set", updatedValue);
        this.crimxAPI.getMongoDB().getUserCollection().updateOne(found, updateOperation);

    }

    public void sendMessage(String message) {

        this.cloudPlayer.getPlayerExecutor().sendChatMessage(message);

    }

    public String[] getPlayerSkin() {

        Document found = this.crimxAPI.getMongoDB().getUserCollection().
                find(new Document("_id", this.cloudPlayer.getUniqueId().toString())).first();

        assert found != null;

        String value = found.getString("skin-texture");
        String signature = found.getString("skin-signature");

        return new String[]{value, signature};

    }


}
