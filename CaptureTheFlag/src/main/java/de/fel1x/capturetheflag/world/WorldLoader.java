package de.fel1x.capturetheflag.world;

import de.fel1x.capturetheflag.filehandler.SpawnHandler;
import org.bukkit.Bukkit;
import org.bukkit.GameRule;
import org.bukkit.World;
import org.bukkit.WorldCreator;

public class WorldLoader {

    public WorldLoader() {

        String lobbyWorldName = SpawnHandler.getWorld("lobby");
        String gameWorldName = SpawnHandler.getWorld("spectator");

        World lobbyWorld = Bukkit.createWorld(new WorldCreator(lobbyWorldName));
        World gameWorld = Bukkit.createWorld(new WorldCreator(gameWorldName));

        try {

            lobbyWorld.setGameRule(GameRule.DO_DAYLIGHT_CYCLE, false);
            lobbyWorld.setGameRule(GameRule.DO_MOB_SPAWNING, false);
            lobbyWorld.setThundering(false);
            lobbyWorld.setStorm(false);
            lobbyWorld.setTime(8000);

            gameWorld.setGameRule(GameRule.DO_DAYLIGHT_CYCLE, false);
            gameWorld.setGameRule(GameRule.DO_MOB_SPAWNING, false);
            gameWorld.setThundering(false);
            gameWorld.setStorm(false);
            gameWorld.setTime(8000);

        } catch (Exception ignored) {

        }

    }

}
