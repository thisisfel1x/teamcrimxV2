package de.fel1x.teamcrimx.mlgwars.utils;

import de.fel1x.teamcrimx.crimxapi.coins.CoinsAPI;
import de.fel1x.teamcrimx.crimxapi.utils.Actionbar;
import de.fel1x.teamcrimx.mlgwars.MlgWars;
import de.fel1x.teamcrimx.mlgwars.enums.Spawns;
import de.fel1x.teamcrimx.mlgwars.gamestate.Gamestate;
import de.fel1x.teamcrimx.mlgwars.objects.GamePlayer;
import de.fel1x.teamcrimx.mlgwars.timer.EndingTimer;
import org.bukkit.*;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Firework;
import org.bukkit.entity.Player;
import org.bukkit.inventory.meta.FireworkMeta;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.Random;

public class WinDetection {

    private final MlgWars mlgWars = MlgWars.getInstance();
    private int timer;

    public WinDetection() {
        if(this.mlgWars.getGamestateHandler().getGamestate() != Gamestate.ENDING) {
            if(this.mlgWars.getData().getPlayers().size() == 1) {

                Player winner = this.mlgWars.getData().getPlayers().get(0);
                CoinsAPI coinsAPI = new CoinsAPI(winner.getUniqueId());
                coinsAPI.addCoins(100);
                winner.sendMessage(this.mlgWars.getPrefix() + "§7Du hast das Spiel gewonnen! §a(+100 Coins)");

                Bukkit.getOnlinePlayers().forEach(player -> {
                    GamePlayer gamePlayer = new GamePlayer(player);
                    gamePlayer.cleanUpOnJoin();
                    gamePlayer.teleport(Spawns.LOBBY);
                    player.playSound(player.getLocation(), Sound.LEVEL_UP, 2f, 0.5f);

                    Actionbar.sendTitle(player, winner.getDisplayName(), 10, 50, 10);
                    Actionbar.sendSubTitle(player, "§7hat das Spiel gewonnen!", 10, 50, 10);
                });

                timer = 5;
                new BukkitRunnable() {
                    @Override
                    public void run() {
                        spawnFireworkCircle(Spawns.LOBBY.getLocation(), 5, 10);
                        if(timer == 0) {
                            this.cancel();
                        }
                        timer--;
                    }
                }.runTaskTimer(this.mlgWars, 0L, 20L);

                this.mlgWars.startTimerByClass(EndingTimer.class);
            } else if(this.mlgWars.getData().getPlayers().size() == 0) {

                Bukkit.getOnlinePlayers().forEach(player -> {
                    GamePlayer gamePlayer = new GamePlayer(player);
                    gamePlayer.cleanUpOnJoin();
                    gamePlayer.teleport(Spawns.LOBBY);

                    Actionbar.sendTitle(player, "§cNiemand", 10, 50, 10);
                    Actionbar.sendSubTitle(player, "§7hat das Spiel gewonnen!", 10, 50, 10);
                });

                this.mlgWars.startTimerByClass(EndingTimer.class);
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

            Random r = new Random();

            fireworkMeta.setPower(2);
            fireworkMeta.addEffect(FireworkEffect.builder().withColor(Color.fromBGR(r.nextInt(255), r.nextInt(255), r.nextInt(255))).flicker(true).build());
            firework.setFireworkMeta(fireworkMeta);

        }
    }

}
