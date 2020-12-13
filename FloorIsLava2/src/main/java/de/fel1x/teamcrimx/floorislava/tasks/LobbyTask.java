package de.fel1x.teamcrimx.floorislava.tasks;

import de.fel1x.teamcrimx.floorislava.FloorIsLava;
import de.fel1x.teamcrimx.floorislava.gamehandler.Gamestate;
import org.bukkit.Bukkit;
import org.bukkit.Sound;

public class LobbyTask implements IFloorIsLavaTask {
    private final FloorIsLava floorIsLava = FloorIsLava.getInstance();

    private int taskId = 0;
    private int countdown = 30;

    private boolean isRunning = false;

    public void start() {
        if (!this.isRunning) {
            this.isRunning = true;
            this.floorIsLava.getGamestateHandler().setGamestate(Gamestate.LOBBY);
            this.taskId = Bukkit.getScheduler().scheduleSyncRepeatingTask(this.floorIsLava, () -> {
                switch (this.countdown) {
                    case 1:
                    case 2:
                    case 3:
                    case 4:
                    case 5:
                    case 10:
                    case 20:
                    case 30:
                    case 40:
                    case 50:
                    case 60:
                        Bukkit.broadcastMessage(this.floorIsLava.getPrefix() + "§7Die Runde startet in §e" + ((this.countdown == 1) ? "einer Sekunde" : (this.countdown + " Sekunden")));
                        this.floorIsLava.getData().getPlayers().forEach(player ->
                                player.playSound(player.getLocation(), Sound.BLOCK_NOTE_BLOCK_BASS, 1, 0.75f));
                        break;
                    case 0:
                        Bukkit.broadcastMessage(this.floorIsLava.getPrefix() + "§e§lDie Runde beginnt!");
                        this.floorIsLava.startTimerByClass(PreGameTask.class);
                        break;
                }
                this.countdown--;
            }, 0L, 20L);
        }
    }

    public void stop() {
        if (this.isRunning) {
            this.isRunning = false;
            Bukkit.getScheduler().cancelTask(this.taskId);
            this.countdown = 60;
        }
    }

    public int getCountdown() {
        return this.countdown;
    }

    public void setCountdown(int countdown) {
        this.countdown = countdown;
    }
}
