package de.fel1x.teamcrimx.mlgwars.maphandler.gametype.types;

import de.fel1x.teamcrimx.mlgwars.MlgWars;

public class TeamGameType extends SoloGameType {

    public TeamGameType(MlgWars mlgWars) {
        super(mlgWars);
    }

    @Override
    public String getGameTypeName() {
        return "Team";
    }
}
