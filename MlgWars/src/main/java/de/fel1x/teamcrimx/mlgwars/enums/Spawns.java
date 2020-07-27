package de.fel1x.teamcrimx.mlgwars.enums;

import org.bukkit.Location;

public enum Spawns {

    LOBBY(null),
    SPECTATOR(null),
    LOC_1(null),
    LOC_2(null),
    MIDDLE_1(null),
    MIDDLE_2(null);

    private Location location;

    Spawns(Location location) {
        this.location = location;
    }

    public Location getLocation() {
        return location;
    }

    public void setLocation(Location location) {
        this.location = location;
    }
}
