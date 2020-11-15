package de.fel1x.bingo.tasks;

import de.fel1x.bingo.Bingo;
import de.fel1x.bingo.gamehandler.Gamestate;
import de.fel1x.bingo.objects.BingoTeam;
import org.bukkit.Bukkit;

public class IdleTask implements IBingoTask {

    Bingo bingo = Bingo.getInstance();
    int taskId = 0;

    boolean isRunning = false;

    @Override
    public void start() {

        if (!this.isRunning) {

            this.isRunning = true;
            this.bingo.getGamestateHandler().setGamestate(Gamestate.IDLE);

            this.taskId = Bukkit.getScheduler().scheduleSyncRepeatingTask(this.bingo, () -> {

                int neededPlayers = (BingoTeam.RED.getTeamSize() * 2) - this.bingo.getData().getPlayers().size();
                Bukkit.broadcastMessage(String.format(this.bingo.getPrefix() + "§7Warten auf §c%s Spieler!",
                        (neededPlayers == 1) ? "einen §7weiteren" : neededPlayers + " §7weitere"));

            }, 0L, 20L * 30);
        }

    }

    @Override
    public void stop() {

        if (this.isRunning) {

            this.isRunning = false;
            Bukkit.getScheduler().cancelTask(this.taskId);

        }

    }
}
