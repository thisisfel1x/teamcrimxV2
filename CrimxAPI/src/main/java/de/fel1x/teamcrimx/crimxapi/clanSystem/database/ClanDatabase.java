package de.fel1x.teamcrimx.crimxapi.clanSystem.database;

import com.mongodb.client.MongoCollection;
import de.fel1x.teamcrimx.crimxapi.CrimxAPI;
import de.fel1x.teamcrimx.crimxapi.database.mongodb.MongoDB;
import de.fel1x.teamcrimx.crimxapi.database.mongodb.MongoDBCollection;
import de.fel1x.teamcrimx.crimxapi.support.CrimxSpigotAPI;
import org.bson.Document;
import org.bson.conversions.Bson;
import org.bukkit.Bukkit;

import java.util.UUID;
import java.util.stream.StreamSupport;

public class ClanDatabase {

    private final CrimxAPI crimxAPI;
    private final CrimxSpigotAPI crimxSpigotAPI;

    private final MongoDB mongoDB;

    private final MongoCollection<Document> clanCollection;
    private final MongoCollection<Document> userCollection;

    public ClanDatabase(CrimxAPI crimxAPI) {
        this.crimxAPI = crimxAPI;
        this.crimxSpigotAPI = CrimxSpigotAPI.getInstance();

        this.mongoDB = this.crimxAPI.getMongoDB();

        this.clanCollection = this.mongoDB.getClanCollection();
        this.userCollection = this.mongoDB.getUserCollection();
    }

    public CrimxAPI getCrimxAPI() {
        return this.crimxAPI;
    }

    public MongoDB getMongoDB() {
        return this.mongoDB;
    }

    public MongoCollection<Document> getClanCollection() {
        return this.clanCollection;
    }

    public MongoCollection<Document> getUserCollection() {
        return this.userCollection;
    }

    public void insertAsyncInClanCollection(Document document) {
        Bukkit.getScheduler().runTaskAsynchronously(this.crimxSpigotAPI, () -> this.clanCollection.insertOne(document));
    }

    public void insertAsyncInUserCollection(Document document) {
        Bukkit.getScheduler().runTaskAsynchronously(this.crimxSpigotAPI, () -> this.userCollection.insertOne(document));
    }

    public boolean clanTagAlreadyTaken(String clanTag) {
        return StreamSupport.stream(this.clanCollection.find().spliterator(), false).anyMatch(document -> document.getString("clanTag").equalsIgnoreCase(clanTag));
    }

    public Object getObject(String key, MongoCollection<Document> mongoCollection, UUID uuid) {
        Document found = mongoCollection.find(new Document("_id", uuid.toString())).first();
        return found != null ? found.get(key) : null;
    }

    public boolean insertAsyncInUserCollection(String key, String value, Document playerDocument, MongoDBCollection mongoDBCollection) {
        Bson updateOperation = new Document("$set", new Document(key, value));
        if(mongoDBCollection == MongoDBCollection.CLAN) {
            Bukkit.getScheduler().runTaskAsynchronously(this.crimxSpigotAPI, () -> this.clanCollection.updateOne(playerDocument, updateOperation));
            return true;
        } else if(mongoDBCollection == MongoDBCollection.USERS) {
            Bukkit.getScheduler().runTaskAsynchronously(this.crimxSpigotAPI, () -> this.userCollection.updateOne(playerDocument, updateOperation));
            return true;
        } else {
            return false;
        }
    }
}