package de.fel1x.teamcrimx.floorislava.tasks;

import de.fel1x.teamcrimx.floorislava.FloorIsLava;
import de.fel1x.teamcrimx.floorislava.gamehandler.Gamestate;
import org.bukkit.Bukkit;

public class IdleTask implements IFloorIsLavaTask {
    private final FloorIsLava floorIsLava = FloorIsLava.getInstance();

    private int taskId = 0;

    private boolean isRunning = false;

    public void start() {
        if (!this.isRunning) {
            this.isRunning = true;
            this.floorIsLava.getGamestateHandler().setGamestate(Gamestate.IDLE);
            this.taskId = Bukkit.getScheduler().scheduleSyncRepeatingTask(this.floorIsLava, () -> {
                int neededPlayers = 3 - this.floorIsLava.getData().getPlayers().size();
                Bukkit.broadcastMessage(String.format(this.floorIsLava.getPrefix() + "§7Warten auf §c%s Spieler!",
                        (neededPlayers == 1) ? "einen §7weiteren" : (neededPlayers + " §7weitere")));
            }, 0L, 600L);
        }
    }

    public void stop() {
        if (this.isRunning) {
            this.isRunning = false;
            Bukkit.getScheduler().cancelTask(this.taskId);
        }
    }
}
