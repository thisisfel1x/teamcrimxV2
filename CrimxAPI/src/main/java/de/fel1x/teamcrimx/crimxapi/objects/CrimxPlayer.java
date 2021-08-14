package de.fel1x.teamcrimx.crimxapi.objects;

import com.destroystokyo.paper.profile.PlayerProfile;
import com.destroystokyo.paper.profile.ProfileProperty;
import de.dytanic.cloudnet.ext.bridge.player.ICloudPlayer;
import de.fel1x.teamcrimx.crimxapi.CrimxAPI;
import de.fel1x.teamcrimx.crimxapi.cosmetic.database.CosmeticPlayer;
import de.fel1x.teamcrimx.crimxapi.database.mongodb.MongoDB;
import de.fel1x.teamcrimx.crimxapi.database.mongodb.MongoDBCollection;
import de.fel1x.teamcrimx.crimxapi.friends.database.FriendPlayer;
import net.kyori.adventure.text.Component;
import net.md_5.bungee.api.ChatColor;
import org.bson.Document;
import org.bukkit.entity.Player;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

public class CrimxPlayer {

    private final CrimxAPI crimxAPI = CrimxAPI.getInstance();
    private final MongoDB mongoDB = this.crimxAPI.getMongoDB();

    private final ICloudPlayer cloudPlayer;

    public CrimxPlayer(UUID playerUniqueId) {
        this.cloudPlayer = this.crimxAPI.getPlayerManager().getOnlinePlayer(playerUniqueId);
    }

    public CrimxPlayer(ICloudPlayer cloudPlayer) {
        this.cloudPlayer = cloudPlayer;
    }

    public CrimxPlayer(Player player) {
        this.cloudPlayer = this.crimxAPI.getPlayerManager().getOnlinePlayer(player.getUniqueId());
    }

    /*
    This method stores all important information for a player into the user collection
     */
    public void createPlayerData(Player player) {
        player.sendMessage(this.crimxAPI.getPrefix() + "§7Nutzerdaten werden angelegt...");

        String[] skin = this.getPlayerSkinFromDatabase(player);

        String skinSignature = "";
        String skinTexture = "";

        if (skin != null) {
            skinSignature = skin[0];
            skinTexture = skin[1];
        } else {
            player.sendMessage(this.crimxAPI.getPrefix() + "§cDein Skin konnte nicht gespeichert werden. " +
                    "Bitte versuche es später erneut");
        }

        Document playerDocument = new Document("_id", this.cloudPlayer.getUniqueId().toString())
                .append("name", this.cloudPlayer.getName())
                .append("coins", 0)
                .append("firstJoin", System.currentTimeMillis())
                .append("lastJoin", System.currentTimeMillis())
                .append("onlinetime", 0L)
                .append("lastJoinMe", 0L)
                .append("currentClan", null)
                .append("skinSignature", skinSignature)
                .append("skinTexture", skinTexture)
                .append("nick", false);

        if (this.mongoDB.insertDocumentInCollectionSync(playerDocument, MongoDBCollection.USERS)) {
            player.sendMessage(this.crimxAPI.getPrefix() + "§7Nutzerdaten wurden erfolgreich angelegt!");
        } else {
            player.kick(Component.text(this.crimxAPI.getPrefix() + "§cEin Fehler ist aufgetreten"));
        }

    }

    /*
    This method updates a players skin and name if changed
    Also checks if the player is already saved the user collection (-> first join)
     */
    public void updateUserDataIfNecessary(Player player) {
        if (!this.mongoDB.checkIfDocumentExistsSync(player.getUniqueId(), MongoDBCollection.USERS)) {
            this.createPlayerData(player);
            return;
        }

        new CosmeticPlayer(player.getUniqueId()).updateCosmeticList();
        new FriendPlayer(player.getUniqueId()).createPlayerData();

        Document found = this.mongoDB.getDocumentSync(this.cloudPlayer.getUniqueId(), MongoDBCollection.USERS);

        if (found == null) {
            this.createPlayerData(player);
            return;
        }

        Document updateDocument = new Document();

        if (!found.getString("name").equalsIgnoreCase(player.getName())) {
            updateDocument.append("name", player.getName());
        }

        String[] skin = this.getPlayerSkinFromDatabase(player);

        if (!found.getString("skinSignature").equalsIgnoreCase(skin[0])) {
            updateDocument.append("skinSignature", skin[0]);
        }

        if (!found.getString("skinTexture").equalsIgnoreCase(skin[1])) {
            updateDocument.append("skinTexture", skin[1]);
        }

        if (!updateDocument.isEmpty()) {
            this.mongoDB.updateDocumentInCollectionSync(player.getUniqueId(), MongoDBCollection.USERS, updateDocument);
        }
    }

    /**
     * Get a players skin (signature & texture)
     * String[0] -> Signature
     * String[1] -> Value
     *
     * @param player - Player
     * @return String[]
     */
    public String[] getPlayerSkinFromDatabase(Player player) {

        PlayerProfile playerProfile = player.getPlayerProfile();

        if (playerProfile.hasTextures()) {
            List<ProfileProperty> properties = playerProfile.getProperties().stream()
                    .filter(profileProperty -> profileProperty.getName().equalsIgnoreCase("textures"))
                    .collect(Collectors.toList());

            return new String[]{
                    properties.get(0).getSignature(), properties.get(0).getValue()
            };
        }

        return null;
    }


    /*
    With this method you can get the skin signature and value out of the database
     */
    public String[] getPlayerSkinFromDatabase() {
        Document found = this.crimxAPI.getMongoDB().getUserCollection().
                find(new Document("_id", this.cloudPlayer.getUniqueId().toString())).first();

        assert found != null;

        String value = found.getString("skinTexture");
        String signature = found.getString("skinSignature");

        return new String[]{value, signature};
    }

    /*
    This method returns a String[] for displaying the players skin in chat
     */
    public String[] getPlayerSkinForChat(UUID playerUniqueID, int scale) {
        String[] toReturn = new String[scale];
        String urlString = "https://crafatar.com/avatars/"
                + playerUniqueID.toString() + "?size=8";

        try {
            URL url = new URL(urlString);
            BufferedImage image = ImageIO.read(url);
            for (int i = 0; i < image.getHeight(); i++) {
                StringBuilder chatHeadString = new StringBuilder();
                for (int j = 0; j < image.getWidth(); j++) {
                    Color color = new Color(image.getRGB(j, i));

                    ChatColor chatColor = ChatColor.of(color);
                    chatHeadString.append(chatColor).append("⬛");

                }
                toReturn[i] = chatHeadString.toString();
            }
        } catch (IOException ignored) {
            return null;
        }
        return toReturn;
    }
}
