package de.fel1x.teamcrimx.mlgwars.timer;

import de.fel1x.teamcrimx.mlgwars.MlgWars;
import de.fel1x.teamcrimx.mlgwars.gamestate.Gamestate;
import org.bukkit.Bukkit;

public class InGameTimer implements ITimer {

    private final MlgWars mlgWars = MlgWars.getInstance();

    private boolean running = false;
    private int taskId;

    private int gameTime = 0;

    @Override
    public void start() {

        if(!this.running) {
            this.running = true;
            this.mlgWars.getGamestateHandler().setGamestate(Gamestate.INGAME);

            this.taskId = Bukkit.getScheduler().scheduleSyncRepeatingTask(this.mlgWars, () -> {

                this.gameTime++;

            }, 0L, 20L);
        }

    }

    @Override
    public void stop() {
        if(this.running) {
            Bukkit.getScheduler().cancelTask(taskId);
            this.running = false;
        }
    }
}
