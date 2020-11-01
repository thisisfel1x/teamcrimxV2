package de.fel1x.capturetheflag.timers;

import de.dytanic.cloudnet.driver.CloudNetDriver;
import de.dytanic.cloudnet.ext.bridge.player.IPlayerManager;
import de.fel1x.capturetheflag.CaptureTheFlag;
import de.fel1x.capturetheflag.filehandler.SpawnHandler;
import de.fel1x.capturetheflag.gamestate.Gamestate;
import org.bukkit.Bukkit;
import org.bukkit.Sound;

public class EndingTimer implements ITimer {

    private final CaptureTheFlag captureTheFlag = CaptureTheFlag.getInstance();
    private final IPlayerManager playerManager = CloudNetDriver.getInstance().getServicesRegistry().getFirstService(IPlayerManager.class);

    private int taskId;
    private boolean running;

    private int countdown = 20;

    public void start() {
        if (!this.running) {

            this.captureTheFlag.getGamestateHandler().setGamestate(Gamestate.ENDING);
            this.running = true;

            Bukkit.getOnlinePlayers().forEach(current -> {
                Bukkit.getOnlinePlayers().forEach(currentLoop -> {
                    current.showPlayer(this.captureTheFlag, currentLoop);
                    currentLoop.showPlayer(this.captureTheFlag, current);
                });

                current.teleport(SpawnHandler.loadLocation("lobby"));
            });

            this.taskId = Bukkit.getScheduler().scheduleSyncRepeatingTask(this.captureTheFlag, () -> {
                switch (countdown) {
                    case 20:
                    case 10:
                    case 5:
                    case 4:
                    case 3:
                    case 2:
                    case 1:
                        Bukkit.broadcastMessage(this.captureTheFlag.getPrefix() + "§cDer Server startet in "
                                + (countdown == 1 ? "einer Sekunde" : this.countdown + " Sekunden") + " neu");
                        Bukkit.getOnlinePlayers().forEach(player -> player.playSound(player.getLocation(), Sound.BLOCK_NOTE_BLOCK_BASS, 2f, 3f));
                        break;

                    case 0:
                        Bukkit.broadcastMessage(this.captureTheFlag.getPrefix() + "§cDer Server startet neu");
                        Bukkit.getOnlinePlayers().forEach(player -> {
                            this.playerManager.getPlayerExecutor(player.getUniqueId()).connect("Lobby-1");
                            player.playSound(player.getLocation(), Sound.ENTITY_CAT_HISS, 3f, 5f);
                        });
                        break;
                }

                if (countdown > 0) {
                    Bukkit.getOnlinePlayers().forEach(current -> {

                        current.setLevel(countdown);
                        current.setExp((float) countdown / (float) 20);

                    });
                } else if (countdown < 0 && Bukkit.getOnlinePlayers().isEmpty()) {
                    Bukkit.getServer().shutdown();
                }

                this.countdown--;

            }, 0L, 20L);
        }
    }

    public void stop() {
        if (this.running) {

            this.running = false;
            this.countdown = 20;

            Bukkit.getScheduler().cancelTask(this.taskId);
        }
    }
}
