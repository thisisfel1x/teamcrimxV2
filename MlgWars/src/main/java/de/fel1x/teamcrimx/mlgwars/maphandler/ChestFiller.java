package de.fel1x.teamcrimx.mlgwars.maphandler;

import de.fel1x.teamcrimx.crimxapi.utils.Cuboid;
import de.fel1x.teamcrimx.crimxapi.utils.ItemBuilder;
import de.fel1x.teamcrimx.mlgwars.MlgWars;
import de.fel1x.teamcrimx.mlgwars.enums.Spawns;
import org.bukkit.Chunk;
import org.bukkit.Material;
import org.bukkit.block.BlockState;
import org.bukkit.block.Chest;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.Potion;
import org.bukkit.potion.PotionType;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;

public class ChestFiller {

    private final MlgWars mlgWars = MlgWars.getInstance();
    private ArrayList<ItemStack> items;
    private ArrayList<ItemStack> itemsTier2;

    public ChestFiller() {

        if (this.mlgWars.isNoMap()) return;

        Random random = new Random(); // Will be the instance of the random chance to generate all our random numbers.

        items = new ArrayList<>();
        itemsTier2 = new ArrayList<>();
        this.addItems(items);
        this.addItemsTier2(itemsTier2);

        Cuboid cuboid = new Cuboid(Spawns.LOC_1.getLocation(), Spawns.LOC_2.getLocation());
        Cuboid middleCube = new Cuboid(Spawns.MIDDLE_1.getLocation(), Spawns.MIDDLE_2.getLocation());

        for (Chunk c : cuboid.getChunks()) {
            for (BlockState b : c.getTileEntities()) {

                if (b instanceof Chest) {

                    Chest chest = (Chest) b;

                    if (b.getBlock().getType() != Material.CHEST) continue;

                    chest.getBlockInventory().clear();

                    Collections.shuffle(items);

                    boolean[] chosen = new boolean[chest.getBlockInventory().getSize()]; // This checks which slots are already taken in the inventory.

                    int loot = random.nextInt(7);
                    for (int i = 0; i < (8 + loot); i++) {

                        Inventory chestInv = chest.getBlockInventory();

                        int slot;

                        do {
                            slot = random.nextInt(chestInv.getSize());
                        } while (chosen[slot]); // Make sure the slot does not already have an item in it.

                        chosen[slot] = true;
                        ItemStack is = items.get(random.nextInt(items.size()));
                        Material current = is.getType();

                        while (chest.getBlockInventory().contains(current)) {
                            is = items.get(random.nextInt(items.size()));
                            current = is.getType();
                        }

                        chestInv.setItem(random.nextInt(chestInv.getSize()), is); // Set the item in the chest to a random place (which is not taken).
                    }
                }
            }
        }

        for (Chunk c1 : middleCube.getChunks()) {
            for (BlockState b : c1.getTileEntities()) {

                if (b instanceof Chest) {

                    Chest chest = (Chest) b;
                    chest.getBlockInventory().clear();

                    Collections.shuffle(itemsTier2);

                    boolean[] chosen = new boolean[chest.getBlockInventory().getSize()]; // This checks which slots are already taken in the inventory.

                    int loot = random.nextInt(7);
                    for (int i = 0; i < (10 + loot); i++) {

                        Inventory chestInv = chest.getBlockInventory();

                        int slot;

                        do {
                            slot = random.nextInt(chestInv.getSize());
                        } while (chosen[slot]); // Make sure the slot does not already have an item in it.

                        chosen[slot] = true;
                        ItemStack is = itemsTier2.get(random.nextInt(itemsTier2.size()));
                        Material current = is.getType();

                        while (chest.getBlockInventory().contains(current)) {
                            is = itemsTier2.get(random.nextInt(itemsTier2.size()));
                            current = is.getType();
                        }

                        chestInv.setItem(random.nextInt(chestInv.getSize()), is); // Set the item in the chest to a random place (which is not taken).
                    }
                }
            }
        }
    }

