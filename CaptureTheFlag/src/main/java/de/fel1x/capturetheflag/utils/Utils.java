package de.fel1x.capturetheflag.utils;

import de.dytanic.cloudnet.ext.cloudperms.bukkit.BukkitCloudNetCloudPermissionsPlugin;
import de.fel1x.capturetheflag.CaptureTheFlag;
import de.fel1x.capturetheflag.filehandler.SpawnHandler;
import de.fel1x.capturetheflag.gameplayer.GamePlayer;
import de.fel1x.capturetheflag.gamestate.Gamestate;
import de.fel1x.capturetheflag.team.Teams;
import org.bukkit.*;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Firework;
import org.bukkit.inventory.meta.FireworkMeta;

import java.util.Random;

public class Utils {

    public static void win(Teams teams) {

        CaptureTheFlag.getInstance().getInGameTimer().stop();
        CaptureTheFlag.getInstance().getEndingTimer().start();

        String name = teams.getTeamName();

        CaptureTheFlag.getInstance().getData().getPlayers().forEach(current -> {
            GamePlayer gamePlayer = new GamePlayer(current);
            gamePlayer.cleanupInventory();
        });

        CaptureTheFlag.getInstance().getGamestateHandler().setGamestate(Gamestate.ENDING);

        Bukkit.getOnlinePlayers().forEach(current -> {

            Actionbar.sendTitle(current, "§7Team " + name, 5, 30, 5);
            Actionbar.sendSubTitle(current, "§7hat das Spiel gewonnen", 5, 30, 5);

            spawnCircle(SpawnHandler.loadLocation("lobby"), 1.5, 12);

            current.getInventory().clear();
            current.getInventory().setArmorContents(null);

            current.setGameMode(GameMode.SURVIVAL);

            current.getActivePotionEffects().forEach(potionEffect -> {
                current.removePotionEffect(potionEffect.getType());
            });

            current.setScoreboard(Bukkit.getScoreboardManager().getNewScoreboard());
            BukkitCloudNetCloudPermissionsPlugin.getInstance().updateNameTags(current);

        });

    }

    public static void spawnCircle(Location center, double radius, int amount) {

        World world = center.getWorld();
        double increment = (2 * Math.PI) / amount;
        for (int i = 0; i < amount; i++) {

            double angle = i * increment;
            double x = center.getX() + (radius * Math.cos(angle));
            double z = center.getZ() + (radius * Math.sin(angle));

            Location current = new Location(world, x, center.getY(), z);

            Firework firework = (Firework) current.getWorld().spawnEntity(current, EntityType.FIREWORK);

            FireworkMeta fireworkMeta = firework.getFireworkMeta();

            Random r = new Random();

            fireworkMeta.setPower(2);
            fireworkMeta.addEffect(FireworkEffect.builder().withColor(Color.fromBGR(r.nextInt(255), r.nextInt(255), r.nextInt(255))).flicker(true).build());
            firework.setFireworkMeta(fireworkMeta);

        }


    }

}
