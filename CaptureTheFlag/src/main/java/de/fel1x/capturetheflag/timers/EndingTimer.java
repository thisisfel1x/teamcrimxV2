package de.fel1x.capturetheflag.timers;

import de.dytanic.cloudnet.driver.CloudNetDriver;
import de.dytanic.cloudnet.ext.bridge.player.IPlayerManager;
import de.fel1x.capturetheflag.CaptureTheFlag;
import de.fel1x.capturetheflag.filehandler.SpawnHandler;
import org.bukkit.Bukkit;
import org.bukkit.Sound;

public class EndingTimer implements ITimer {

    private final CaptureTheFlag captureTheFlag = CaptureTheFlag.getInstance();
    private final IPlayerManager playerManager = CloudNetDriver.getInstance().getServicesRegistry().getFirstService(IPlayerManager.class);

    int taskId;
    boolean running;

    int countdown = 20;

    public void start() {
        if (!running) {
            running = true;

            Bukkit.getOnlinePlayers().forEach(current -> {
                Bukkit.getOnlinePlayers().forEach(currentLoop -> {
                    current.showPlayer(this.captureTheFlag, currentLoop);
                    currentLoop.showPlayer(this.captureTheFlag, current);
                });

                current.teleport(SpawnHandler.loadLocation("lobby"));
            });

            taskId = Bukkit.getScheduler().scheduleSyncRepeatingTask(this.captureTheFlag, () -> {
                switch (countdown) {

                    case 20:
                    case 10:
                    case 5:
                    case 4:
                    case 3:
                    case 2:

                        Bukkit.broadcastMessage("§7Der Server §c§lstoppt §7in §c§l" + countdown + " Sekunden");
                        Bukkit.getOnlinePlayers().forEach(current -> current.playSound(current.getLocation(),
                                Sound.BLOCK_NOTE_BLOCK_BASS, 5, 7));

                        break;

                    case 1:

                        Bukkit.broadcastMessage("§7Der Server §c§lstoppt §7in §c§leiner Sekunde");
                        Bukkit.getOnlinePlayers().forEach(current -> current.playSound(current.getLocation(),
                                Sound.BLOCK_NOTE_BLOCK_BASS, 5, 7));

                        break;


                    case 0:

                        Bukkit.getOnlinePlayers().forEach(current -> this.playerManager
                                .getPlayerExecutor(current.getUniqueId()).connect("Lobby-1"));
                        break;

                }

                if (countdown > 0) {
                    Bukkit.getOnlinePlayers().forEach(current -> {

                        current.setLevel(countdown);
                        current.setExp((float) countdown / (float) 20);

                    });
                } else if (countdown < 0) {
                    if (Bukkit.getOnlinePlayers().size() == 0) {
                        this.stop();
                        Bukkit.getServer().shutdown();
                    }
                }

                countdown--;
            }, 0L, 20L);
        }
    }

    public void stop() {
        if (running) {

            running = false;
            countdown = 20;

            Bukkit.getScheduler().cancelTask(taskId);
        }
    }

}
