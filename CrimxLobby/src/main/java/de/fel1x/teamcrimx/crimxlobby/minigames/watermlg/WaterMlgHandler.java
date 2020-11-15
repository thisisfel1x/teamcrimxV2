package de.fel1x.teamcrimx.crimxlobby.minigames.watermlg;

import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class WaterMlgHandler {

    private final List<Player> waterMlgPlayers;
    private final Map<Player, Boolean> failed;

    public WaterMlgHandler() {

        this.waterMlgPlayers = new ArrayList<>();
        this.failed = new HashMap<>();

    }

    public List<Player> getWaterMlgPlayers() {
        return this.waterMlgPlayers;
    }

    public Map<Player, Boolean> getFailed() {
        return this.failed;
    }

}
