package de.fel1x.bingo.tasks;

import com.destroystokyo.paper.Title;
import de.fel1x.bingo.Bingo;
import de.fel1x.bingo.gamehandler.Gamestate;
import de.fel1x.bingo.objects.BingoDifficulty;
import org.bukkit.Bukkit;
import org.bukkit.Sound;

public class LobbyTask implements IBingoTask {

    private final Bingo bingo = Bingo.getInstance();
    private int taskId = 0;
    private int countdown = 60;

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
                        if(this.countdown == 10) {
                            BingoDifficulty bingoDifficulty = this.bingo.getVotingManager().getFinalDifficulty();

                            Bukkit.getScheduler().runTaskAsynchronously(this.bingo, () -> this.bingo.generateItems(bingoDifficulty));

                            Title title = Title.builder()
                                    .title(bingoDifficulty.getDisplayName())
                                    .subtitle("§7Ausgewählte Schwierigkeit")
                                    .fadeIn(10)
                                    .stay(60)
                                    .fadeIn(10)
                                    .build();
                            this.bingo.getData().getPlayers().forEach(player -> player.sendTitle(title));
                        }

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

                if (this.countdown >= 1) {
                    Bukkit.getOnlinePlayers().forEach(current -> {
                        current.setLevel(this.countdown);
                        current.setExp((float) this.countdown / (float) 60);
                    });
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
