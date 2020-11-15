package de.fel1x.teamcrimx.crimxlobby.database;

public class LobbyDatabasePlayer {

    private boolean hotbarSoundEnabled;
    private boolean spawnAtLastLocation;
    private long lastReward;

    public LobbyDatabasePlayer(boolean hotbarSoundEnabled, boolean spawnAtLastLocation, long lastReward) {
        this.hotbarSoundEnabled = hotbarSoundEnabled;
        this.spawnAtLastLocation = spawnAtLastLocation;
        this.lastReward = lastReward;
    }

    public boolean isHotbarSoundEnabled() {
        return this.hotbarSoundEnabled;
    }

    public void setHotbarSoundEnabled(boolean hotbarSoundEnabled) {
        this.hotbarSoundEnabled = hotbarSoundEnabled;
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
