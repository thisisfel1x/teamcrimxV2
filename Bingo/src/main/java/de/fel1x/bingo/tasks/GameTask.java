package de.fel1x.bingo.tasks;

import com.destroystokyo.paper.Title;
import de.fel1x.bingo.Bingo;
import de.fel1x.bingo.gamehandler.Gamestate;
import de.fel1x.bingo.scenarios.IBingoScenario;
import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.boss.BarColor;
import org.bukkit.boss.BarFlag;
import org.bukkit.boss.BarStyle;
import org.bukkit.boss.BossBar;
import org.bukkit.entity.Player;

import java.util.Random;

public class GameTask implements IBingoTask {

    private final Bingo bingo = Bingo.getInstance();
    private int taskId = 0;
    private int timer = 0;

    private final Title title = Title.builder().title("§aViel Glück").subtitle("§7Das Spiel beginnt").fadeIn(0)
            .stay(40).fadeOut(10).build();

    private boolean isRunning = false;

    private BossBar bossBar;

    private final Random random = new Random();
    private int eventTimer = this.random.nextInt(120) + 60;
    private double timeToGo = this.eventTimer;

    @Override
    public void start() {

        Bukkit.getScheduler().cancelTasks(this.bingo);

        this.bossBar = this.bingo.getServer().createBossBar(String.format("§7Nächstes Event in §e%s Sekunden", this.eventTimer),
                BarColor.GREEN, BarStyle.SEGMENTED_20, BarFlag.DARKEN_SKY);
        this.bossBar.removeFlag(BarFlag.DARKEN_SKY);

        Bukkit.getOnlinePlayers().forEach(player -> this.bossBar.addPlayer(player));

        if (!this.isRunning) {

            this.isRunning = true;
            this.bingo.getGamestateHandler().setGamestate(Gamestate.INGAME);

            for (Player player : this.bingo.getData().getPlayers()) {
                this.bingo.getData().getPlayTime().put(player.getUniqueId(), System.currentTimeMillis());
                this.bingo.getData().getCachedStats().get(player).increaseGamesByOne();

                player.sendTitle(this.title);
                player.playSound(player.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 2f, 0.2f);
            }

            this.taskId = Bukkit.getScheduler().scheduleSyncRepeatingTask(this.bingo, () -> {

                this.bossBar.setColor(this.getColor(this.eventTimer));
                this.bossBar.setProgress(this.eventTimer / this.timeToGo);
                if (this.eventTimer > 0) {
                    this.bossBar.setTitle(String.format("§7Nächstes Event in §e%s", ((this.eventTimer == 1) ? "einer Sekunde" :
                            ((this.eventTimer <= 60) ? this.eventTimer + " Sekunden" :
                                    String.format("%02d:%02d", this.eventTimer / 60, this.eventTimer % 60)))));
                }

                Bukkit.getOnlinePlayers().forEach(player -> player.sendActionBar("§fVergangene Zeit §8● §e" + this.formatSeconds(this.timer)));

                if (this.eventTimer == 0) {
                    this.eventTimer = this.random.nextInt(120) + 60;
                    this.timeToGo = this.eventTimer;

                    try {
                        IBingoScenario bingoScenario = this.bingo.startRandomScenario()
                                .get(this.random.nextInt(this.bingo.startRandomScenario().size())).newInstance();
                        bingoScenario.execute();
                        this.bossBar.setTitle("§7Event §8● §a§l" + bingoScenario.getName());
                    } catch (InstantiationException | IllegalAccessException e) {
                        e.printStackTrace();
                    }

                }

                this.timer++;

                if(this.eventTimer > 0) {
                    this.eventTimer--;
                }

                if(Bukkit.getOnlinePlayers().isEmpty()) {
                    Bukkit.shutdown();
                }


            }, 0L, 20L);
        }

    }

    @Override
    public void stop() {

        if (this.isRunning) {

            this.isRunning = false;
            Bukkit.getScheduler().cancelTask(this.taskId);

            this.bossBar.removeAll();

        }

    }

    private String formatSeconds(int timeInSeconds) {
        int secondsLeft = timeInSeconds % 3600 % 60;
        int minutes = (int) Math.floor(timeInSeconds % 3600D / 60D);
        int hours = (int) Math.floor(timeInSeconds / 3600D);
        int days = (int) Math.floor(timeInSeconds / 3600D * 24D);

        String HH = ((hours < 10) ? "0" : "") + hours;
        String MM = ((minutes < 10) ? "0" : "") + minutes;
        String SS = ((secondsLeft < 10) ? "0" : "") + secondsLeft;

        return MM + ":" + SS;
    }

    private BarColor getColor(int progress) {

        double fraction = progress / this.timeToGo;

        if (fraction > 0.66) {
            return BarColor.GREEN;
        } else if (fraction > 0.33) {
            return BarColor.YELLOW;
        } else {
            return BarColor.RED;
        }

    }

    public BossBar getBossBar() {
        return this.bossBar;
    }
}
