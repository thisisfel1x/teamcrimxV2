package de.fel1x.teamcrimx.crimxapi.clanSystem.database;

import com.mongodb.client.MongoCollection;
import de.fel1x.teamcrimx.crimxapi.CrimxAPI;
import de.fel1x.teamcrimx.crimxapi.database.mongodb.MongoDB;
import de.fel1x.teamcrimx.crimxapi.support.CrimxSpigotAPI;
import org.bson.Document;
import org.bukkit.Bukkit;

import java.util.stream.StreamSupport;

public class ClanDatabase {

    private final CrimxAPI crimxAPI;
    private final CrimxSpigotAPI crimxSpigotAPI;

    private final MongoDB mongoDB;
    private final MongoCollection<Document> clanCollection;

    public ClanDatabase(CrimxAPI crimxAPI) {
        this.crimxAPI = crimxAPI;
        this.crimxSpigotAPI = CrimxSpigotAPI.getInstance();

        this.mongoDB = this.crimxAPI.getMongoDB();
        this.clanCollection = this.mongoDB.getClanCollection();
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

    public void insertAsync(Document document) {
        Bukkit.getScheduler().runTaskAsynchronously(this.crimxSpigotAPI, () -> this.clanCollection.insertOne(document));
    }

    public boolean clanTagAlreadyTaken(String clanTag) {
        return StreamSupport.stream(this.clanCollection.find().spliterator(), false).anyMatch(document -> document.getString("clanTag").equalsIgnoreCase(clanTag));
    }
}