package de.fel1x.teamcrimx.crimxlobby.objects;

import de.fel1x.teamcrimx.crimxlobby.CrimxLobby;
import org.bukkit.Location;

public enum Spawn {

    // SPAWN & NPC
    SPAWN(null),
    SHOP_NPC(null),
    PROFILE_NPC(null),

    // MINIGAMES
    MLGWARS(null),
    FLOOR_IS_LAVA(null),
    BINGO(null),
    CAPTURE_THE_FLAG(null);

    private Location playerSpawn;

    Spawn(Location playerSpawn) {
        this.playerSpawn = playerSpawn;
    }

    public Location getSpawn() {
        return this.playerSpawn;
    }

    public void setPlayerSpawn(Location playerSpawn) {
        this.playerSpawn = playerSpawn;
    }

}
