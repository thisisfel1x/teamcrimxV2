package de.fel1x.teamcrimx.mlgwars.utils;

import de.dytanic.cloudnet.ext.cloudperms.bukkit.BukkitCloudNetCloudPermissionsPlugin;
import de.fel1x.teamcrimx.crimxapi.utils.Actionbar;
import de.fel1x.teamcrimx.mlgwars.MlgWars;
import de.fel1x.teamcrimx.mlgwars.enums.Spawns;
import de.fel1x.teamcrimx.mlgwars.gamestate.Gamestate;
import de.fel1x.teamcrimx.mlgwars.objects.GamePlayer;
import de.fel1x.teamcrimx.mlgwars.objects.MlgWarsTeam;
import de.fel1x.teamcrimx.mlgwars.timer.EndingTimer;
import me.libraryaddict.disguise.DisguiseAPI;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextColor;
import org.bukkit.*;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Firework;
import org.bukkit.entity.Player;
import org.bukkit.inventory.meta.FireworkMeta;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class WinDetection {

    public WinDetection(MlgWars mlgWars) {
        if (!Bukkit.getOnlinePlayers().isEmpty()) {
            if (mlgWars.getGamestateHandler().getGamestate() != Gamestate.ENDING) {
                if (mlgWars.getTeamSize() == 1) {
                    if (mlgWars.getData().getPlayers().size() == 1) {
                        Player winner = mlgWars.getData().getPlayers().get(0);
                        GamePlayer winnerGamePlayer = mlgWars.getData().getGamePlayers().get(winner.getUniqueId());
                        winnerGamePlayer.getActiveKit().disableKit();

                        winnerGamePlayer.getCrimxCoins().addCoinsAsync(100);
                        winner.sendMessage(mlgWars.getPrefix() + "§7Du hast das Spiel gewonnen! §a(+100 Coins)");

                        winnerGamePlayer.updateOnlineTime();

                        this.cleanUpOnlinePlayers(mlgWars);

                        Bukkit.getOnlinePlayers().forEach(player -> {
                            Actionbar.sendFullTitle(player, winner.getDisplayName(),
                                    "§7hat das Spiel gewonnen!", 10, 50, 10);
                        });

                        winnerGamePlayer.getStats().increaseWinsByOne();
                        winnerGamePlayer.saveStats();

                        mlgWars.startTimerByClass(EndingTimer.class);
                    } else if (mlgWars.getData().getPlayers().size() == 0) {
                        this.cleanUpOnlinePlayers(mlgWars);

                        Bukkit.getOnlinePlayers().forEach(player -> {
                            Actionbar.sendFullTitle(player, "§cNiemand",
                                    "§7hat das Spiel gewonnen!", 10, 50, 10);
                        });

                        mlgWars.startTimerByClass(EndingTimer.class);
                    }
                } else if (mlgWars.getTeamSize() > 1) {
                    if (mlgWars.getData().getGameTeams().size() == 1) {
                        this.cleanUpOnlinePlayers(mlgWars);

                        MlgWarsTeam winnerTeam = new ArrayList<>(mlgWars.getData().getGameTeams().values()).get(0);

                        Bukkit.getOnlinePlayers().forEach(player -> {
                            Actionbar.sendFullTitle(player, "§aTeam #" + winnerTeam.getTeamId(),
                                    "§7hat das Spiel gewonnen!", 10, 50, 10);
                        });

                        List<String> teamPlayers = new ArrayList<>();

                        for (Player winnerTeamTeamPlayer : winnerTeam.getTeamPlayers()) {
                            teamPlayers.add(winnerTeamTeamPlayer.getDisplayName());

                            GamePlayer winnerGamePlayer = mlgWars.getData().getGamePlayers().get(winnerTeamTeamPlayer.getUniqueId());
                            winnerGamePlayer.getActiveKit().disableKit();

                            winnerGamePlayer.getCrimxCoins().addCoinsAsync(100);

                            winnerTeamTeamPlayer.sendMessage(mlgWars.getPrefix() + "§7Du hast das Spiel gewonnen! §a(+100 Coins)");

                            winnerGamePlayer.getStats().increaseWinsByOne();
                            winnerGamePlayer.updateOnlineTime();
                            winnerGamePlayer.saveStats();
                        }

                        String winMessage = mlgWars.getPrefix() + "§7Das Team §a#" + winnerTeam.getTeamId()
                                + " §8(" + String.join(", ", teamPlayers) + "§8) §7hat das Spiel §agewonnen!";

                        Bukkit.broadcastMessage(winMessage);

                        mlgWars.startTimerByClass(EndingTimer.class);
                    } else if (mlgWars.getData().getGameTeams().size() == 0) {

                        Bukkit.getOnlinePlayers().forEach(player -> {
                            Actionbar.sendFullTitle(player, "§cKein Team",
                                    "§7hat das Spiel gewonnen!", 10, 50, 10);
                        });

                        mlgWars.startTimerByClass(EndingTimer.class);
                    }
                }
            }
        } else {
            Bukkit.getServer().shutdown();
        }
    }

    private void cleanUpOnlinePlayers(MlgWars mlgWars) {
        Bukkit.getOnlinePlayers().forEach(player -> {
            GamePlayer gamePlayer = mlgWars.getData().getGamePlayers().get(player.getUniqueId());
            gamePlayer.removeFromSpectators();
            gamePlayer.cleanUpOnJoin();
            gamePlayer.teleport(Spawns.LOBBY);
            player.playSound(player.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 2f, 0.5f);
            if (DisguiseAPI.isDisguised(player)) {
                DisguiseAPI.undisguiseToAll(player);
            }
            BukkitCloudNetCloudPermissionsPlugin.getInstance().updateNameTags(player);
            player.playerListName(player.displayName());
        });
    }

    @Deprecated
    // new wining animation system
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
