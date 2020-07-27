package de.fel1x.teamcrimx.mlgwars.timer;

import de.fel1x.teamcrimx.mlgwars.MlgWars;
import de.fel1x.teamcrimx.mlgwars.gamestate.Gamestate;
import org.bukkit.Bukkit;
import org.bukkit.Sound;

public class EndingTimer implements ITimer {

    private final MlgWars mlgWars = MlgWars.getInstance();

    private boolean running = false;
    private int taskId;
    private int countdown = 20;

    @Override
    public void start() {
        if(!this.running) {

            this.mlgWars.getGamestateHandler().setGamestate(Gamestate.ENDING);
            this.running = true;

            this.taskId = Bukkit.getScheduler().scheduleSyncRepeatingTask(this.mlgWars, () -> {

                switch (countdown) {
                    case 20: case 10: case 5: case 4: case 3: case 2: case 1:
                        Bukkit.broadcastMessage(this.mlgWars.getPrefix() + "§cDer Server startet in "
                                + (countdown == 1 ? "einer Sekunde" : this.countdown + " Sekunden") + " neu");
                        Bukkit.getOnlinePlayers().forEach(player -> player.playSound(player.getLocation(), Sound.NOTE_BASS, 2f, 3f));
                        break;

                    case 0:
                        Bukkit.broadcastMessage(this.mlgWars.getPrefix() + "§cDer Server startet neu");
                        Bukkit.getOnlinePlayers().forEach(player -> player.playSound(player.getLocation(), Sound.CAT_HISS, 3f, 5f));
                        Bukkit.getServer().shutdown();
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
