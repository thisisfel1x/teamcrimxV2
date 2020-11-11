package de.fel1x.capturetheflag.team;

import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public enum Team {

    NONE("§fnull", new ArrayList<>()),

    RED("§cRot", new ArrayList<>()),
    BLUE("§9Blau", new ArrayList<>());

    private final String teamName;
    private final List<Player> teamPlayers;

    Team(String teamName, List<Player> teamPlayers) {
        this.teamName = teamName;
        this.teamPlayers = teamPlayers;
    }

    public List<Player> getTeamPlayers() {
        return this.teamPlayers;
    }

    public String getTeamName() {
        return this.teamName;
    }
}
