package de.fel1x.bingo.tasks;

import de.fel1x.bingo.Bingo;
import de.fel1x.bingo.gamehandler.Gamestate;
import org.bukkit.Bukkit;
import org.bukkit.Sound;

public class EndingTask implements IBingoTask {

    Bingo bingo = Bingo.getInstance();
    int taskId = 0;
    int timer = 20;

    boolean isRunning = false;

    @Override
    public void start() {

        if (!this.isRunning) {

            this.isRunning = true;
            this.bingo.getGamestateHandler().setGamestate(Gamestate.ENDING);

            Bukkit.getOnlinePlayers().forEach(player -> player.getInventory().clear());

            this.taskId = Bukkit.getScheduler().scheduleSyncRepeatingTask(this.bingo, () -> {

                switch (this.timer) {

                    case 20:
                    case 10:
                    case 5:
                    case 4:
                    case 3:
                    case 2:
                    case 1:

                        Bukkit.broadcastMessage(this.bingo.getPrefix() + "§7Der Server stoppt in §c"
                                + ((this.timer == 1) ? "einer Sekunde" : this.timer + " Sekunden"));

                        this.bingo.getData().getPlayers().forEach(player ->
                                player.playSound(player.getLocation(), Sound.BLOCK_NOTE_BLOCK_BASS, 1, 0.75f));

                        break;

                    case 0:
                        Bukkit.broadcastMessage(this.bingo.getPrefix() + "§c§lDer Server stoppt!");
                        Bukkit.getServer().shutdown();

                        break;

                }

                this.timer--;

            }, 0L, 20L);
        }

    }

    @Override
    public void stop() {

        if (this.isRunning) {

            this.isRunning = false;
            Bukkit.getScheduler().cancelTask(this.taskId);

            this.timer = 60;

        }

    }
}
