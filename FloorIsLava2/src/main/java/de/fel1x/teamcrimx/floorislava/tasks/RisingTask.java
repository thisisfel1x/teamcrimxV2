package de.fel1x.teamcrimx.floorislava.tasks;

import de.fel1x.teamcrimx.floorislava.FloorIsLava;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;

public class RisingTask implements IFloorIsLavaTask {
    private final FloorIsLava floorIsLava = FloorIsLava.getInstance();
    private final int increasePerRise = 1;
    private final Location spawnLocation = this.floorIsLava.getWorldSpawnLocation();
    private int delay = 5;
    private int height = 0;
    private Location bottomRight;

    private Location topLeft;

    private boolean isRunning = false;

    private int taskId;

    public void start() {
        if (!this.isRunning) {
            this.isRunning = true;
            int size = 50;
            this.bottomRight = this.spawnLocation.clone().subtract(size / 2.0D, this.height, size / 2.0D);
            this.topLeft = this.spawnLocation.clone().add(size / 2.0D, this.height, size / 2.0D);
            this.taskId = Bukkit.getScheduler().scheduleSyncRepeatingTask(this.floorIsLava, () -> {
                if (this.delay == 0) {
                    World world = this.bottomRight.getWorld();
                    this.bottomRight.setY(this.height);
                    this.topLeft.setY((this.height + 1));
                    Bukkit.broadcastMessage("rise");
                    for (int x = this.bottomRight.getBlockX(); x <= this.topLeft.getBlockX(); x++) {
                        for (int y = this.bottomRight.getBlockY(); y <= this.topLeft.getBlockY(); y++) {
                            for (int z = this.bottomRight.getBlockZ(); z <= this.topLeft.getBlockZ(); z++) {
                                Block block = Bukkit.getWorlds().get(0).getBlockAt(new Location(world, x, y, z));
                                block.setType(Material.LAVA, false);
                            }
                        }
                    }
                    this.height++;
                    this.delay = 5;
                }
                this.delay--;
            }, 0L, 20L);
        }
    }

    public void stop() {
        if (this.isRunning) {
            this.isRunning = false;
            Bukkit.getScheduler().cancelTask(this.taskId);
        }
    }
}
