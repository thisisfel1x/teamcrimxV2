package de.fel1x.teamcrimx.mlgwars.objects;

import org.bukkit.entity.Player;

import java.util.ArrayList;

public class ScoreboardTeam {

    private int id;
    private int teamId;
    private int maxPlayers;
    private ArrayList<Player> teamPlayers;
    private ArrayList<Player> alivePlayers;

    public ScoreboardTeam(int id, int teamId, int maxPlayers, ArrayList<Player> teamPlayers, ArrayList<Player> alivePlayers) {
        this.id = id;
        this.teamId = teamId;
        this.maxPlayers = maxPlayers;
        this.teamPlayers = teamPlayers;
        this.alivePlayers = alivePlayers;
    }

    public int getId() {
        return this.id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getTeamId() {
        return this.teamId;
    }

    public void setTeamId(int teamId) {
        this.teamId = teamId;
    }

    public int getMaxPlayers() {
        return this.maxPlayers;
    }

    public void setMaxPlayers(int maxPlayers) {
        this.maxPlayers = maxPlayers;
    }

    public ArrayList<Player> getTeamPlayers() {
        return this.teamPlayers;
    }

    public void setTeamPlayers(ArrayList<Player> teamPlayers) {
        this.teamPlayers = teamPlayers;
    }

    public ArrayList<Player> getAlivePlayers() {
        return this.alivePlayers;
    }

    public void setAlivePlayers(ArrayList<Player> alivePlayers) {
        this.alivePlayers = alivePlayers;
    }
}
