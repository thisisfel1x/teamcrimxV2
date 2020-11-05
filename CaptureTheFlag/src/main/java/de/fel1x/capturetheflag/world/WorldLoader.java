package de.fel1x.capturetheflag.world;

import com.destroystokyo.paper.profile.PlayerProfile;
import com.destroystokyo.paper.profile.ProfileProperty;
import de.fel1x.capturetheflag.CaptureTheFlag;
import de.fel1x.capturetheflag.filehandler.SpawnHandler;
import org.bson.Document;
import org.bukkit.*;
import org.bukkit.block.BlockFace;
import org.bukkit.block.Sign;
import org.bukkit.block.Skull;

import java.util.List;
import java.util.UUID;

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

        } catch (Exception ignored) { }

        this.setTop5Wall();

    }

    private void setTop5Wall() {
        if (SpawnHandler.loadLocation("topHead1") == null) {
            return;
        }

        List<Document> documents = CaptureTheFlag.getInstance().getTop(5);
        for (int i = 0; i < 5; i++) {

            int current = i + 1;

            Location locHead = SpawnHandler.loadLocation("topHead" + current);
            Location locSign = SpawnHandler.loadLocation("topSign" + current);

            if(locHead == null || locSign == null) continue;

            Skull skull = (Skull) locHead.getBlock().getState();

            if (locSign.getBlock().getState() instanceof Sign) {
                Sign sign = (Sign) locSign.getBlock().getState();

                sign.setLine(0, "§7---------------");
                sign.setLine(1, "§cNicht");
                sign.setLine(2, "§cbesetzt");
                sign.setLine(3, "§7---------------");

                sign.update();
            }

            skull.setPlayerProfile(Bukkit.createProfile("MHF_Question"));
            skull.update();

        }

        int i = 1;

        for (Document currentDocument : documents) {
            int current = i;
            i++;

            Location locHead = SpawnHandler.loadLocation("topHead" + current);
            Location locSign = SpawnHandler.loadLocation("topSign" + current);

            if(locHead == null || locSign == null) continue;

            Skull skull = (Skull) locHead.getBlock().getState();

            if (locSign.getBlock().getState() instanceof Sign) {
                Sign sign = (Sign) locSign.getBlock().getState();

                int kills = currentDocument.getInteger("kills");
                int deaths = currentDocument.getInteger("deaths");
                int gamesWon = currentDocument.getInteger("gamesWon");

                double kd;
                if (deaths > 0) {
                    double a = ((double) kills / deaths) * 100;
                    kd = Math.round(a);
                } else {
                    kd = kills;
                }

                sign.setLine(0, "#" + current);
                sign.setLine(1, currentDocument.getString("name"));
                sign.setLine(2, gamesWon + " Wins");
                sign.setLine(3, "K/D: " + (kd / 100));

                sign.update();
            }

            skull.setOwner(currentDocument.getString("name"));

            skull.update();
        }

    }

}
