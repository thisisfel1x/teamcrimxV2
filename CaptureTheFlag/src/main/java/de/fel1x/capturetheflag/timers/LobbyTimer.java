package de.fel1x.capturetheflag.timers;

import de.dytanic.cloudnet.ext.bridge.bukkit.BukkitCloudNetHelper;
import de.fel1x.capturetheflag.CaptureTheFlag;
import de.fel1x.capturetheflag.gameplayer.GamePlayer;
import org.bukkit.Bukkit;
import org.bukkit.Sound;

public class LobbyTimer implements ITimer {

    private final CaptureTheFlag captureTheFlag = CaptureTheFlag.getInstance();

    int taskId;
    boolean running;

    int countdown = 60;

    public void start() {

        if (!running) {

            running = true;

            taskId = Bukkit.getScheduler().scheduleSyncRepeatingTask(this.captureTheFlag, () -> {
                switch (countdown) {

                    case 60:
                    case 50:
                    case 40:
                    case 30:
                    case 20:
                    case 10:
                    case 5:
                    case 4:
                    case 3:
                    case 2:

                        Bukkit.broadcastMessage("§7Das Spiel startet in §e§l" + countdown + " Sekunden");
                        Bukkit.getOnlinePlayers().forEach(current -> current.playSound(current.getLocation(),
                                Sound.BLOCK_NOTE_BLOCK_BASS, 5, 3));

                        break;

                    case 1:

                        Bukkit.broadcastMessage("§7Das Spiel startet in §e§leiner Sekunde");
                        Bukkit.getOnlinePlayers().forEach(current -> current.playSound(current.getLocation(),
                                Sound.BLOCK_NOTE_BLOCK_BASS, 5, 3));

                        break;


                    case 0:

                        Bukkit.broadcastMessage("§a§lDie Runde beginnt!");

                        this.stopFinally();

                        Bukkit.getOnlinePlayers().forEach(current -> current.playSound(current.getLocation(),
                                Sound.ENTITY_PLAYER_LEVELUP, 5, 7));

                        CaptureTheFlag.getInstance().getData().getPlayers().forEach(current -> {
                            GamePlayer player = new GamePlayer(current);

                            player.checkForTeam();
                            player.cleanupInventory();
                            player.teleportToTeamSpawn();
                            player.setKitItems();

                            current.setLevel(0);
                            current.setExp(0);

                        });

                        BukkitCloudNetHelper.changeToIngame();

                        break;

                }

                if (countdown >= 1) {
                    Bukkit.getOnlinePlayers().forEach(current -> {

                        current.setLevel(countdown);
                        current.setExp((float) countdown / (float) 60);

                    });
                }

                countdown--;

            }, 0L, 20L);

        }

    }

    public void stop() {

        if (running) {

            running = false;
            countdown = 60;

            Bukkit.getOnlinePlayers().forEach(current -> {

                current.setLevel(0);
                current.setExp(0);

            });

            Bukkit.getScheduler().cancelTask(taskId);

        }

    }

    private void stopFinally() {

        if (running) {

            running = false;
            countdown = 0;

            Bukkit.getOnlinePlayers().forEach(current -> {

                current.setLevel(0);
                current.setExp(0);

            });

            Bukkit.getScheduler().cancelTask(taskId);

        }

    }

    public int getCountdown() {
        return countdown;
    }

    public void setCountdown(int countdown) {
        this.countdown = countdown;
    }

    public boolean isRunning() {
        return running;
    }
}
