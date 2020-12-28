package de.fel1x.bingo.utils.world;

import de.fel1x.bingo.Bingo;
import org.bukkit.*;
import org.bukkit.block.Block;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

@Deprecated
public class WorldGenerator {

    private final Material[] glassTypes = {

            Material.WHITE_STAINED_GLASS,
            Material.ORANGE_STAINED_GLASS,
            Material.MAGENTA_STAINED_GLASS,
            Material.LIGHT_BLUE_STAINED_GLASS,
            Material.YELLOW_STAINED_GLASS,
            Material.LIME_STAINED_GLASS,
            Material.PINK_STAINED_GLASS,
            Material.GRAY_STAINED_GLASS,
            Material.LIGHT_GRAY_STAINED_GLASS,
            Material.PURPLE_STAINED_GLASS,
            Material.BLUE_STAINED_GLASS,
            Material.BROWN_STAINED_GLASS,
            Material.GREEN_STAINED_GLASS,
            Material.RED_STAINED_GLASS

    };

    List<Block> blocks;

    public WorldGenerator() {

        Random random = new Random();
        this.blocks = new ArrayList<>();

        World world = Bukkit.getWorlds().get(0);
        Cuboid spawnCuboid = new Cuboid(world, -20, 120, -20, 20, 120, 20);

        Location spawnLocation = new Location(world, 0.5, 121, 0.5);

        world.setSpawnLocation(spawnLocation);

        Chunk worldSpawnLocationChunk = spawnLocation.getChunk();

        for(int i = worldSpawnLocationChunk.getX() - 15; i < worldSpawnLocationChunk.getX() + 15; i++) {
            for(int j = worldSpawnLocationChunk.getZ() - 15; j < worldSpawnLocationChunk.getZ() + 15; j++) {
                Chunk current = world.getChunkAt(i, j);
                current.load(true);
                Bukkit.getConsoleSender().sendMessage(Bingo.getInstance().getPrefix() + "Loading Chunk " + current.getX() + " " + current.getZ());
            }
        }

        for (int i = spawnCuboid.getLowerX(); i < spawnCuboid.getUpperX(); i++) {
            for (int j = spawnCuboid.getLowerZ(); j < spawnCuboid.getUpperZ(); j++) {
                this.blocks.add(world.getBlockAt(i, 120, j));
            }
        }

        this.blocks.remove(world.getBlockAt(0, 120, 0));
        world.getBlockAt(0, 120, 0).setType(Material.QUARTZ_PILLAR);

        for (int i = spawnCuboid.getLowerX(); i < spawnCuboid.getUpperX(); i++) {
            for (int height = 0; height < 5; height++) {
                this.blocks.add(world.getBlockAt(i, 120 + height, spawnCuboid.getLowerZ()));
            }
        }

        for (int i = spawnCuboid.getLowerX(); i < spawnCuboid.getUpperX(); i++) {
            for (int height = 0; height < 5; height++) {
                this.blocks.add(world.getBlockAt(i, 120 + height, spawnCuboid.getUpperZ()));
            }
        }

        for (int i = spawnCuboid.getLowerX(); i < spawnCuboid.getUpperX(); i++) {
            for (int height = 0; height < 5; height++) {
                this.blocks.add(world.getBlockAt(spawnCuboid.getLowerX(), 120 + height, i));
            }
        }

        for (int i = spawnCuboid.getLowerX(); i < spawnCuboid.getUpperX(); i++) {
            for (int height = 0; height < 5; height++) {
                this.blocks.add(world.getBlockAt(spawnCuboid.getUpperX(), 120 + height, i));
            }
        }

        this.blocks.forEach(block -> block.setType(Material.GLASS));

        Bukkit.getScheduler().scheduleSyncRepeatingTask(Bingo.getInstance(),
                () -> this.blocks.forEach(block -> {
                    if (random.nextBoolean() && !random.nextBoolean() && ThreadLocalRandom.current().nextBoolean()) {
                        block.setType(this.glassTypes[random.nextInt(this.glassTypes.length)]);
                    }
                }), 0L, 20L * 2);

    }

    public List<Block> getBlocks() {
        return this.blocks;
    }
}
