package de.fel1x.teamcrimx.mlgwars.timer;

import de.fel1x.teamcrimx.mlgwars.MlgWars;
import de.fel1x.teamcrimx.mlgwars.gamestate.Gamestate;
import org.bukkit.Bukkit;

public class IdleTimer implements ITimer {

    private final MlgWars mlgWars = MlgWars.getInstance();

    private boolean running = false;
    private int taskId;

    @Override
    public void start() {

        if (!this.running) {

            Bukkit.getScheduler().cancelTasks(this.mlgWars);

            this.mlgWars.getGamestateHandler().setGamestate(Gamestate.IDLE);
            this.running = true;

            this.taskId = Bukkit.getScheduler().scheduleSyncRepeatingTask(this.mlgWars, () -> {

                int neededPlayers = (this.mlgWars.getTeamSize() * 2) - this.mlgWars.getData().getPlayers().size();
                Bukkit.broadcastMessage(this.mlgWars.getPrefix() + "§7Warten auf §e" + (neededPlayers == 1 ? "einen §7weiteren" : neededPlayers + " §7weitere") + " Spieler!");

            }, 0L, 15 * 20L);
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
