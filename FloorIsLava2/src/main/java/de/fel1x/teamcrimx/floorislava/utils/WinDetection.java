package de.fel1x.teamcrimx.floorislava.utils;

import de.dytanic.cloudnet.ext.cloudperms.bukkit.BukkitCloudNetCloudPermissionsPlugin;
import de.fel1x.teamcrimx.crimxapi.coins.CoinsAPI;
import de.fel1x.teamcrimx.crimxapi.utils.Actionbar;
import de.fel1x.teamcrimx.floorislava.FloorIsLava;
import de.fel1x.teamcrimx.floorislava.gamehandler.Gamestate;
import de.fel1x.teamcrimx.floorislava.gameplayer.GamePlayer;
import de.fel1x.teamcrimx.floorislava.tasks.EndingTask;
import org.bukkit.*;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Firework;
import org.bukkit.entity.Player;
import org.bukkit.inventory.meta.FireworkMeta;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.Random;

public class WinDetection {

    private int timer;

    public WinDetection() {
        if (!Bukkit.getOnlinePlayers().isEmpty()) {
            FloorIsLava floorIsLava = FloorIsLava.getInstance();
            if (floorIsLava.getGamestateHandler().getGamestate() != Gamestate.ENDING) {
                if (floorIsLava.getData().getPlayers().size() == 1) {

                    Player winner = floorIsLava.getData().getPlayers().get(0);
                    GamePlayer winnerGamePlayer = new GamePlayer(winner);
                    CoinsAPI coinsAPI = new CoinsAPI(winner.getUniqueId());

                    coinsAPI.addCoins(100);
                    winner.sendMessage(floorIsLava.getPrefix() + "§7Du hast das Spiel gewonnen! §a(+100 Coins)");

                    winnerGamePlayer.saveStats();

                    Bukkit.getOnlinePlayers().forEach(player -> {
                        GamePlayer gamePlayer = new GamePlayer(player);
                        gamePlayer.cleanupInventory();
                        player.teleport(floorIsLava.getSpawnLocation());
                        player.playSound(player.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 2f, 0.5f);

                        Actionbar.sendFullTitle(player, winner.getDisplayName(),
                                "§7hat das Spiel gewonnen!", 10, 50, 10);

                        player.setPlayerListName(player.getName());
                        BukkitCloudNetCloudPermissionsPlugin.getInstance().updateNameTags(player);
                    });

                    this.timer = 5;
                    new BukkitRunnable() {
                        @Override
                        public void run() {
                            spawnFireworkCircle(floorIsLava.getSpawnLocation(), 5, 10);
                            if (WinDetection.this.timer == 0) {
                                this.cancel();
                            }
                            WinDetection.this.timer--;
                        }
                    }.runTaskTimer(floorIsLava, 0L, 20L);

                    floorIsLava.startTimerByClass(EndingTask.class);
                } else if (floorIsLava.getData().getPlayers().size() == 0) {

                    Bukkit.getOnlinePlayers().forEach(player -> {
                        GamePlayer gamePlayer = new GamePlayer(player);
                        gamePlayer.cleanupInventory();
                        player.teleport(floorIsLava.getSpawnLocation());

                        Actionbar.sendFullTitle(player, "§cNiemand",
                                "§7hat das Spiel gewonnen!", 10, 50, 10);

                        player.setPlayerListName(player.getName());
                        BukkitCloudNetCloudPermissionsPlugin.getInstance().updateNameTags(player);

                    });

                    floorIsLava.startTimerByClass(EndingTask.class);
                }
            }
        }
    }

    private void spawnFireworkCircle(Location center, double radius, int amount) {

        World world = center.getWorld();
        double increment = (2 * Math.PI) / amount;
        for (int i = 0; i < amount; i++) {

            double angle = i * increment;
            double x = center.getX() + (radius * Math.cos(angle));
            double z = center.getZ() + (radius * Math.sin(angle));

            Location current = new Location(world, x, center.getY(), z);

            Firework firework = (Firework) current.getWorld().spawnEntity(current, EntityType.FIREWORK);

            FireworkMeta fireworkMeta = firework.getFireworkMeta();

            Random random = new Random();

            fireworkMeta.setPower(2);
            fireworkMeta.addEffect(FireworkEffect.builder()
                    .with(FireworkEffect.Type.values()[random.nextInt(FireworkEffect.Type.values().length)])
                    .withColor(Color.fromBGR(random.nextInt(255), random.nextInt(255), random.nextInt(255))).flicker(true).build());
            firework.setFireworkMeta(fireworkMeta);

        }
    }

}
