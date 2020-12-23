package de.fel1x.bingo.scenarios;

import de.fel1x.bingo.Bingo;
import de.fel1x.bingo.Data;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.FallingBlock;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.Random;

public class AnvilRain implements IBingoScenario {

    Bingo bingo = Bingo.getInstance();
    Data data = this.bingo.getData();

    Random random = new Random();

    @Override
    public void execute() {

        this.data.getPlayers().forEach(player -> {

            int toGo = this.random.nextInt(5) + 15;

            new BukkitRunnable() {

                int timer = 0;

                @Override
                public void run() {

                    for (int i = 0; i < toGo; i++) {

                        Location currentLocation = player.getLocation();

                        int x = (AnvilRain.this.random.nextInt(7)) * ((AnvilRain.this.random.nextBoolean()) ? 1 : -1);
                        int z = (AnvilRain.this.random.nextInt(7)) * ((AnvilRain.this.random.nextBoolean()) ? 1 : -1);
                        Block blockToChange = currentLocation.add(x, 25, z).getBlock();

                        if (blockToChange.getType() != Material.AIR && blockToChange.getType() != Material.CAVE_AIR)
                            continue;

                        FallingBlock fallingBlock = blockToChange.getLocation().getWorld().spawnFallingBlock(blockToChange.getLocation(),
                                Bukkit.createBlockData(Material.ANVIL));

                        AnvilRain.this.bingo.getFallingAnvils().add(fallingBlock);

                    }

                    if (this.timer == 10) {
                        this.cancel();
                    }

                    this.timer++;

                }
            }.runTaskTimer(this.bingo, 0L, 10L);

        });

    }

    @Override
    public String getName() {
        return "Anvil Rain";
    }

    @Override
    public Material getDisplayMaterial() {
        return Material.ANVIL;
    }
}
