package de.fel1x.teamcrimx.mlgwars.timer;

import de.fel1x.teamcrimx.mlgwars.MlgWars;
import de.fel1x.teamcrimx.mlgwars.gamestate.Gamestate;
import org.bukkit.Bukkit;
import org.bukkit.Sound;

public class PreGameTimer implements ITimer {

    private final MlgWars mlgWars = MlgWars.getInstance();

    private boolean running = false;
    private int taskId;
    private int countdown = 40;

    @Override
    public void start() {
        if(!this.running) {

            this.mlgWars.getGamestateHandler().setGamestate(Gamestate.PREGAME);
            this.running = true;

            this.taskId = Bukkit.getScheduler().scheduleSyncRepeatingTask(this.mlgWars, () -> {

                switch (countdown) {
                    case 30: case 20: case 10: case 5: case 4: case 3: case 2: case 1:
                        Bukkit.broadcastMessage(this.mlgWars.getPrefix() + "§7Die Schutzzeit endet in §e"
                                + (countdown == 1 ? "einer Sekunde" : this.countdown + " Sekunden"));
                        Bukkit.getOnlinePlayers().forEach(player -> player.playSound(player.getLocation(), Sound.NOTE_BASS, 2f, 3f));
                        break;

                    case 0:
                        Bukkit.broadcastMessage(this.mlgWars.getPrefix() + "§cDie Schutzzeit ist vorbei! Kämpft!");
                        Bukkit.getOnlinePlayers().forEach(player -> player.playSound(player.getLocation(), Sound.LEVEL_UP, 3f, 5f));
                        this.mlgWars.startTimerByClass(InGameTimer.class);
                        break;
                }

                this.countdown--;

            }, 0L, 20L);
        }

    }

    @Override
    public void stop() {
        if(this.running) {
            Bukkit.getScheduler().cancelTask(taskId);
            this.running = false;
            this.countdown = 60;
        }
    }
}
