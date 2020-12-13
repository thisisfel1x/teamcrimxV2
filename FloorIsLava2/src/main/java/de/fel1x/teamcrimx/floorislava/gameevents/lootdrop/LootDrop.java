package de.fel1x.teamcrimx.floorislava.gameevents.lootdrop;

import de.fel1x.teamcrimx.crimxapi.utils.InstantFirework;
import de.fel1x.teamcrimx.crimxapi.utils.ItemBuilder;
import de.fel1x.teamcrimx.floorislava.FloorIsLava;
import de.fel1x.teamcrimx.floorislava.utils.ArmorstandStatsLoader;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.block.Chest;
import org.bukkit.entity.Chicken;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.FallingBlock;
import org.bukkit.entity.Item;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionType;
import org.bukkit.util.Vector;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Random;

public class LootDrop {

    private final FloorIsLava floorIsLava;
    private final Location origin;
    private LootDropTier lootDropTier = LootDropTier.TIER_1;
    private double speed = 1.0;
    private Material blockType;
    private final Random random = new Random();

    private final ItemStack[] lootDropItemsTierOne = {
            // BLOCKS
            new ItemStack(Material.SANDSTONE, this.random.nextInt(20) + 20),
            new ItemStack(Material.GREEN_STAINED_GLASS, this.random.nextInt(20) + 20),
            new ItemStack(Material.BLUE_GLAZED_TERRACOTTA, this.random.nextInt(20) + 20),
            new ItemStack(Material.PURPLE_CONCRETE, this.random.nextInt(20) + 20),
            new ItemStack(Material.OAK_PLANKS, this.random.nextInt(20) + 20),

            // FOOD
            new ItemStack(Material.BAKED_POTATO, this.random.nextInt(5) + 2),
            new ItemStack(Material.COOKED_MUTTON, this.random.nextInt(5) + 2),
            new ItemStack(Material.COOKIE, this.random.nextInt(5) + 2),
            new ItemStack(Material.CAKE),

            // STUFF
            new ItemStack(Material.OAK_SAPLING, this.random.nextInt(2) + 1),
            new ItemStack(Material.FARMLAND, this.random.nextInt(6) + 5),
            new ItemStack(Material.WHEAT_SEEDS, this.random.nextInt(4) + 2),
            new ItemStack(Material.BONE, this.random.nextInt(4) + 2),
            new ItemStack(Material.COBWEB, this.random.nextInt(4) + 2),
            new ItemStack(Material.IRON_INGOT, this.random.nextInt(4) + 2),
            new ItemStack(Material.SNOWBALL, this.random.nextInt(2) + 2),
            new ItemStack(Material.WATER_BUCKET),
            new ItemStack(Material.FLINT_AND_STEEL),

            // POTION
            new ItemBuilder(Material.SPLASH_POTION).setPotionEffect(PotionType.INSTANT_HEAL).toItemStack(),
            new ItemBuilder(Material.SPLASH_POTION).setPotionEffect(PotionType.REGEN).toItemStack(),
            new ItemBuilder(Material.SPLASH_POTION).setPotionEffect(PotionType.SLOW_FALLING).toItemStack(),
            new ItemBuilder(Material.SPLASH_POTION).setPotionEffect(PotionType.INVISIBILITY).toItemStack(),
            new ItemBuilder(Material.SPLASH_POTION).setPotionEffect(PotionType.FIRE_RESISTANCE).toItemStack(),
    };

    public LootDrop(@NotNull FloorIsLava floorIsLava,@NotNull Location origin, Material material) {
        this.floorIsLava = floorIsLava;
        this.origin = origin.toCenterLocation();
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
        Block finalBlock = world.getHighestBlockAt(this.origin);

        FallingBlock fallingChest = world.spawnFallingBlock(this.origin, Bukkit.createBlockData(material));
        fallingChest.setDropItem(false);
        fallingChest.setGlowing(true);
        ArrayList<Chicken> chickens = new ArrayList<>();

        if(finalBlock.isLiquid()) {
            finalBlock.setType(Material.RED_CONCRETE);
            finalBlock.getLocation().clone().add(1, 0, 0).getBlock().setType(Material.RED_CONCRETE);
            finalBlock.getLocation().clone().add(-1, 0, 0).getBlock().setType(Material.RED_CONCRETE);
            finalBlock.getLocation().clone().add(0, 0, 1).getBlock().setType(Material.RED_CONCRETE);
            finalBlock.getLocation().clone().add(0, 0, -1).getBlock().setType(Material.RED_CONCRETE);
        }

        int amount = 21;
        double height = 0;

        for(double radius = 3; radius >= 0; radius-=0.75) {
            double finalHeight = height;
            ArmorstandStatsLoader.getCirclePoints(this.origin.clone().add(0, 1.5, 0), radius == 0 ? 0.1 : radius, amount).forEach(location -> {
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
                chicken.setVelocity(new Vector(0, -0.09, 0));
                if(chicken.isOnGround()) {
                    chicken.remove();
                    chicken.getLocation().getNearbyEntitiesByType(Item.class, 2).forEach(item -> {
                        if(item.getItemStack().getType() == Material.LEAD) {
                            item.remove();
                        }
                    });
                }
            });
            if(fallingChest.isOnGround()
                    || fallingChest.getLocation().clone().subtract(0, 1, 0).getBlock().isLiquid()) {
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
        this.fillChest(chest);

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

    private void fillChest(Chest chest) {

        ArrayList<ItemStack> itemsAsList = new ArrayList<>(Arrays.asList(this.lootDropItemsTierOne));
        Collections.shuffle(itemsAsList);

        boolean[] chosen = new boolean[chest.getBlockInventory().getSize()]; // This checks which slots are already taken in the inventory.

        int loot = this.random.nextInt(3);
        for (int i = 0; i < (3 + loot); i++) {

            Inventory chestInv = chest.getBlockInventory();

            int slot;

            do {
                slot = this.random.nextInt(chestInv.getSize());
            } while (chosen[slot]); // Make sure the slot does not already have an item in it.

            chosen[slot] = true;
            ItemStack is = itemsAsList.get(this.random.nextInt(itemsAsList.size()));
            Material current = is.getType();

            while (chest.getBlockInventory().contains(current)) {
                is = itemsAsList.get(this.random.nextInt(itemsAsList.size()));
                current = is.getType();
            }

            chestInv.setItem(this.random.nextInt(chestInv.getSize()), is); // Set the item in the chest to a random place (which is not taken).
        }
    }
}
