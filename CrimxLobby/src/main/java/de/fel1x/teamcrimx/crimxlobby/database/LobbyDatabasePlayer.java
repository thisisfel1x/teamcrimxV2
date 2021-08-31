package de.fel1x.teamcrimx.crimxlobby.database;

public class LobbyDatabasePlayer {

    private boolean spawnAtLastLocation;
    private long lastReward;

    public LobbyDatabasePlayer(boolean spawnAtLastLocation, long lastReward) {
        this.spawnAtLastLocation = spawnAtLastLocation;
        this.lastReward = lastReward;
    }

    public boolean isSpawnAtLastLocation() {
        return this.spawnAtLastLocation;
    }

    public void setSpawnAtLastLocation(boolean spawnAtLastLocation) {
        this.spawnAtLastLocation = spawnAtLastLocation;
    }

    public long getLastReward() {
        return this.lastReward;
    }

    public void setLastReward(long lastReward) {
        this.lastReward = lastReward;
    }
}
