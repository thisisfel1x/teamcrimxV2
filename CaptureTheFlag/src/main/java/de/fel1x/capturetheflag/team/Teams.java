package de.fel1x.capturetheflag.team;

import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public enum Teams {

    NONE("§fnull", new ArrayList<>()),

    RED("§cRot", new ArrayList<>()),
    BLUE("§9Blau", new ArrayList<>());

    private String teamName;
    private List<Player> teamPlayers;

    Teams(String teamName, List<Player> teamPlayers) {
        this.teamName = teamName;
        this.teamPlayers = teamPlayers;
    }

    public List<Player> getTeamPlayers() {
        return teamPlayers;
    }

    public String getTeamName() {
        return teamName;
    }
}
