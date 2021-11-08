package de.fel1x.teamcrimx.mlgwars.objects;

import de.fel1x.teamcrimx.mlgwars.enums.Size;

public class Map {

    private final String mapName;
    private final String mapBuilder;
    private final Size size;

    public Map(String mapName, String mapBuilder, Size size) {
        this.mapName = mapName;
        this.mapBuilder = mapBuilder;
        this.size = size;
    }

    public String getMapName() {
        return this.mapName;
    }

    public String getMapBuilder() {
        return this.mapBuilder;
    }

    public Size getSize() {
        return this.size;
    }
}
