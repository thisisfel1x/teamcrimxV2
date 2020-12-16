package de.fel1x.teamcrimx.floorislava.tasks;

import com.destroystokyo.paper.Title;
import de.dytanic.cloudnet.ext.bridge.BridgeHelper;
import de.dytanic.cloudnet.ext.bridge.bukkit.BukkitCloudNetHelper;
import de.fel1x.teamcrimx.floorislava.FloorIsLava;
import de.fel1x.teamcrimx.floorislava.gamehandler.Gamestate;
import de.fel1x.teamcrimx.floorislava.utils.ArmorstandStatsLoader;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Objects;

public class PreGameTask implements IFloorIsLavaTask {
    private final FloorIsLava floorIsLava = FloorIsLava.getInstance();

    private int taskId = 0;

    private int timer = 15;

    private boolean isRunning = false;

    public void start() {
        if (!this.isRunning) {
            BukkitCloudNetHelper.changeToIngame();
            BridgeHelper.updateServiceInfo();
            this.isRunning = true;
            this.floorIsLava.getGamestateHandler().setGamestate(Gamestate.PREGAME);
            ArrayList<Location> spawns = ArmorstandStatsLoader.getCirclePoints(this.floorIsLava.getWorldSpawnLocation(), 20.0D, 12);
            int counter = 0;
            for (Player player : this.floorIsLava.getData().getPlayers()) {
                Location spawnLocation = spawns.get(counter).getWorld().getHighestBlockAt(spawns.get(0)).getLocation();
                spawnLocation.getBlock().setType(player.hasMetadata("block") ?
                                (Material) Objects.requireNonNull(player.getMetadata("block").get(0).value())
                        : Material.GLASS);
                spawnLocation.clone().add(0, 1, 0);
                player.teleport(spawnLocation.toCenterLocation().clone().subtract(0, 0.5, 0));
                counter++;
            }
            this.taskId = Bukkit.getScheduler().scheduleSyncRepeatingTask(this.floorIsLava, () -> {
                switch (this.timer) {
                    case 1:
                    case 2:
                    case 3:
                    case 5:
                    case 10:
                    case 15:
                        Bukkit.broadcastMessage(this.floorIsLava.getPrefix() + "§7Das Spiel startet in §e" + ((this.timer == 1) ? "einer Sekunde" : (this.timer + " Sekunden")));
                        this.floorIsLava.getData().getPlayers().forEach(player -> {

                            player.playSound(player.getLocation(), Sound.BLOCK_NOTE_BLOCK_PLING, 1, 0.75f);
                            player.sendTitle(Title.builder()
                                    .title(((this.timer == 3) ? "§a§l" : ((this.timer == 2) ? "§e§l" : "§c§l")) + this.timer)
                                    .fadeIn(10).stay(20).fadeOut(10).build());

                            player.getInventory().clear();

                        });
                        break;
                    case 0:
                        Bukkit.broadcastMessage(this.floorIsLava.getPrefix() + "§e§lDas Spiel beginnt!");
                        Bukkit.getScheduler().cancelTasks(this.floorIsLava);
                        this.floorIsLava.getGamestateHandler().setGamestate(Gamestate.FARMING);
                        this.floorIsLava.getData().getPlayers().forEach(player -> {
                            player.playSound(player.getLocation(), Sound.ENTITY_GENERIC_EXPLODE, 1.5f, 1.25f);
                            player.sendTitle(Title.builder()
                                    .title("§a§lGO").fadeIn(0).stay(40).fadeOut(10).build());
                        });
                        this.floorIsLava.startTimerByClass(FarmingTask.class);
                        break;
                }
                this.timer--;
            }, 0L, 20L);
        }
    }

    public void stop() {
        if (this.isRunning) {
            this.isRunning = false;
            Bukkit.getScheduler().cancelTask(this.taskId);
            this.timer = 60;
        }
    }
}
