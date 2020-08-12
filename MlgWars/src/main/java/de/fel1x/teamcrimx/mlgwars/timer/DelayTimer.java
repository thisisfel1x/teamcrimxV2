package de.fel1x.teamcrimx.mlgwars.timer;

import de.dytanic.cloudnet.ext.bridge.BridgeHelper;
import de.dytanic.cloudnet.ext.bridge.bukkit.BukkitCloudNetHelper;
import de.fel1x.teamcrimx.crimxapi.database.mongodb.MongoDBCollection;
import de.fel1x.teamcrimx.crimxapi.utils.Actionbar;
import de.fel1x.teamcrimx.mlgwars.MlgWars;
import de.fel1x.teamcrimx.mlgwars.gamestate.Gamestate;
import de.fel1x.teamcrimx.mlgwars.kit.IKit;
import de.fel1x.teamcrimx.mlgwars.objects.GamePlayer;
import de.fel1x.teamcrimx.mlgwars.objects.ScoreboardTeam;
import org.bukkit.*;
import org.bukkit.entity.Player;
import org.bukkit.metadata.FixedMetadataValue;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class DelayTimer implements ITimer {

    private final MlgWars mlgWars = MlgWars.getInstance();

    private boolean running = false;
    private int taskId;
    private int countdown = 3;

    @Override
    public void start() {
        if(!this.running) {

            this.mlgWars.getGamestateHandler().setGamestate(Gamestate.DELAY);
            this.running = true;

            for (Player player : this.mlgWars.getData().getPlayers()) {
                player.setGameMode(GameMode.SURVIVAL);
                player.getInventory().clear();

                player.setLevel(0);
                player.setExp(0f);
                this.mlgWars.getData().getPlayTime().put(player.getUniqueId(), System.currentTimeMillis());

                GamePlayer gamePlayer = new GamePlayer(player, true);
                gamePlayer.checkForTeam();
                try {
                    IKit iKit = gamePlayer.getSelectedKit().getClazz().newInstance();
                    iKit.setKitInventory(player);
                } catch (InstantiationException | IllegalAccessException ignored) {
                    player.sendMessage(this.mlgWars.getPrefix() + "§cEin Fehler ist aufgetreten! Du erhälst keine Kit-Items");
                }

                gamePlayer.saveObjectInDocument("gamesPlayed",
                        (player.getMetadata("games").get(0).asInt() + 1), MongoDBCollection.MLGWARS);
                gamePlayer.setInGameScoreboard();
            }

            this.teleportPlayersToSpawns();

            BukkitCloudNetHelper.changeToIngame();
            BukkitCloudNetHelper.setMaxPlayers(50);
            BridgeHelper.updateServiceInfo();

            this.taskId = Bukkit.getScheduler().scheduleSyncRepeatingTask(this.mlgWars, () -> {

                switch (countdown) {
                    case 3: case 2: case 1:
                        Bukkit.broadcastMessage(this.mlgWars.getPrefix() + "§7Das Spiel startet in §e"
                                + (countdown == 1 ? "einer §7Sekunde" : this.countdown + " §7Sekunden"));
                        Bukkit.getOnlinePlayers().forEach(player -> player.playSound(player.getLocation(), Sound.NOTE_BASS, 1.5f, 0.75f));
                        break;

                    case 0:
                        Bukkit.broadcastMessage(this.mlgWars.getPrefix() + "§aDas Spiel beginnt!");
                        Bukkit.getOnlinePlayers().forEach(player -> player.playSound(player.getLocation(), Sound.FIREWORK_LAUNCH, 1.5f, 2f));
                        this.mlgWars.startTimerByClass(PreGameTimer.class);
                        break;
                }

                Bukkit.getOnlinePlayers().forEach(player -> {
                    player.setLevel(0);
                    player.setExp(0f);

                    Actionbar.sendTitle(player, (countdown == 3) ? "§a§l3"
                            : (countdown == 2) ? "§e§l2" : (countdown == 1) ? "§c§l1" : "§a§lGO!", 0, 40, 10);
                    if(player.hasMetadata("team")) {
                        int team = player.getMetadata("team").get(0).asInt();
                        Actionbar.sendActiobar(player, "§7Team §a#" + team);
                    }
                });

                countdown--;

            }, 0L, 20L);
        }

    }

    @Override
    public void stop() {
        if(this.running) {
            Bukkit.getScheduler().cancelTask(taskId);
            this.running = false;
            this.countdown = 60;
        }
    }

    private void teleportPlayersToSpawns() {
        List<Location> playerSpawns = this.mlgWars.getData().getPlayerSpawns();
        Collections.shuffle(playerSpawns);

        if(this.mlgWars.getTeamSize() == 1) {
            List<Player> players = this.mlgWars.getData().getPlayers();

            Collections.shuffle(players);

            for (int i = 0; i < players.size(); i++) {
                players.get(i).teleport(playerSpawns.get(i));
            }
        } else if(this.mlgWars.getTeamSize() > 1) {
            List<ScoreboardTeam> teams = new ArrayList<>(this.mlgWars.getData().getGameTeams().values());

            for (int i = 0; i < teams.size(); i++) {
                ScoreboardTeam currentTeam = teams.get(i);
                if(currentTeam.getTeamPlayers().isEmpty()) {
                    this.mlgWars.getData().getGameTeams().remove(i);
                    continue;
                }
                for (Player teamPlayer : currentTeam.getTeamPlayers()) {
                    teamPlayer.teleport(playerSpawns.get(i));
                    this.mlgWars.getData().getGameTeams().get(i).getAlivePlayers().add(teamPlayer);
                    System.out.println(this.mlgWars.getData().getGameTeams().get(i).getAlivePlayers());
                }
            }
            System.out.println(this.mlgWars.getData().getGameTeams());
        }
    }
}
