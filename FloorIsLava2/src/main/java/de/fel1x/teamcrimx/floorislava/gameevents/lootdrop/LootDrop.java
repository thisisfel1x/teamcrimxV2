package de.fel1x.teamcrimx.floorislava.gameevents.lootdrop;

import de.fel1x.teamcrimx.crimxapi.utils.InstantFirework;
import de.fel1x.teamcrimx.floorislava.FloorIsLava;
import de.fel1x.teamcrimx.floorislava.utils.ArmorstandStatsLoader;
import org.bukkit.*;
import org.bukkit.block.Chest;
import org.bukkit.entity.Chicken;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.FallingBlock;
import org.bukkit.entity.Item;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitTask;
import org.bukkit.util.Vector;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Random;
import java.util.function.Consumer;

public class LootDrop {

    private FloorIsLava floorIsLava;
    private Location origin;
    private LootDropTier lootDropTier = LootDropTier.TIER_1;
    private double speed = 1.0;
    private Material blockType;

    public LootDrop(@NotNull FloorIsLava floorIsLava,@NotNull Location origin, Material material) {
        this.floorIsLava = floorIsLava;
        this.origin = origin;
        this.blockType = material;
    }

    public LootDrop lootDropTier( @NotNull LootDropTier lootDropTier) {
        this.lootDropTier = lootDropTier;
        return this;
    }

    public LootDrop setLootDropSpeed(double speed) {
        this.speed = speed;
        return this;
    }

    public LootDrop build() {
        this.startDrop(this.blockType);
        return this;
    }

    private void startDrop(Material material) {
        World world = this.origin.getWorld();

        FallingBlock fallingChest = world.spawnFallingBlock(this.origin, Bukkit.createBlockData(material));
        fallingChest.setDropItem(false);
        fallingChest.setGlowing(true);
        ArrayList<Chicken> chickens = new ArrayList<>();

        int amount = 21;
        double height = 0;

        for(double radius = 3; radius >= 0; radius-=0.75) {
            double finalHeight = height;
            ArmorstandStatsLoader.getCirclePoints(this.origin.clone().add(0, 2, 0), radius == 0 ? 0.1 : radius, amount).forEach(location -> {
                Chicken chicken = (Chicken) world.spawnEntity(location.clone().add(0, finalHeight, 0), EntityType.CHICKEN);
                chicken.setLeashHolder(fallingChest);
                chicken.setSilent(true);
                chickens.add(chicken);
            });
            amount-= 5;
            height+= radius <= 1.5 ? 0.25 : 0.5;
        }

        Bukkit.getScheduler().runTaskTimer(FloorIsLava.getInstance(), bukkitTask -> {
            Vector toSet = new Vector(0, -0.05, 0);
            fallingChest.setVelocity(toSet);
            chickens.forEach(chicken -> {
                chicken.setVelocity(new Vector(0, -0.08, 0));
                if(chicken.isOnGround()) {
                    chicken.remove();
                    chicken.getLocation().getNearbyEntitiesByType(Item.class, 2).forEach(item -> {
                        if(item.getItemStack().getType() == Material.LEAD) {
                            item.remove();
                        }
                    });
                }
            });
            if(fallingChest.isOnGround()) {
                fallingChest.remove();

                chickens.forEach(chicken -> {
                    chicken.setLeashHolder(null);
                    chicken.remove();
                });

                chickens.get(0).getLocation().getNearbyEntitiesByType(Item.class, 7, 2).forEach(item -> {
                    if(item.getItemStack().getType() == Material.LEAD) {
                        item.remove();
                    }
                });

                this.finishLootDrop(fallingChest.getLocation().toCenterLocation());
                bukkitTask.cancel();
            }
        }, 0L, 0L);

    }

    private void finishLootDrop(Location dropLocation) {
        dropLocation.getBlock().setType(Material.CHEST);

        Chest chest = (Chest) dropLocation.getBlock().getState();
        chest.getBlockInventory().setItem(new Random().nextInt(chest.getBlockInventory().getSize() - 1), new ItemStack(Material.COCOA_BEANS, 3));
        chest.getBlockInventory().setItem(new Random().nextInt(chest.getBlockInventory().getSize() - 1), new ItemStack(Material.WATER_BUCKET));
        chest.getBlockInventory().setItem(new Random().nextInt(chest.getBlockInventory().getSize() - 1), new ItemStack(Material.SANDSTONE, 37));

        chest.setCustomName("§aLootdrop");

        Bukkit.broadcastMessage(String.format("%s§7Ein §aLootdrop (%s, %s) §7ist gelandet!",
                this.floorIsLava.getPrefix(), dropLocation.getBlockX(),
                dropLocation.getBlockZ()));

        new InstantFirework(FireworkEffect.builder().withColor(Color.GREEN, Color.RED, Color.BLUE, Color.YELLOW,
                Color.AQUA, Color.ORANGE, Color.PURPLE, Color.MAROON, Color.FUCHSIA)
                .trail(true).withFade(Color.GREEN, Color.RED, Color.BLUE, Color.YELLOW,
                        Color.AQUA, Color.ORANGE, Color.PURPLE, Color.MAROON, Color.FUCHSIA)
                .build(), dropLocation.clone().add(0, 0.5, 0));


    }


}
