package de.fel1x.capturetheflag.timers;

import de.dytanic.cloudnet.ext.bridge.BridgePlayerManager;
import de.fel1x.capturetheflag.CaptureTheFlag;
import de.fel1x.capturetheflag.filehandler.SpawnHandler;
import org.bukkit.Bukkit;
import org.bukkit.Sound;

public class EndingTimer {

    int taskId;
    boolean running;

    int countdown = 20;

    public void start() {

        if (!running) {

            running = true;

            Bukkit.getOnlinePlayers().forEach(current -> {
                Bukkit.getOnlinePlayers().forEach(currentLoop -> {

                    current.showPlayer(currentLoop);
                    currentLoop.showPlayer(current);

                });

                current.teleport(SpawnHandler.loadLocation("lobby"));

            });

            taskId = Bukkit.getScheduler().scheduleSyncRepeatingTask(CaptureTheFlag.getInstance(), () -> {

                switch (countdown) {

                    case 20:
                    case 10:
                    case 5:
                    case 4:
                    case 3:
                    case 2:

                        Bukkit.broadcastMessage("§7Der Server §c§lstoppt §7in §c§l" + countdown + " Sekunden");

                        Bukkit.getOnlinePlayers().forEach(current -> {
                            current.playSound(current.getLocation(), Sound.NOTE_BASS, 5, 7);
                        });

                        break;

                    case 1:

                        Bukkit.broadcastMessage("§7Der Server §c§lstoppt §7in §c§leiner Sekunde");

                        Bukkit.getOnlinePlayers().forEach(current -> {
                            current.playSound(current.getLocation(), Sound.NOTE_BASS, 5, 7);
                        });

                        break;


                    case 0:

                        Bukkit.getOnlinePlayers().forEach(current -> {

                            BridgePlayerManager.getInstance().proxySendPlayer(current.getUniqueId(), "Lobby-1");

                        });

                        this.stop();

                        Bukkit.getServer().shutdown();

                        break;

                }

                if (countdown > 0) {
                    Bukkit.getOnlinePlayers().forEach(current -> {

                        current.setLevel(countdown);
                        current.setExp((float) countdown / (float) 20);

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

            Bukkit.getScheduler().cancelTask(taskId);

        }

    }

}
