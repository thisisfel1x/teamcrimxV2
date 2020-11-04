package de.fel1x.capturetheflag.timers;

import de.fel1x.capturetheflag.CaptureTheFlag;
import de.fel1x.capturetheflag.gamestate.Gamestate;
import org.bukkit.Bukkit;

public class IdleTimer implements ITimer {

    private final CaptureTheFlag captureTheFlag = CaptureTheFlag.getInstance();

    private boolean running = false;
    private int taskId;

    @Override
    public void start() {

        if (!this.running) {

            Bukkit.getScheduler().cancelTasks(this.captureTheFlag);

            this.captureTheFlag.getGamestateHandler().setGamestate(Gamestate.IDLE);
            this.running = true;

            this.taskId = Bukkit.getScheduler().scheduleSyncRepeatingTask(this.captureTheFlag, () -> {

                int neededPlayers = 6 - this.captureTheFlag.getData().getPlayers().size();
                Bukkit.broadcastMessage(this.captureTheFlag.getPrefix() + "§7Warten auf §e"
                        + (neededPlayers == 1 ? "einen §7weiteren" : neededPlayers + " §7weitere") + " Spieler");

            }, 0L, 30 * 20L);
        }

    }

    @Override
    public void stop() {
        if (this.running) {
            this.running = false;
            Bukkit.getScheduler().cancelTask(taskId);
        }

    }
}
