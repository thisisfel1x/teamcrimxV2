package de.fel1x.teamcrimx.crimxapi.database.mongodb;

import com.mongodb.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;

public class MongoDB extends MongoHandler {

    private final MongoClient mongoClient;
    private final MongoDatabase networkDatabase;

    private final MongoCollection<Document> userCollection;
    private final MongoCollection<Document> lobbyCollection;
    private final MongoCollection<Document> mlgWarsCollection;
    private final MongoCollection<Document> captureTheFlagCollection;
    private final MongoCollection<Document> bingoCollection;
    private final MongoCollection<Document> floorIsLavaCollection;
    private final MongoCollection<Document> suggestionsCollection;
    private final MongoCollection<Document> bugCollection;
    private final MongoCollection<Document> clanCollection;
    private final MongoCollection<Document> friendCollection;

    /*
    Constructor for initializing the database.
    This plugin runs on all servers, so by adding it as a dependency to maven you can access this methods easily
     */
    public MongoDB() {
        this.mongoClient = new MongoClient();

        /*
        Network database. Location where all stuff is stored
         */
        this.networkDatabase = this.mongoClient.getDatabase("network");

        /*
        All collections for all kind of stuff like minigames
         */
        this.userCollection = this.networkDatabase.getCollection("users");
        this.lobbyCollection = this.networkDatabase.getCollection("lobby");
        this.mlgWarsCollection = this.networkDatabase.getCollection("mlgwars");
        this.captureTheFlagCollection = this.networkDatabase.getCollection("capturetheflag");
        this.bingoCollection = this.networkDatabase.getCollection("bingo");
        this.floorIsLavaCollection = this.networkDatabase.getCollection("floorislava");
        this.suggestionsCollection = this.networkDatabase.getCollection("suggestions");
        this.bugCollection = this.networkDatabase.getCollection("bug");
        this.clanCollection = this.networkDatabase.getCollection("clan");
        this.friendCollection = this.networkDatabase.getCollection("friends");

    }

    public MongoClient getMongoClient() {
        return this.mongoClient;
    }

    public MongoDatabase getNetworkDatabase() {
        return this.networkDatabase;
    }

    public MongoCollection<Document> getUserCollection() {
        return this.userCollection;
    }

    public MongoCollection<Document> getLobbyCollection() {
        return this.lobbyCollection;
    }

    public MongoCollection<Document> getMlgWarsCollection() {
        return this.mlgWarsCollection;
    }

    public MongoCollection<Document> getCaptureTheFlagCollection() {
        return this.captureTheFlagCollection;
    }

    public MongoCollection<Document> getBingoCollection() {
        return this.bingoCollection;
    }

    public MongoCollection<Document> getFloorIsLavaCollection() {
        return this.floorIsLavaCollection;
    }

    public MongoCollection<Document> getSuggestionsCollection() {
        return this.suggestionsCollection;
    }

    public MongoCollection<Document> getBugCollection() {
        return this.bugCollection;
    }

    public MongoCollection<Document> getClanCollection() {
        return this.clanCollection;
    }

    public MongoCollection<Document> getFriendCollection() {
        return this.friendCollection;
    }
}
