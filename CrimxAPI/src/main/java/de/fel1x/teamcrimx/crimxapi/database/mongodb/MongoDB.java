package de.fel1x.teamcrimx.crimxapi.database.mongodb;

import com.mongodb.MongoClient;
import com.mongodb.MongoClientOptions;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;

public class MongoDB {

    private MongoClient mongoClient;
    private MongoDatabase networkDatabase;

    private MongoCollection<Document> userCollection;
    private MongoCollection<Document> lobbyCollection;
    private MongoCollection<Document> mlgWarsCollection;

    public MongoDB() {

        this.mongoClient = new MongoClient();

        this.networkDatabase = this.mongoClient.getDatabase("network");

        this.userCollection = this.networkDatabase.getCollection("users");
        this.lobbyCollection = this.networkDatabase.getCollection("lobby");
        this.mlgWarsCollection = this.networkDatabase.getCollection("mlgwars");

    }

    public MongoClient getMongoClient() {
        return mongoClient;
    }

    public MongoDatabase getNetworkDatabase() {
        return networkDatabase;
    }

    public MongoCollection<Document> getUserCollection() {
        return userCollection;
    }

    public MongoCollection<Document> getLobbyCollection() {
        return lobbyCollection;
    }

    public MongoCollection<Document> getMlgWarsCollection() {
        return mlgWarsCollection;
    }
}
