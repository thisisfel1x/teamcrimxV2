package de.fel1x.teamcrimx.mlgwars.timer;

import de.dytanic.cloudnet.ext.bridge.BridgeHelper;
import de.dytanic.cloudnet.ext.bridge.bukkit.BukkitCloudNetHelper;
import de.fel1x.teamcrimx.crimxapi.utils.Actionbar;
import de.fel1x.teamcrimx.mlgwars.MlgWars;
import de.fel1x.teamcrimx.mlgwars.gamestate.Gamestate;
import de.fel1x.teamcrimx.mlgwars.kit.rework.Kit;
import de.fel1x.teamcrimx.mlgwars.maphandler.gametype.types.SoloGameType;
import de.fel1x.teamcrimx.mlgwars.maphandler.gametype.types.TntMadnessGameType;
import de.fel1x.teamcrimx.mlgwars.objects.GamePlayer;
import de.fel1x.teamcrimx.mlgwars.objects.MlgWarsTeam;
import org.bukkit.*;
import org.bukkit.entity.Player;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class DelayTimer implements ITimer {

    private final MlgWars mlgWars = MlgWars.getInstance();

    private boolean running = false;
    private int taskId;
    private int countdown = this.mlgWars.getTeamSize() > 1 ? 5
            : this.mlgWars.getGameType().getClass() != SoloGameType.class ? 8 : 5;

    @Override
    public void start() {
        if (!this.running) {
            this.mlgWars.getGamestateHandler().setGamestate(Gamestate.DELAY);
            this.running = true;

            for (Player player : this.mlgWars.getData().getPlayers()) {
                player.setGameMode(GameMode.SURVIVAL);
                player.getInventory().clear();

                player.setLevel(0);
                player.setExp(0f);

                this.mlgWars.getData().getGamePlayers().get(player.getUniqueId())
                        .setGameStartTime(System.currentTimeMillis());

                GamePlayer gamePlayer = this.mlgWars.getData().getGamePlayers().get(player.getUniqueId());

                gamePlayer.checkForTeam();

                gamePlayer.clearCosmetics();

                try {
                    Kit iKit = gamePlayer.getSelectedKit().getClazz().getDeclaredConstructor(Player.class, MlgWars.class).newInstance(player, this.mlgWars);
                    iKit.initializeKit();
                } catch (InstantiationException | IllegalAccessException | NoSuchMethodException | InvocationTargetException ignored) {
                    player.sendMessage(this.mlgWars.getPrefix() + "§cEin Fehler ist aufgetreten! Du erhälst keine Kit-Items (OPFER)");
                }

                gamePlayer.getStats().increaseGamesByOne();
                gamePlayer.setInGameScoreboard();
            }

            this.mlgWars.getGameType().gameInit();
            this.teleportPlayersToSpawns();

            BukkitCloudNetHelper.changeToIngame();
            BukkitCloudNetHelper.setMaxPlayers(50);
            BridgeHelper.updateServiceInfo();

            this.taskId = Bukkit.getScheduler().scheduleSyncRepeatingTask(this.mlgWars, () -> {
                float pitch = 0.2f;

                switch (this.countdown) {
                    case 8 -> Bukkit.getOnlinePlayers().forEach(player -> Actionbar.sendFullTitle(player,
                            "§5" + this.mlgWars.getGameType().getGameTypeName(),
                            "§7Ausgewählter Modus", 10, 30, 10));
                    case 5, 4, 3, 2, 1 -> {
                        Bukkit.broadcastMessage(this.mlgWars.getPrefix() + "§7Das Spiel startet in §e"
                                + (this.countdown == 1 ? "einer §7Sekunde" : this.countdown + " §7Sekunden"));
                    }
                    case 0 -> {
                        Bukkit.broadcastMessage(this.mlgWars.getPrefix() + "§aDas Spiel beginnt!");
                        Bukkit.getOnlinePlayers().forEach(player -> {
                            Actionbar.sendOnlyTitle(player, "§a§lGO!", 0, 20, 10);
                        });
                        Bukkit.getServer().playSound(net.kyori.adventure.sound.Sound.sound(Sound.BLOCK_NOTE_BLOCK_BASS.key(),
                                net.kyori.adventure.sound.Sound.Source.BLOCK, 1f, 1f));
                        this.mlgWars.startTimerByClass(PreGameTimer.class);
                    }
                }

                if(this.countdown > 0) {
                    Bukkit.getServer().playSound(net.kyori.adventure.sound.Sound.sound(Sound.BLOCK_NOTE_BLOCK_HARP.key(),
                            net.kyori.adventure.sound.Sound.Source.BLOCK, 2f, (0.2f + this.countdown * pitch)));
                }

                Bukkit.getOnlinePlayers().forEach(player -> {
                    player.setLevel(0);
                    player.setExp(0f);
                    if (this.countdown <= 3) {
                        Actionbar.sendOnlyTitle(player, (this.countdown == 3) ? "§a§l3"
                                : (this.countdown == 2) ? "§e§l2" : (this.countdown == 1) ? "§c§l1" : "§a§lGO!", 0, 40, 10);
                    }
                });

                this.countdown--;

            }, 0L, 20L);
        }

    }

    @Override
    public void stop() {
        if (this.running) {
            Bukkit.getScheduler().cancelTask(this.taskId);
            this.running = false;
            this.countdown = 60;
        }
    }

    private void teleportPlayersToSpawns() {
        List<Location> playerSpawns = this.mlgWars.getData().getPlayerSpawns();
        Collections.shuffle(playerSpawns);

        if (this.mlgWars.getTeamSize() == 1) {
            List<Player> players = this.mlgWars.getData().getPlayers();

            Collections.shuffle(players);

            for (int i = 0; i < players.size(); i++) {
                players.get(i).teleport(playerSpawns.get(i));
            }
        } else if (this.mlgWars.getTeamSize() > 1) {
            List<MlgWarsTeam> teams = new ArrayList<>(this.mlgWars.getData().getGameTeams().values());

            for (int i = 0; i < teams.size(); i++) {
                MlgWarsTeam currentTeam = teams.get(i);
                if (currentTeam.getTeamPlayers().isEmpty()) {
                    this.mlgWars.getData().getGameTeams().remove(i);
                    continue;
                }
                for (Player teamPlayer : currentTeam.getTeamPlayers()) {
                    teamPlayer.teleport(playerSpawns.get(i));
                    this.mlgWars.getData().getGameTeams().get(i).getAlivePlayers().add(teamPlayer);
                }
            }
        }
    }
}
