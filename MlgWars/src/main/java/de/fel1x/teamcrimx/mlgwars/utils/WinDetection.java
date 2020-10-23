package de.fel1x.teamcrimx.mlgwars.utils;

import de.dytanic.cloudnet.driver.CloudNetDriver;
import de.dytanic.cloudnet.driver.permission.IPermissionGroup;
import de.dytanic.cloudnet.driver.permission.IPermissionUser;
import de.dytanic.cloudnet.ext.cloudperms.bukkit.BukkitCloudNetCloudPermissionsPlugin;
import de.fel1x.teamcrimx.crimxapi.coins.CoinsAPI;
import de.fel1x.teamcrimx.crimxapi.database.mongodb.MongoDBCollection;
import de.fel1x.teamcrimx.crimxapi.utils.Actionbar;
import de.fel1x.teamcrimx.mlgwars.MlgWars;
import de.fel1x.teamcrimx.mlgwars.enums.Spawns;
import de.fel1x.teamcrimx.mlgwars.gamestate.Gamestate;
import de.fel1x.teamcrimx.mlgwars.objects.GamePlayer;
import de.fel1x.teamcrimx.mlgwars.objects.ScoreboardTeam;
import de.fel1x.teamcrimx.mlgwars.scoreboard.MlgWarsScoreboard;
import de.fel1x.teamcrimx.mlgwars.timer.EndingTimer;
import me.libraryaddict.disguise.DisguiseAPI;
import org.bukkit.*;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Firework;
import org.bukkit.entity.Player;
import org.bukkit.inventory.meta.FireworkMeta;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class WinDetection {

    private final MlgWars mlgWars = MlgWars.getInstance();
    private final MlgWarsScoreboard mlgWarsScoreboard = new MlgWarsScoreboard();
    private int timer;

    public WinDetection() {
        if (!Bukkit.getOnlinePlayers().isEmpty()) {
            if (this.mlgWars.getGamestateHandler().getGamestate() != Gamestate.ENDING) {
                if (this.mlgWars.getTeamSize() == 1) {
                    if (this.mlgWars.getData().getPlayers().size() == 1) {

                        Player winner = this.mlgWars.getData().getPlayers().get(0);
                        GamePlayer winnerGamePlayer = new GamePlayer(winner, true);
                        CoinsAPI coinsAPI = new CoinsAPI(winner.getUniqueId());
                        IPermissionUser iPermissionUser = CloudNetDriver.getInstance().getPermissionManagement().getUser(winner.getUniqueId());

                        if (iPermissionUser == null) return;

                        IPermissionGroup permissionGroup = CloudNetDriver.getInstance().getPermissionManagement().getHighestPermissionGroup(iPermissionUser);

                        coinsAPI.addCoins(100);
                        winner.sendMessage(this.mlgWars.getPrefix() + "§7Du hast das Spiel gewonnen! §a(+100 Coins)");

                        stopTasks(winner);

                        Bukkit.getOnlinePlayers().forEach(player -> {
                            GamePlayer gamePlayer = new GamePlayer(player);
                            gamePlayer.cleanUpOnJoin();
                            gamePlayer.teleport(Spawns.LOBBY);
                            player.playSound(player.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 2f, 0.5f);
                            if (DisguiseAPI.isDisguised(player)) {
                                DisguiseAPI.undisguiseToAll(player);
                            }

                            Actionbar.sendTitle(player, winner.getDisplayName(), 10, 50, 10);
                            Actionbar.sendSubTitle(player, "§7hat das Spiel gewonnen!", 10, 50, 10);

                            player.setPlayerListName(player.getName());
                            mlgWarsScoreboard.setEndingScoreboard(player, permissionGroup.getDisplay().replace('&', '§')
                                    + winner.getName(), permissionGroup.getDisplay().replace('&', '§'));
                            BukkitCloudNetCloudPermissionsPlugin.getInstance().updateNameTags(player);
                        });

                        timer = 5;
                        new BukkitRunnable() {
                            @Override
                            public void run() {
                                spawnFireworkCircle(Spawns.LOBBY.getLocation(), 5, 10);
                                if (timer == 0) {
                                    this.cancel();
                                }
                                timer--;
                            }
                        }.runTaskTimer(this.mlgWars, 0L, 20L);

                        int gamesWon = (int) winnerGamePlayer.getObjectFromMongoDocument("gamesWon", MongoDBCollection.MLGWARS);
                        winnerGamePlayer.saveObjectInDocument("gamesWon", (gamesWon + 1), MongoDBCollection.MLGWARS);

                        this.mlgWars.startTimerByClass(EndingTimer.class);
                    } else if (this.mlgWars.getData().getPlayers().size() == 0) {

                        this.mlgWars.getWorldLoader().setTop5Wall();

                        Bukkit.getOnlinePlayers().forEach(player -> {
                            GamePlayer gamePlayer = new GamePlayer(player);
                            gamePlayer.cleanUpOnJoin();
                            gamePlayer.teleport(Spawns.LOBBY);
                            if (DisguiseAPI.isDisguised(player)) {
                                DisguiseAPI.undisguiseToAll(player);
                            }

                            Actionbar.sendTitle(player, "§cNiemand", 10, 50, 10);
                            Actionbar.sendSubTitle(player, "§7hat das Spiel gewonnen!", 10, 50, 10);

                            player.setPlayerListName(player.getName());
                            mlgWarsScoreboard.setEndingScoreboard(player, "§cNiemand", "§c");
                            BukkitCloudNetCloudPermissionsPlugin.getInstance().updateNameTags(player);

                        });

                        this.mlgWars.startTimerByClass(EndingTimer.class);
                    }
                } else if (this.mlgWars.getTeamSize() > 1) {
                    if (this.mlgWars.getData().getGameTeams().size() == 1) {

                        ScoreboardTeam winnerTeam = new ArrayList<>(this.mlgWars.getData().getGameTeams().values()).get(0);

                        Bukkit.getOnlinePlayers().forEach(player -> {
                            GamePlayer gamePlayer = new GamePlayer(player);
                            gamePlayer.cleanUpOnJoin();
                            gamePlayer.teleport(Spawns.LOBBY);
                            player.playSound(player.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 2f, 0.5f);
                            if (DisguiseAPI.isDisguised(player)) {
                                DisguiseAPI.undisguiseToAll(player);
                            }

                            Actionbar.sendTitle(player, "§aTeam #" + winnerTeam.getTeamId(), 10, 50, 10);
                            Actionbar.sendSubTitle(player, "§7hat das Spiel gewonnen!", 10, 50, 10);

                            player.setPlayerListName(player.getName());
                            mlgWarsScoreboard.setEndingScoreboard(player, "§aTeam #" + winnerTeam.getTeamId(), "§a");
                            BukkitCloudNetCloudPermissionsPlugin.getInstance().updateNameTags(player);
                        });

                        List<String> teamPlayers = new ArrayList<>();

                        for (Player winnerTeamTeamPlayer : winnerTeam.getTeamPlayers()) {

                            teamPlayers.add(winnerTeamTeamPlayer.getDisplayName());

                            GamePlayer winnerGamePlayer = new GamePlayer(winnerTeamTeamPlayer, true);
                            CoinsAPI coinsAPI = new CoinsAPI(winnerTeamTeamPlayer.getUniqueId());

                            coinsAPI.addCoins(100);
                            winnerTeamTeamPlayer.sendMessage(this.mlgWars.getPrefix() + "§7Du hast das Spiel gewonnen! §a(+100 Coins)");

                            int gamesWon = (int) winnerGamePlayer.getObjectFromMongoDocument("gamesWon", MongoDBCollection.MLGWARS);
                            winnerGamePlayer.saveObjectInDocument("gamesWon", (gamesWon + 1), MongoDBCollection.MLGWARS);

                            stopTasks(winnerTeamTeamPlayer);
                        }

                        String winMessage = this.mlgWars.getPrefix() + "§7Das Team §a#" + winnerTeam.getTeamId()
                                + " §8(" + String.join(", ", teamPlayers) + "§8) §7hat das Spiel §agewonnen!";

                        Bukkit.broadcastMessage(winMessage);

                        timer = 5;
                        new BukkitRunnable() {
                            @Override
                            public void run() {
                                spawnFireworkCircle(Spawns.LOBBY.getLocation(), 5, 10);
                                if (timer == 0) {
                                    this.cancel();
                                }
                                timer--;
                            }
                        }.runTaskTimer(this.mlgWars, 0L, 20L);

                        this.mlgWars.startTimerByClass(EndingTimer.class);
                    } else if (this.mlgWars.getData().getGameTeams().size() == 0) {

                        this.mlgWars.getWorldLoader().setTop5Wall();

                        Bukkit.getOnlinePlayers().forEach(player -> {
                            GamePlayer gamePlayer = new GamePlayer(player);
                            gamePlayer.cleanUpOnJoin();
                            gamePlayer.teleport(Spawns.LOBBY);
                            if (DisguiseAPI.isDisguised(player)) {
                                DisguiseAPI.undisguiseToAll(player);
                            }

                            Actionbar.sendTitle(player, "§cKein Team", 10, 50, 10);
                            Actionbar.sendSubTitle(player, "§7hat das Spiel gewonnen!", 10, 50, 10);

                            player.setPlayerListName(player.getName());
                            mlgWarsScoreboard.setEndingScoreboard(player, "§cKein Team", "§c");
                            BukkitCloudNetCloudPermissionsPlugin.getInstance().updateNameTags(player);

                        });

                        this.mlgWars.startTimerByClass(EndingTimer.class);
                    }
                }
            }
        } else {
            Bukkit.getServer().shutdown();
        }
    }

    public static void stopTasks(Player player) {

        GamePlayer gamePlayer = new GamePlayer(player);

        switch (gamePlayer.getSelectedKit()) {

            case KANGAROO:
                if (MlgWars.getInstance().getData().getKangarooTask().containsKey(player.getUniqueId())) {
                    MlgWars.getInstance().getData().getKangarooTask().get(player.getUniqueId()).cancel();
                }
                break;

            case THOR:
                if (MlgWars.getInstance().getData().getThorTask().containsKey(player.getUniqueId())) {
                    MlgWars.getInstance().getData().getThorTask().get(player.getUniqueId()).cancel();
                }
                break;

            case FARMER:
                if (MlgWars.getInstance().getData().getFarmerTask().containsKey(player.getUniqueId())) {
                    MlgWars.getInstance().getData().getFarmerTask().get(player.getUniqueId()).cancel();
                }
                break;

            case CHICKEN_BRIDGE:
                if (MlgWars.getInstance().getData().getEggTask().containsKey(player.getUniqueId())) {
                    MlgWars.getInstance().getData().getEggTask().get(player.getUniqueId()).forEach(BukkitRunnable::cancel);
                }
                break;

            case TURTLE:
                if (MlgWars.getInstance().getData().getTurtleTask().containsKey(player.getUniqueId())) {
                    MlgWars.getInstance().getData().getTurtleTask().get(player.getUniqueId()).forEach(BukkitRunnable::cancel);
                }

                break;

            case CSGO:
                if (MlgWars.getInstance().getData().getCsgoTasks().containsKey(player.getUniqueId())) {
                    MlgWars.getInstance().getData().getCsgoTasks().get(player.getUniqueId()).forEach(BukkitRunnable::cancel);
                }

                break;
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
