package de.fel1x.capturetheflag.world;

import de.fel1x.capturetheflag.filehandler.SpawnHandler;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.WorldCreator;

public class WorldLoader {

    public WorldLoader() {

        String lobbyWorldName = SpawnHandler.getWorld("lobby");
        String gameWorldName = SpawnHandler.getWorld("spectator");

        World lobbyWorld = Bukkit.createWorld(new WorldCreator(lobbyWorldName));
        World gameWorld = Bukkit.createWorld(new WorldCreator(gameWorldName));

        try {

            lobbyWorld.setGameRuleValue("doDaylightCycle", "false");
            lobbyWorld.setGameRuleValue("doMobSpawning", "false");
            lobbyWorld.setThundering(false);
            lobbyWorld.setStorm(false);
            lobbyWorld.setTime(8000);

            gameWorld.setGameRuleValue("doDaylightCycle", "false");
            gameWorld.setGameRuleValue("doMobSpawning", "false");
            gameWorld.setThundering(false);
            gameWorld.setStorm(false);
            gameWorld.setTime(8000);

        } catch (Exception ignored) {

        }

    }

}
