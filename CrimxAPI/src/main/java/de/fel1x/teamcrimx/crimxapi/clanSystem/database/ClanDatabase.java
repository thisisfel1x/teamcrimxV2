package de.fel1x.teamcrimx.crimxapi.clanSystem.database;

import com.mongodb.client.MongoCollection;
import de.fel1x.teamcrimx.crimxapi.CrimxAPI;
import de.fel1x.teamcrimx.crimxapi.database.mongodb.MongoDB;
import org.bson.Document;

public class ClanDatabase {

    private final CrimxAPI crimxAPI;
    private final MongoDB mongoDB;
    private final MongoCollection<Document> clanCollection;

    public ClanDatabase(CrimxAPI crimxAPI) {
        this.crimxAPI = crimxAPI;
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

    public boolean insert(Object object) {

        return true;
    }
}
