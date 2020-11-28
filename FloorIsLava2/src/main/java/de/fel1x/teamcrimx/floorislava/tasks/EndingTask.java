package de.fel1x.teamcrimx.floorislava.tasks;

import de.fel1x.teamcrimx.floorislava.FloorIsLava;
import de.fel1x.teamcrimx.floorislava.gamehandler.Gamestate;
import org.bukkit.Bukkit;
import org.bukkit.Sound;

public class EndingTask implements IFloorIsLavaTask {

    private final FloorIsLava floorIsLava = FloorIsLava.getInstance();

    private int taskId = 0;
    private int timer = 20;

    private boolean isRunning = false;

    public void start() {
        if (!this.isRunning) {

            this.isRunning = true;
            this.floorIsLava.getGamestateHandler().setGamestate(Gamestate.ENDING);

            this.taskId = Bukkit.getScheduler().scheduleSyncRepeatingTask(this.floorIsLava, () -> {
                switch (this.timer) {
                    case 1:
                    case 2:
                    case 3:
                    case 4:
                    case 5:
                    case 10:
                    case 20:
                        Bukkit.broadcastMessage(this.floorIsLava.getPrefix() + "§7Der Server stoppt in §c"
                                + ((this.timer == 1) ? "einer Sekunde" : (this.timer + " Sekunden")));

                        this.floorIsLava.getData().getPlayers().forEach(player ->
                                player.playSound(player.getLocation(), Sound.BLOCK_NOTE_BLOCK_BASS, 1, 0.75f));
                        break;
                    case 0:
                        Bukkit.broadcastMessage(this.floorIsLava.getPrefix() + "§c§lDer Server stoppt!");
                        Bukkit.getServer().shutdown();
                        break;
                }
                this.timer--;
            }, 0L, 20L);
        }
    }

    public void stop() {
        if (this.isRunning) {
            this.isRunning = false;
            Bukkit.getScheduler().cancelTask(this.taskId);
            this.timer = 60;
        }
    }
}
