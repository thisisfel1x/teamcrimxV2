package de.fel1x.teamcrimx.crimxapi.database.mongodb;

public enum MongoDBCollection {

    USERS("users"),
    LOBBY("lobby"),
    MLGWARS("mlgwars"),
    CAPTURE_THE_FLAG("capturetheflag"),
    BINGO("bingo"),
    FLOOR_IS_LAVA("floorislava"),
    SUGGESTIONS("suggestions"),
    BUGS("bugs"),
    CLAN("clan"),
    FRIENDS("friends"),
    COSMETIC("cosmetics"),
    STATISTICS("statistics");

    private final String collectionName;

    MongoDBCollection(String name) {
        this.collectionName = name;
    }

    public String getCollectionName() {
        return this.collectionName;
    }
}
