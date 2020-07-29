package de.fel1x.teamcrimx.mlgwars.timer;

import de.fel1x.teamcrimx.mlgwars.MlgWars;
import de.fel1x.teamcrimx.mlgwars.gamestate.Gamestate;
import de.fel1x.teamcrimx.mlgwars.kit.IKit;
import de.fel1x.teamcrimx.mlgwars.objects.GamePlayer;
import org.bukkit.*;
import org.bukkit.entity.Player;

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

            this.mlgWars.getData().getPlayers().forEach(player -> {
                player.setGameMode(GameMode.SURVIVAL);
                player.getInventory().clear();
                this.mlgWars.getData().getPlayTime().put(player.getUniqueId(), System.currentTimeMillis());

                GamePlayer gamePlayer = new GamePlayer(player);
                try {
                    IKit iKit = gamePlayer.getSelectedKit().getClazz().newInstance();
                    iKit.setKitInventory(player);
                } catch (InstantiationException | IllegalAccessException ignored) {
                    player.sendMessage(this.mlgWars.getPrefix() + "§cEin Fehler ist aufgetreten! Du erhälst keine Kit-Items");
                }
            });

            this.teleportPlayersToSpawns();

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

        List<Player> players = this.mlgWars.getData().getPlayers();
        List<Location> playerSpawns = this.mlgWars.getData().getPlayerSpawns();

        Collections.shuffle(players);
        Collections.shuffle(playerSpawns);

        for (int i = 0; i < players.size(); i++) {
            players.get(i).teleport(playerSpawns.get(i));
        }
    }
}
