package de.fel1x.teamcrimx.mlgwars.maphandler.gametype.types;

import de.fel1x.teamcrimx.mlgwars.MlgWars;
import de.fel1x.teamcrimx.mlgwars.maphandler.gametype.GameType;

public class SoloGameType extends GameType {

    public SoloGameType(MlgWars mlgWars) {
        super(mlgWars);
    }

    @Override
    public String getGameTypeName() {
        return "Solo";
    }

}
