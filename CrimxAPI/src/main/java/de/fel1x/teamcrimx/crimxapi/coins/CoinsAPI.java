package de.fel1x.teamcrimx.crimxapi.coins;


import de.fel1x.teamcrimx.crimxapi.CrimxAPI;
import org.bson.Document;
import org.bson.conversions.Bson;

import java.util.UUID;

/**
 * Old coins api
 * Use {@link de.fel1x.teamcrimx.crimxapi.coins.ICrimxCoins} for better performance
 */
@Deprecated
public class CoinsAPI {

    private final CrimxAPI crimxAPI = new CrimxAPI();
    private final Document playerNetworkDocument;

    public CoinsAPI(UUID uuid) {
        this.playerNetworkDocument = this.crimxAPI.getMongoDB().getUserCollection().
                find(new Document("_id", uuid.toString())).first();

    }

    @Deprecated
    public int getCoins() {
        return this.playerNetworkDocument.getInteger("coins");
    }

    @Deprecated
    public void setCoins(int coins) {

        if (coins < 0) {
            coins = 0;
        }

        Document document = new Document("coins", coins);
        Bson updateOperation = new Document("$set", document);
        this.crimxAPI.getMongoDB().getUserCollection().updateOne(this.playerNetworkDocument, updateOperation);
    }

    @Deprecated
    public void addCoins(int coins) {

        coins = this.getCoins() + coins;

        if (coins < 0) {
            coins = 0;
        }

        Document document = new Document("coins", coins);
        Bson updateOperation = new Document("$set", document);
        this.crimxAPI.getMongoDB().getUserCollection().updateOne(this.playerNetworkDocument, updateOperation);
    }

    @Deprecated
    public void removeCoins(int coins) {

        coins = this.getCoins() - coins;

        if (coins < 0) {
            coins = 0;
        }

        Document document = new Document("coins", coins);
        Bson updateOperation = new Document("$set", document);
        this.crimxAPI.getMongoDB().getUserCollection().updateOne(this.playerNetworkDocument, updateOperation);
    }

}
