package de.fel1x.teamcrimx.mlgwars.utils;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Entity;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

import java.util.ArrayDeque;
import java.util.HashSet;
import java.util.List;

public class Tornado {

    public static void spawnTornado(
            final JavaPlugin plugin,
            final Location location,
            final Material material,
            final byte data,
            final Vector direction,
            final double speed,
            final int amount_of_blocks,
            final long time,
            final boolean spew,
            final boolean explode
    ) {

        class VortexBlock {

            private final Entity entity;
            public boolean removable = true;
            private float ticker_vertical = 0.0f;
            private float ticker_horisontal = (float) (Math.random() * 2 * Math.PI);

            @SuppressWarnings("deprecation")
            public VortexBlock(Location l, Material m, byte d) {

                if (l.getBlock().getType() != Material.AIR) {

                    Block b = l.getBlock();
                    this.entity = l.getWorld().spawnFallingBlock(l, b.getType(), b.getData());

                    if (b.getType() != Material.WATER)
                        b.setType(Material.AIR);

                    this.removable = !spew;
                } else {
                    this.entity = l.getWorld().spawnFallingBlock(l, m, d);
                    this.removable = !explode;
                }

                addMetadata();
            }

            public VortexBlock(Entity e) {
                this.entity = e;
                this.removable = false;
                addMetadata();
            }

            private void addMetadata() {
                this.entity.setMetadata("vortex", new FixedMetadataValue(plugin, "protected"));
            }

            public void remove() {
                if (this.removable) {
                    this.entity.remove();
                }
                this.entity.removeMetadata("vortex", plugin);
            }

            @SuppressWarnings("deprecation")
            public HashSet<VortexBlock> tick() {

                double radius = Math.sin(verticalTicker()) * 2;
                float horisontal = horisontalTicker();

                Vector v = new Vector(radius * Math.cos(horisontal), 0.5D, radius * Math.sin(horisontal));

                HashSet<VortexBlock> new_blocks = new HashSet<VortexBlock>();

                // Pick up blocks
                Block b = this.entity.getLocation().add(v.clone().normalize()).getBlock();
                if (b.getType() != Material.AIR) {
                    new_blocks.add(new VortexBlock(b.getLocation(), b.getType(), b.getData()));
                }

                // Pick up other entities
                List<Entity> entities = this.entity.getNearbyEntities(1.0D, 1.0D, 1.0D);
                for (Entity e : entities) {
                    if (!e.hasMetadata("vortex")) {
                        new_blocks.add(new VortexBlock(e));
                    }
                }

                setVelocity(v);

                return new_blocks;
            }

            private void setVelocity(Vector v) {
                this.entity.setVelocity(v);
            }

            private float verticalTicker() {
                if (this.ticker_vertical < 1.0f) {
                    this.ticker_vertical += 0.05f;
                }
                return this.ticker_vertical;
            }

            private float horisontalTicker() {
//                ticker_horisontal = (float) ((ticker_horisontal + 0.8f) % 2*Math.PI);
                return (this.ticker_horisontal += 0.8f);
            }
        }

        // Modify the direction vector using the speed argument.
        if (direction != null) {
            direction.normalize().multiply(speed);
        }

        // This set will contain every block created to make sure the metadata for each and everyone is removed.
        final HashSet<VortexBlock> clear = new HashSet<VortexBlock>();

        final int id = new BukkitRunnable() {

            private final ArrayDeque<VortexBlock> blocks = new ArrayDeque<VortexBlock>();

            public void run() {

                if (direction != null) {
                    location.add(direction);
                }

                // Spawns 10 blocks at the time.
                for (int i = 0; i < 10; i++) {
                    checkListSize();
                    VortexBlock vb = new VortexBlock(location, material, data);
                    this.blocks.add(vb);
                    clear.add(vb);
                }

                // Make all blocks in the list spin, and pick up any blocks that get in the way.
                ArrayDeque<VortexBlock> que = new ArrayDeque<VortexBlock>();

                for (VortexBlock vb : this.blocks) {
                    HashSet<VortexBlock> new_blocks = vb.tick();
                    for (VortexBlock temp : new_blocks) {
                        que.add(temp);
                    }
                }

                // Add the new blocks
                for (VortexBlock vb : que) {
                    checkListSize();
                    this.blocks.add(vb);
                    clear.add(vb);
                }
            }

            // Removes the oldest block if the list goes over the limit.
            private void checkListSize() {
                while (this.blocks.size() >= amount_of_blocks) {
                    VortexBlock vb = this.blocks.getFirst();
                    vb.remove();
                    this.blocks.remove(vb);
                    clear.remove(vb);
                }
            }
        }.runTaskTimer(plugin, 5L, 5L).getTaskId();

        // Stop the "tornado" after the given time.
        new BukkitRunnable() {
            public void run() {
                for (VortexBlock vb : clear) {
                    vb.remove();
                }
                plugin.getServer().getScheduler().cancelTask(id);
            }
        }.runTaskLater(plugin, time);
    }

}
