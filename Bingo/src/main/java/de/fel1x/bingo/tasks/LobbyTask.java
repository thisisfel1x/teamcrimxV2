package de.fel1x.bingo.tasks;

import de.fel1x.bingo.Bingo;
import de.fel1x.bingo.gamehandler.Gamestate;
import org.bukkit.Bukkit;
import org.bukkit.Sound;

public class LobbyTask implements IBingoTask {

    Bingo bingo = Bingo.getInstance();
    int taskId = 0;
    int countdown = 30;

    boolean isRunning = false;

    @Override
    public void start() {

        if (!this.isRunning) {

            this.isRunning = true;
            this.bingo.getGamestateHandler().setGamestate(Gamestate.LOBBY);

            this.taskId = Bukkit.getScheduler().scheduleSyncRepeatingTask(this.bingo, () -> {

                switch (this.countdown) {

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

                        Bukkit.broadcastMessage(this.bingo.getPrefix() + "§7Die Runde startet in §e"
                                + ((this.countdown == 1) ? "einer Sekunde" : this.countdown + " Sekunden"));

                        this.bingo.getData().getPlayers().forEach(player ->
                                player.playSound(player.getLocation(), Sound.BLOCK_NOTE_BLOCK_BASS, 1, 0.75f));

                        break;

                    case 0:
                        Bukkit.broadcastMessage(this.bingo.getPrefix() + "§e§lDie Runde beginnt!");

                        this.bingo.startTimerByClass(PreGameTask.class);

                        break;

                }

                this.countdown--;

            }, 0L, 20L);
        }

    }

    @Override
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
