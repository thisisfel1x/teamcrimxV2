package de.fel1x.bingo.tasks;

import com.destroystokyo.paper.Title;
import de.dytanic.cloudnet.ext.bridge.BridgeHelper;
import de.dytanic.cloudnet.ext.bridge.bukkit.BukkitCloudNetHelper;
import de.fel1x.bingo.Bingo;
import de.fel1x.bingo.gamehandler.Gamestate;
import de.fel1x.bingo.objects.BingoPlayer;
import de.fel1x.bingo.objects.BingoTeam;
import de.fel1x.bingo.utils.Utils;
import de.fel1x.bingo.utils.scoreboard.GameScoreboard;
import de.fel1x.bingo.utils.world.ArmorstandStatsLoader;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Objects;

public class PreGameTask implements IBingoTask {

    private final Bingo bingo = Bingo.getInstance();
    private final String[] commandInfo = {
            " ",
            this.bingo.getPrefix() + "§e§lWichtige Commands:",
            this.bingo.getPrefix() + "§a/items §8● §7Liste alle Items auf",
            this.bingo.getPrefix() + "§a/backpack §7oder §a/bp §8● §7Öffne den Team-Backpack",
            " "
    };
    private int taskId = 0;
    private int timer = 30;
    private boolean isRunning = false;

    @Override
    public void start() {
        if (!this.isRunning) {

            BukkitCloudNetHelper.changeToIngame();
            BridgeHelper.updateServiceInfo();

            this.isRunning = true;
            this.bingo.getGamestateHandler().setGamestate(Gamestate.PREGAME);

            this.bingo.setGameScoreboard(new GameScoreboard());
            this.bingo.getData().getPlayers().forEach(player -> this.bingo.getGameScoreboard().setGameScoreboard(player));

            this.taskId = Bukkit.getScheduler().scheduleSyncRepeatingTask(this.bingo, () -> {

                switch (this.timer) {
                    case 20:
                        for (String string : this.commandInfo) {
                            Bukkit.broadcastMessage(string);
                        }
                        break;

                    case 30:
                    case 15:
                    case 10:
                    case 5:
                    case 4:
                    case 3:
                    case 2:
                    case 1:

                        Bukkit.broadcastMessage(this.bingo.getPrefix() + "§7Das Spiel startet in §e"
                                + ((this.timer == 1) ? "einer Sekunde" : this.timer + " Sekunden"));

                        this.bingo.getData().getPlayers().forEach(player -> {

                            player.setLevel(0);
                            player.setExp(0f);

                            player.playSound(player.getLocation(), Sound.BLOCK_NOTE_BLOCK_PLING, 1, 0.75f);
                            if (this.timer <= 3) {
                                player.sendTitle(Title.builder()
                                        .title(((this.timer == 3) ? "§a§l" : ((this.timer == 2) ? "§e§l" : "§c§l")) + this.timer)
                                        .fadeIn(10).stay(20).fadeOut(10).build());
                            }

                        });

                        break;

                    case 0:
                        Bukkit.broadcastMessage(this.bingo.getPrefix() + "§e§lDas Spiel beginnt!");
                        Bukkit.getScheduler().cancelTasks(this.bingo);

                        this.bingo.getGamestateHandler().setGamestate(Gamestate.INGAME);

                        this.bingo.startTimerByClass(GameTask.class);

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
