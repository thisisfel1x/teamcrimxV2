package de.fel1x.teamcrimx.floorislava.tasks;

import de.fel1x.teamcrimx.crimxapi.utils.Cuboid;
import de.fel1x.teamcrimx.floorislava.FloorIsLava;
import de.fel1x.teamcrimx.floorislava.gameevents.lootdrop.LootDrop;
import de.fel1x.teamcrimx.floorislava.gamehandler.Gamestate;
import de.fel1x.teamcrimx.floorislava.scenarios.*;
import org.bukkit.Bukkit;
import org.bukkit.Color;
import org.bukkit.Location;
import org.bukkit.Material;
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
    private final List<Class<? extends ILavaScenario>> scenarios = Arrays.asList(EnderGamesTeleport.class, InventoryShuffle.class, RandomPotionEffect.class);
    private final Random random = new Random();
    private int taskId = 0;
    private int bossBarTimer;
    private int farmingTime = 60;
    private int lootDropTime = this.random.nextInt(10) + this.farmingTime - 40;
    private boolean isRunning = false;
    private boolean generateNewEvent = true;
    private BossBar bossBar;
    private double timeToGo;

    public void start() {
        this.bossBar = this.floorIsLava.getServer().createBossBar(String.format("§7Nächstes Event in §e%s Sekunden", this.bossBarTimer),
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

            /*
            Generates a random time for the scenario task
             */
            this.bossBarTimer = this.random.nextInt(20) + 30;
            this.timeToGo = this.bossBarTimer;

            this.taskId = Bukkit.getScheduler().scheduleSyncRepeatingTask(this.floorIsLava, () -> {
                for (Player onlinePlayer : Bukkit.getOnlinePlayers()) {
                    onlinePlayer.sendActionBar("§7Verbleibende Zeit §8● §a" + this.formatSeconds(this.farmingTime));
                }

                if(this.bossBarTimer > 0 && this.generateNewEvent) {
                    this.bossBar.setColor(this.getColor(this.bossBarTimer));
                    this.bossBar.setProgress((this.bossBarTimer / this.timeToGo >= 0 ? this.bossBarTimer / this.timeToGo : 0));
                    this.bossBar.setTitle(String.format("§7Nächstes Event in §e%s",
                            (this.bossBarTimer == 1) ? "einer Sekunde" : ((this.bossBarTimer <= 60)
                                    ? (this.bossBarTimer + " Sekunden") : String.format("%02d:%02d",
                                    this.bossBarTimer / 60, this.bossBarTimer % 60))));
                } else if(this.bossBarTimer == 0 && this.generateNewEvent) {
                    try {
                        ILavaScenario lavaScenario = this.scenarios.get(this.random.nextInt(this.scenarios.size())).newInstance();
                        lavaScenario.execute();
                        this.bossBar.setTitle("§7Event §8● §a§l" + lavaScenario.getName());
                    } catch (InstantiationException | IllegalAccessException e) {
                        e.printStackTrace();
                    }
                    /*
                    Generating a new number
                     */
                    if(this.farmingTime > 60) {
                        this.bossBarTimer = this.random.nextInt(20) + 30;
                        this.timeToGo = this.bossBarTimer;
                    } else if(this.farmingTime > 20) {
                        this.bossBarTimer = this.random.nextInt(5) + 10;
                        this.timeToGo = this.bossBarTimer;
                    } else {
                        this.generateNewEvent = false;
                        this.bossBar.setTitle("§5Bereitet euch vor!");
                        this.bossBar.setColor(BarColor.PURPLE);
                        this.bossBar.setProgress(1.0);
                    }
                }

                if(this.farmingTime == 0) {
                    this.bossBar.setTitle("§6TheFloorIsLava");
                    this.bossBar.setColor(BarColor.YELLOW);

                    this.floorIsLava.startTimerByClass(RisingTask.class);
                }

                if(this.lootDropTime == 0) {
                    this.lootDropTime = this.random.nextInt(20) + 10;

                    Location randomLocation = this.floorIsLava.getLootDropCuboid().getBlocks()
                            .get(this.random.nextInt(this.floorIsLava.getLootDropCuboid().getBlocks().size())).getLocation();
                    int highestBlock = randomLocation.getWorld().getHighestBlockYAt(randomLocation);
                    randomLocation.setY(highestBlock + 40);
                    new LootDrop(this.floorIsLava, randomLocation, Material.BARREL).build();

                    Bukkit.broadcastMessage(this.floorIsLava.getPrefix() + "§7Ein §aLootdrop §7ist gespawnt!");
                }

                this.lootDropTime--;
                this.bossBarTimer--;
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
