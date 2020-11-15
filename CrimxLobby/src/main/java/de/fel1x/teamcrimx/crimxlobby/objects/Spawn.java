package de.fel1x.teamcrimx.crimxlobby.objects;

import de.fel1x.teamcrimx.crimxlobby.CrimxLobby;
import org.bukkit.Location;

public enum Spawn {

    SPAWN(CrimxLobby.getInstance().getSpawnManager().loadLocation("spawn"), null),
    MLGWARS(CrimxLobby.getInstance().getSpawnManager().loadLocation("mlgwars"), null),
    FLOOR_IS_LAVA(CrimxLobby.getInstance().getSpawnManager().loadLocation("floorislava"), null),
    MASTERBUILDERS(CrimxLobby.getInstance().getSpawnManager().loadLocation("masterbuilders"), null),
    BEDWARS(CrimxLobby.getInstance().getSpawnManager().loadLocation("bedwars"), null),
    CAPTURE_THE_FLAG(CrimxLobby.getInstance().getSpawnManager().loadLocation("ctf"), null),
    COMING_SOON(CrimxLobby.getInstance().getSpawnManager().loadLocation("soon"), null);

    private Location playerSpawn;
    private Location npcSpawn;

    Spawn(Location playerSpawn, Location npcSpawn) {
        this.playerSpawn = playerSpawn;
        this.npcSpawn = npcSpawn;
    }

    public Location getPlayerSpawn() {
        return this.playerSpawn;
    }

    public void setPlayerSpawn(Location playerSpawn) {
        this.playerSpawn = playerSpawn;
    }

    public Location getNpcSpawn() {
        return this.npcSpawn;
    }

    public void setNpcSpawn(Location npcSpawn) {
        this.npcSpawn = npcSpawn;
    }
}
