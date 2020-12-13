package de.fel1x.teamcrimx.floorislava.scenarios;

import de.fel1x.teamcrimx.floorislava.Data;
import de.fel1x.teamcrimx.floorislava.FloorIsLava;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.FallingBlock;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.Random;

public class AnvilRain implements ILavaScenario {
    private final FloorIsLava floorIsLava = FloorIsLava.getInstance();
    private final Data data = this.floorIsLava.getData();

    private final Random random = new Random();

    public void execute() {
        this.data.getPlayers().forEach(player -> {
            final int toGo = this.random.nextInt(5) + 15;
            (new BukkitRunnable() {
                int timer = 0;

                public void run() {
                    for (int i = 0; i < toGo; i++) {
                        Location currentLocation = player.getLocation();
                        int x = AnvilRain.this.random.nextInt(7) * (AnvilRain.this.random.nextBoolean() ? 1 : -1);
                        int z = AnvilRain.this.random.nextInt(7) * (AnvilRain.this.random.nextBoolean() ? 1 : -1);
                        Block blockToChange = currentLocation.add(x, 25.0D, z).getBlock();
                        if (blockToChange.getType() == Material.AIR || blockToChange.getType() == Material.CAVE_AIR) {
                            FallingBlock fallingBlock = blockToChange.getLocation().getWorld().spawnFallingBlock(blockToChange.getLocation(),
                                    Bukkit.createBlockData(Material.ANVIL));
                            fallingBlock.setMetadata("custom", new FixedMetadataValue(AnvilRain.this.floorIsLava, true));
                            AnvilRain.this.floorIsLava.getFallingAnvils().add(fallingBlock);
                        }
                    }
                    if (this.timer == 5)
                        cancel();
                    this.timer++;
                }
            }).runTaskTimer(this.floorIsLava, 0L, 10L);
        });
    }

    public String getName() {
        return "Anvil Rain";
    }

    public Material getDisplayMaterial() {
        return Material.ANVIL;
    }
}
