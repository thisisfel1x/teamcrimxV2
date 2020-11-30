package de.fel1x.teamcrimx.floorislava.tasks;

import de.fel1x.teamcrimx.floorislava.FloorIsLava;
import de.fel1x.teamcrimx.floorislava.gamehandler.Gamestate;
import de.fel1x.teamcrimx.floorislava.scenarios.*;
import org.bukkit.Bukkit;
import org.bukkit.boss.BarColor;
import org.bukkit.boss.BarFlag;
import org.bukkit.boss.BarStyle;
import org.bukkit.boss.BossBar;
import org.bukkit.entity.Player;

import java.util.Arrays;
import java.util.List;
import java.util.Random;

public class FarmingTask implements IFloorIsLavaTask {
    private final FloorIsLava floorIsLava = FloorIsLava.getInstance();
    private final List<Class<? extends ILavaScenario>> scenarios = Arrays.asList(AnvilRain.class, EnderGamesTeleport.class, InventoryShuffle.class, RandomPotionEffect.class);
    private final Random random = new Random();
    private int taskId = 0;
    private int timer = 0;
    private int farmingTime = 60;
    private boolean isRunning = false;
    private boolean generateNewEvent = true;
    private BossBar bossBar;
    private int eventTimer = this.random.nextInt(30) + 40;

    private double timeToGo = this.eventTimer;

    public void start() {
        this.bossBar = this.floorIsLava.getServer().createBossBar(String.format("§7Nächstes Event in §e%s Sekunden", this.eventTimer),
                BarColor.GREEN, BarStyle.SEGMENTED_20, BarFlag.DARKEN_SKY);
        this.bossBar.removeFlag(BarFlag.DARKEN_SKY);
        Bukkit.getOnlinePlayers().forEach(player -> this.bossBar.addPlayer(player));

        if (!this.isRunning) {
            this.isRunning = true;
            this.floorIsLava.getGamestateHandler().setGamestate(Gamestate.FARMING);
            for (Player player : this.floorIsLava.getData().getPlayers()) {
                this.floorIsLava.getData().getPlayTime().put(player.getUniqueId(), System.currentTimeMillis());
                this.floorIsLava.getData().getCachedStats().get(player).increaseGamesByOne();
            }
            this.taskId = Bukkit.getScheduler().scheduleSyncRepeatingTask(this.floorIsLava, () -> {
                Gamestate gamestate = this.floorIsLava.getGamestateHandler().getGamestate();
                for (Player onlinePlayer : Bukkit.getOnlinePlayers()) {
                    onlinePlayer.sendActionBar("§7Verbleibende Zeit §8● §a" + this.formatSeconds(this.farmingTime));
                }
                if (this.eventTimer > 0 && this.generateNewEvent)
                    this.bossBar.setColor(getColor(this.eventTimer));
                    this.bossBar.setProgress((this.eventTimer / this.timeToGo >= 0 ? this.eventTimer / this.timeToGo : 0));
                    this.bossBar.setTitle(String.format("§7Nächstes Event in §e%s",
                            (this.eventTimer == 1) ? "einer Sekunde" : ((this.eventTimer <= 60) ? (this.eventTimer + " Sekunden") : String.format("%02d:%02d",
                                    this.eventTimer / 60, this.eventTimer % 60))));
                if (this.eventTimer == 0 && this.farmingTime > 40 && this.generateNewEvent) {
                    this.eventTimer = this.random.nextInt(30) + 40;
                    if(this.eventTimer >= this.farmingTime - 20) {
                       this.generateNewEvent = false;
                       this.bossBar.setTitle("§7Bereitet euch vor!");
                       return;
                    }
                    this.timeToGo = this.eventTimer;
                    try {
                        ILavaScenario lavaScenario = this.scenarios.get(this.random.nextInt(this.scenarios.size())).newInstance();
                        lavaScenario.execute();
                        this.bossBar.setTitle("§7Event §8» §a§l" + lavaScenario.getName());
                    } catch (InstantiationException | IllegalAccessException e) {
                        e.printStackTrace();
                    }
                }
                if(this.farmingTime == 0) {
                    this.floorIsLava.startTimerByClass(RisingTask.class);
                }
                if ((gamestate == Gamestate.RISING || gamestate == Gamestate.FARMING) && Bukkit.getOnlinePlayers().isEmpty()) {
                    Bukkit.getServer().shutdown();
                }

                this.timer++;
                if(this.generateNewEvent) {
                    this.eventTimer--;
                }
                this.farmingTime--;
            }, 0L, 20L);
        }
    }

    public void stop() {
        if (this.isRunning) {
            this.isRunning = false;
            Bukkit.getScheduler().cancelTask(this.taskId);
        }
    }

    private String formatSeconds(int timeInSeconds) {
        int secondsLeft = timeInSeconds % 3600 % 60;
        int minutes = (int) Math.floor(timeInSeconds % 3600.0D / 60.0D);
        int hours = (int) Math.floor(timeInSeconds / 3600.0D);
        int days = (int) Math.floor(timeInSeconds / 3600.0D * 24.0D);
        String HH = ((hours < 10) ? "0" : "") + hours;
        String MM = ((minutes < 10) ? "0" : "") + minutes;
        String SS = ((secondsLeft < 10) ? "0" : "") + secondsLeft;
        return MM + ":" + SS;
    }

    private BarColor getColor(int progress) {
        double fraction = progress / this.timeToGo;
        if (fraction > 0.66D)
            return BarColor.GREEN;
        if (fraction > 0.33D)
            return BarColor.YELLOW;
        return BarColor.RED;
    }

    public BossBar getBossBar() {
        return this.bossBar;
    }
}