    private void addItems(ArrayList<ItemStack> items) {

        Random r = new Random();

        ItemStack potion = new ItemStack(Material.POTION);
        Potion pot = new Potion(PotionType.INSTANT_HEAL, 1);
        pot.setSplash(true);
        pot.apply(potion);


        for (int i = 0; i < 2; i++) {

            potion.setAmount(1);

            items.add(potion);
            items.add(potion);
            items.add(potion);

            potion.setAmount(2);

            items.add(potion);
            items.add(potion);

            potion.setAmount(3);

            items.add(potion);


        }

        for (int i = 0; i < 20; i++) {

            items.add(new ItemBuilder(Material.STONE, 20 + r.nextInt(44)).toItemStack());
            items.add(new ItemBuilder(Material.BRICK, 20 + r.nextInt(44)).toItemStack());
            items.add(new ItemBuilder(Material.TNT, 5 + r.nextInt(4)).toItemStack());
            items.add(new ItemBuilder(Material.WOOD, 20 + r.nextInt(44)).toItemStack());

        }
        for (int i = 0; i < 3; i++) {

            items.add(new ItemBuilder(Material.EXP_BOTTLE, 5 + r.nextInt(10)).toItemStack());
            items.add(new ItemBuilder(Material.INK_SACK, 2 + r.nextInt(5), (byte) 4).toItemStack());

        }


        items.add(new ItemBuilder(Material.GOLDEN_APPLE).toItemStack());

        for (int i = 0; i < 5; i++) {

            items.add(new ItemBuilder(Material.DIAMOND, 2 + r.nextInt(4)).toItemStack());

        }

        for (int i = 0; i < 4; i++) {
            items.add(new ItemBuilder(Material.ENDER_PEARL).toItemStack());
        }

        for (int i = 0; i < 6; i++) {

            items.add(new ItemBuilder(Material.IRON_INGOT, 3 + r.nextInt(8)).toItemStack());

        }

        for (int i = 0; i < 5; i++) {

            items.add(new ItemBuilder(Material.COOKED_CHICKEN, 3 + r.nextInt(8)).toItemStack());
            items.add(new ItemBuilder(Material.RAW_BEEF, 3 + r.nextInt(8)).toItemStack());
            items.add(new ItemBuilder(Material.COOKED_BEEF, 3 + r.nextInt(8)).toItemStack());
            items.add(new ItemBuilder(Material.PUMPKIN_PIE, 3 + r.nextInt(8)).toItemStack());

        }

        for (int i = 0; i < 6; i++) {

            items.add(new ItemBuilder(Material.STICK, 3 + r.nextInt(8)).toItemStack());
            items.add(new ItemBuilder(Material.LAVA_BUCKET).toItemStack());
            items.add(new ItemBuilder(Material.COMPASS).toItemStack());

        }

        items.add(new ItemBuilder(Material.DIRT).setName("§f§l§kI§r §aM§6a§5g§e§ci§bs§1c§3h§2e §4E§5r§8d§9e §r§f§l§kI")
                .setLore("§7Huch! Wie kommt", "§7die denn hier rein?!").toItemStack());

        for (int i = 0; i < 30; i++) {
            items.add(new ItemBuilder(Material.WATER_BUCKET).toItemStack());
        }

        for (int i = 0; i < 25; i++) {
            items.add(new ItemBuilder(Material.WEB, r.nextInt(5) + 3).toItemStack());
        }

        for (int i = 0; i < 5; i++) {
            items.add(new ItemBuilder(Material.WOOD_SWORD).addEnchant(Enchantment.DAMAGE_ALL, 2).toItemStack());
            items.add(new ItemBuilder(Material.STONE_SWORD).addEnchant(Enchantment.DAMAGE_ALL, 1).toItemStack());
            items.add(new ItemBuilder(Material.GOLD_SWORD).addEnchant(Enchantment.DAMAGE_ALL, 1).toItemStack());
        }


        for (int i = 0; i < 5; i++) {
            items.add(new ItemBuilder(Material.FLINT, 2 + r.nextInt(2)).toItemStack());
            items.add(new ItemBuilder(Material.FLINT_AND_STEEL).setDurability((short) 50).toItemStack());

        }

        items.add(new ItemBuilder(Material.IRON_PICKAXE).addEnchant(Enchantment.DIG_SPEED, 1).toItemStack());
        items.add(new ItemBuilder(Material.IRON_AXE).addEnchant(Enchantment.DIG_SPEED, 1).toItemStack());
        items.add(new ItemBuilder(Material.GOLD_PICKAXE).addEnchant(Enchantment.DIG_SPEED, 1).toItemStack());
        items.add(new ItemBuilder(Material.DIAMOND_AXE).addEnchant(Enchantment.DIG_SPEED, 1).toItemStack());
        items.add(new ItemBuilder(Material.DIAMOND_PICKAXE).addEnchant(Enchantment.DIG_SPEED, 1).toItemStack());

        for (int i = 0; i < 3; i++) {

            items.add(new ItemBuilder(Material.CHAINMAIL_HELMET).addEnchant(Enchantment.PROTECTION_ENVIRONMENTAL, 1).toItemStack());
            items.add(new ItemBuilder(Material.CHAINMAIL_CHESTPLATE).addEnchant(Enchantment.PROTECTION_ENVIRONMENTAL, 1).toItemStack());
            items.add(new ItemBuilder(Material.CHAINMAIL_LEGGINGS).addEnchant(Enchantment.PROTECTION_ENVIRONMENTAL, 1).toItemStack());
            items.add(new ItemBuilder(Material.CHAINMAIL_BOOTS).addEnchant(Enchantment.PROTECTION_ENVIRONMENTAL, 1).toItemStack());

        }

        for (int i = 0; i < 4; i++) {
            items.add(new ItemBuilder(Material.GOLD_HELMET).addEnchant(Enchantment.PROTECTION_ENVIRONMENTAL, 1).toItemStack());
            items.add(new ItemBuilder(Material.GOLD_LEGGINGS).addEnchant(Enchantment.PROTECTION_ENVIRONMENTAL, 1).toItemStack());
            items.add(new ItemBuilder(Material.GOLD_BOOTS).addEnchant(Enchantment.PROTECTION_ENVIRONMENTAL, 1).toItemStack());
            items.add(new ItemBuilder(Material.GOLD_CHESTPLATE).addEnchant(Enchantment.PROTECTION_ENVIRONMENTAL, 1).toItemStack());
        }

        for (int i = 0; i < 2; i++) {

            items.add(new ItemBuilder(Material.IRON_HELMET).addEnchant(Enchantment.PROTECTION_ENVIRONMENTAL, 2).toItemStack());
            items.add(new ItemBuilder(Material.IRON_BOOTS).addEnchant(Enchantment.PROTECTION_ENVIRONMENTAL, 2).toItemStack());
            items.add(new ItemBuilder(Material.IRON_LEGGINGS).addEnchant(Enchantment.PROTECTION_ENVIRONMENTAL, 2).toItemStack());
            items.add(new ItemBuilder(Material.IRON_CHESTPLATE).addEnchant(Enchantment.PROTECTION_ENVIRONMENTAL, 2).toItemStack());

        }


        for (int i = 0; i < 2; i++) {

            items.add(new ItemBuilder(Material.LEATHER_CHESTPLATE).toItemStack());
            items.add(new ItemBuilder(Material.LEATHER_CHESTPLATE).toItemStack());
            items.add(new ItemBuilder(Material.LEATHER_HELMET).toItemStack());
            items.add(new ItemBuilder(Material.LEATHER_LEGGINGS).toItemStack());
            items.add(new ItemBuilder(Material.LEATHER_BOOTS).toItemStack());

        }


        items.add(new ItemBuilder(Material.DIAMOND_HELMET).addEnchant(Enchantment.PROTECTION_ENVIRONMENTAL, 1).toItemStack());
        items.add(new ItemBuilder(Material.DIAMOND_BOOTS).addEnchant(Enchantment.PROTECTION_ENVIRONMENTAL, 1).toItemStack());
        items.add(new ItemBuilder(Material.DIAMOND_CHESTPLATE).addEnchant(Enchantment.PROTECTION_ENVIRONMENTAL, 1).toItemStack());
        items.add(new ItemBuilder(Material.DIAMOND_LEGGINGS).addEnchant(Enchantment.PROTECTION_ENVIRONMENTAL, 1).toItemStack());


        Collections.shuffle(items);
        Collections.shuffle(items);
        Collections.shuffle(items);
        Collections.shuffle(items);

    }

