package de.fel1x.teamcrimx.crimxapi.database.mongodb;

public enum MongoDBCollection {

    USERS("users"),
    LOBBY("lobby"),
    MLGWARS("mlgwars"),
    CAPTURE_THE_FLAG("capturetheflag"),
    BINGO("bingo");

    String name;

    MongoDBCollection(String name) {
        this.name = name;
    }

    public String getName() {
        return this.name;
    }
}
