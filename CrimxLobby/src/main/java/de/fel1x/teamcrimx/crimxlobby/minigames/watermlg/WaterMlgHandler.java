package de.fel1x.teamcrimx.crimxlobby.minigames.watermlg;

import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class WaterMlgHandler {

    private List<Player> waterMlgPlayers;
    private Map<Player, Boolean> failed;

    public WaterMlgHandler() {

        waterMlgPlayers = new ArrayList<>();
        failed = new HashMap<>();

    }

    public List<Player> getWaterMlgPlayers() {
        return waterMlgPlayers;
    }

    public Map<Player, Boolean> getFailed() {
        return failed;
    }

}