    public void addItemsTier2(ArrayList<ItemStack> items) {
        Random r = new Random();

        ItemStack potion = new ItemStack(Material.POTION);
        Potion pot = new Potion(PotionType.INSTANT_HEAL, 1);
        pot.setSplash(true);
        pot.apply(potion);


        for (int i = 0; i < 2; i++) {

            potion.setAmount(1);

            items.add(potion);
            items.add(potion);
            items.add(potion);

            potion.setAmount(2);

            items.add(potion);
            items.add(potion);

            potion.setAmount(3);

            items.add(potion);


        }

        for (int i = 0; i < 10; i++) {

            items.add(new ItemBuilder(Material.STONE, 20 + r.nextInt(44)).toItemStack());
            items.add(new ItemBuilder(Material.BRICK, 20 + r.nextInt(44)).toItemStack());
            items.add(new ItemBuilder(Material.TNT, 5 + r.nextInt(4)).toItemStack());
            items.add(new ItemBuilder(Material.WOOD, 20 + r.nextInt(44)).toItemStack());

        }
        for (int i = 0; i < 5; i++) {

            items.add(new ItemBuilder(Material.EXP_BOTTLE, 5 + r.nextInt(10)).toItemStack());
            items.add(new ItemBuilder(Material.EXP_BOTTLE, 5 + r.nextInt(10)).toItemStack());
            items.add(new ItemBuilder(Material.GOLDEN_APPLE).toItemStack());
            items.add(new ItemBuilder(Material.INK_SACK, 2 + r.nextInt(5), (byte) 4).toItemStack());

        }

        for (int i = 0; i < 5; i++) {

            items.add(new ItemBuilder(Material.DIAMOND, 2 + r.nextInt(4)).toItemStack());

        }

        for (int i = 0; i < 6; i++) {
            items.add(new ItemBuilder(Material.ENDER_PEARL).toItemStack());
        }

        for (int i = 0; i < 3; i++) {

            items.add(new ItemBuilder(Material.IRON_INGOT, 3 + r.nextInt(8)).toItemStack());

        }

        for (int i = 0; i < 3; i++) {

            items.add(new ItemBuilder(Material.COOKED_CHICKEN, 3 + r.nextInt(8)).toItemStack());
            items.add(new ItemBuilder(Material.RAW_BEEF, 3 + r.nextInt(8)).toItemStack());
            items.add(new ItemBuilder(Material.COOKED_BEEF, 3 + r.nextInt(8)).toItemStack());
            items.add(new ItemBuilder(Material.PUMPKIN_PIE, 3 + r.nextInt(8)).toItemStack());

        }

        for (int i = 0; i < 6; i++) {

            items.add(new ItemBuilder(Material.STICK, 3 + r.nextInt(8)).toItemStack());
            items.add(new ItemBuilder(Material.LAVA_BUCKET).toItemStack());
            items.add(new ItemBuilder(Material.COMPASS).toItemStack());

        }

        items.add(new ItemBuilder(Material.DIRT).setName("§f§l§kI§r §aM§6a§5g§e§ci§bs§1c§3h§2e §4E§5r§8d§9e §r§f§l§kI")
                .setLore("§7Huch! Wie kommt", "§7die denn hier rein?!").toItemStack());

        for (int i = 0; i < 20; i++) {
            items.add(new ItemBuilder(Material.WATER_BUCKET).toItemStack());
        }

        for (int i = 0; i < 20; i++) {
            items.add(new ItemBuilder(Material.WEB, r.nextInt(5) + 3).toItemStack());
        }

        for (int i = 0; i < 5; i++) {
            items.add(new ItemBuilder(Material.WOOD_SWORD).addEnchant(Enchantment.DAMAGE_ALL, 2).toItemStack());
        }


        for (int i = 0; i < 7; i++) {
            items.add(new ItemBuilder(Material.FLINT, r.nextInt(2)).toItemStack());
            items.add(new ItemBuilder(Material.FLINT_AND_STEEL).setDurability((short) 50).toItemStack());

        }

        items.add(new ItemBuilder(Material.IRON_PICKAXE).addEnchant(Enchantment.DIG_SPEED, 1).toItemStack());
        items.add(new ItemBuilder(Material.IRON_AXE).addEnchant(Enchantment.DIG_SPEED, 1).toItemStack());
        items.add(new ItemBuilder(Material.GOLD_PICKAXE).addEnchant(Enchantment.DIG_SPEED, 1).toItemStack());
        items.add(new ItemBuilder(Material.DIAMOND_AXE).addEnchant(Enchantment.DIG_SPEED, 1).toItemStack());
        items.add(new ItemBuilder(Material.DIAMOND_PICKAXE).addEnchant(Enchantment.DIG_SPEED, 1).toItemStack());

        for (int i = 0; i < 2; i++) {
            items.add(new ItemBuilder(Material.GOLD_HELMET).addEnchant(Enchantment.PROTECTION_ENVIRONMENTAL, 1).toItemStack());
            items.add(new ItemBuilder(Material.GOLD_LEGGINGS).addEnchant(Enchantment.PROTECTION_ENVIRONMENTAL, 1).toItemStack());
            items.add(new ItemBuilder(Material.GOLD_BOOTS).addEnchant(Enchantment.PROTECTION_ENVIRONMENTAL, 1).toItemStack());
            items.add(new ItemBuilder(Material.GOLD_CHESTPLATE).addEnchant(Enchantment.PROTECTION_ENVIRONMENTAL, 1).toItemStack());
        }

        for (int i = 0; i < 3; i++) {

            items.add(new ItemBuilder(Material.IRON_HELMET).addEnchant(Enchantment.PROTECTION_ENVIRONMENTAL, 2).toItemStack());
            items.add(new ItemBuilder(Material.IRON_BOOTS).addEnchant(Enchantment.PROTECTION_ENVIRONMENTAL, 2).toItemStack());
            items.add(new ItemBuilder(Material.IRON_LEGGINGS).addEnchant(Enchantment.PROTECTION_ENVIRONMENTAL, 2).toItemStack());
            items.add(new ItemBuilder(Material.IRON_CHESTPLATE).addEnchant(Enchantment.PROTECTION_ENVIRONMENTAL, 2).toItemStack());

        }


        for (int i = 0; i < 5; i++) {

            items.add(new ItemBuilder(Material.DIAMOND_HELMET).addEnchant(Enchantment.PROTECTION_ENVIRONMENTAL, 1).toItemStack());
            items.add(new ItemBuilder(Material.DIAMOND_BOOTS).addEnchant(Enchantment.PROTECTION_ENVIRONMENTAL, 1).toItemStack());
            items.add(new ItemBuilder(Material.DIAMOND_CHESTPLATE).addEnchant(Enchantment.PROTECTION_ENVIRONMENTAL, 1).toItemStack());
            items.add(new ItemBuilder(Material.DIAMOND_LEGGINGS).addEnchant(Enchantment.PROTECTION_ENVIRONMENTAL, 1).toItemStack());

            items.add(new ItemBuilder(Material.SNOW_BALL, 10 + r.nextInt(6)).toItemStack());
            items.add(new ItemBuilder(Material.EGG, 10 + r.nextInt(6)).toItemStack());

        }


        Collections.shuffle(items);
        Collections.shuffle(items);
        Collections.shuffle(items);
        Collections.shuffle(items);

    }

    public ArrayList<ItemStack> getItems() {
        return items;
    }

    public ArrayList<ItemStack> getItemsTier2() {
        return itemsTier2;
    }

}
