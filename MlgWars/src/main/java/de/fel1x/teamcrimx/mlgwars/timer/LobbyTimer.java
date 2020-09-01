package de.fel1x.teamcrimx.mlgwars.timer;

import de.fel1x.teamcrimx.mlgwars.MlgWars;
import de.fel1x.teamcrimx.mlgwars.gamestate.Gamestate;
import de.fel1x.teamcrimx.mlgwars.maphandler.ChestFiller;
import org.bukkit.Bukkit;
import org.bukkit.Sound;

public class LobbyTimer implements ITimer {

    private final MlgWars mlgWars = MlgWars.getInstance();

    private boolean running = false;
    private int taskId;
    private int countdown = 60;

    public void start(int countdown) {
        this.countdown = countdown;
        this.mlgWars.setLobbyCountdown(this.countdown);
        this.start();
    }

    @Override
    public void start() {
        if (!this.running) {

            this.mlgWars.getGamestateHandler().setGamestate(Gamestate.LOBBY);
            this.running = true;

            this.taskId = Bukkit.getScheduler().scheduleSyncRepeatingTask(this.mlgWars, () -> {
                switch (countdown) {
                    case 60:
                    case 50:
                    case 40:
                    case 30:
                    case 20:
                    case 10:
                    case 5:
                    case 4:
                    case 3:
                    case 2:
                    case 1:
                        Bukkit.broadcastMessage(this.mlgWars.getPrefix() + "§7Die Runde startet in §e"
                                + (countdown == 1 ? "einer §7Sekunde" : this.countdown + " §7Sekunden"));
                        Bukkit.getOnlinePlayers().forEach(player -> player.playSound(player.getLocation(), Sound.NOTE_BASS, 2f, 3f));
                        break;

                    case 0:
                        Bukkit.broadcastMessage(this.mlgWars.getPrefix() + "§aDie Runde beginnt!");
                        Bukkit.getOnlinePlayers().forEach(player -> {
                            player.playSound(player.getLocation(), Sound.LEVEL_UP, 3f, 5f);
                            player.setLevel(0);
                            player.setExp(0);
                        });
                        this.stop();
                        this.mlgWars.startTimerByClass(DelayTimer.class);
                        break;
                }

                if (this.countdown == 5) {
                    new ChestFiller();
                }

                if (this.countdown >= 1) {
                    Bukkit.getOnlinePlayers().forEach(current -> {
                        current.setLevel(countdown);
                        current.setExp((float) countdown / (float) 60);
                    });
                }

                this.countdown--;
                this.mlgWars.setLobbyCountdown(this.countdown);

            }, 0L, 20L);
        }

    }

    @Override
    public void stop() {
        if (this.running) {
            Bukkit.getScheduler().cancelTask(taskId);
            this.running = false;
            this.setCountdown(60);
            this.mlgWars.setLobbyCountdown(60);

            Bukkit.getOnlinePlayers().forEach(player -> {
                player.setLevel(0);
                player.setExp(0);
            });
        }
    }

    public int getCountdown() {
        return countdown;
    }

    public void setCountdown(int countdown) {
        this.countdown = countdown;
    }
}
