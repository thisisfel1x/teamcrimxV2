package de.fel1x.teamcrimx.floorislava;

import org.bukkit.entity.Player;

import java.util.ArrayList;

public class Data {

    private final ArrayList<Player> players;
    private final ArrayList<Player> spectators;

    public Data() {
        this.players = new ArrayList<>();
        this.spectators = new ArrayList<>();
    }

    public ArrayList<Player> getPlayers() {
        return this.players;
    }

    public ArrayList<Player> getSpectators() {
        return this.spectators;
    }
}
