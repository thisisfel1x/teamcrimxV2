package de.fel1x.teamcrimx.crimxapi.objects;

import de.dytanic.cloudnet.ext.bridge.player.ICloudPlayer;
import de.fel1x.teamcrimx.crimxapi.CrimxAPI;
import de.fel1x.teamcrimx.crimxapi.database.mongodb.MongoDBCollection;
import de.fel1x.teamcrimx.crimxapi.utils.ColorUtil;
import de.fel1x.teamcrimx.crimxapi.utils.Skin;
import net.md_5.bungee.api.chat.TextComponent;
import org.bson.Document;
import org.bson.conversions.Bson;
import org.bukkit.ChatColor;

import javax.annotation.Nullable;
import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URL;
import java.util.Arrays;
import java.util.UUID;

public class CrimxPlayer {

    private final CrimxAPI crimxAPI = CrimxAPI.getInstance();

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
                .append("lastJoinMe", 0L)
                .append("currentClan", null)
                .append("skinTexture", skin[0])
                .append("skinSignature", skin[1]);

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

        String value = found.getString("skinTexture");
        String signature = found.getString("skinSignature");

        return new String[]{value, signature};

    }

    public String[] getPlayerSkinForChat(String user, int scale) {

        String[] toReturn = new String[scale];
        String urlString = "https://minotar.net/avatar/" + user + "/" + scale + ".png";

        try {
            URL url = new URL(urlString);
            BufferedImage image = ImageIO.read(url);
            for (int i = 0; i < image.getHeight(); i++) {
                StringBuilder chatHeadString = new StringBuilder();
                for (int j = 0; j < image.getWidth(); j++) {
                    Color color = new Color(image.getRGB(j, i));
                    ChatColor chatColor = ColorUtil.fromRGB(color.getRed(), color.getGreen(), color.getBlue());
                    TextComponent textComponent = new TextComponent("#" + color.getRGB());
                    chatHeadString.append(textComponent.toString()).append("⬛");
                }
                toReturn[i] = chatHeadString.toString();
            }
        } catch (IOException ignored) {
            return null;
        }
        return toReturn;
    }
}
