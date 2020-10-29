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
import org.bukkit.inventory.meta.PotionMeta;
import org.bukkit.potion.Potion;
import org.bukkit.potion.PotionData;
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

        if (this.mlgWars.isLabor()) {
            this.addLaborItems(items);
            this.addLaborItems(itemsTier2);
        } else {
            this.addItems(items);
            this.addItemsTier2(itemsTier2);
        }

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
                            is = items.get(random.nextInt(items.size()));
                            current = is.getType();
                        }

                        chestInv.setItem(random.nextInt(chestInv.getSize()), is); // Set the item in the chest to a random place (which is not taken).
                    }
                }
            }
        }
    }

    private void addLaborItems(ArrayList<ItemStack> items) {

        Random random = new Random();

        for (PotionType potionType : PotionType.values()) {
            if (potionType == PotionType.UNCRAFTABLE) continue;
            if (potionType == PotionType.WATER) continue;
            if (potionType == PotionType.MUNDANE) continue;
            if (potionType == PotionType.THICK) continue;
            if (potionType == PotionType.AWKWARD) continue;

            ItemStack potion = new ItemStack(Material.SPLASH_POTION);
            PotionMeta potionMeta = (PotionMeta) potion.getItemMeta();
            potionMeta.setBasePotionData(new PotionData(potionType));
            potion.setItemMeta(potionMeta);

            potion.setAmount(1);

            items.add(potion);
            items.add(potion);
        }


        for (int i = 0; i < 20; i++) {
            items.add(new ItemBuilder(Material.TNT, 5 + random.nextInt(20))
                    .setName("§cInstant TNT").setLore("§7Dieses TNT explodiert sofort")
                    .toItemStack());

            items.add(new ItemBuilder(Material.STONE, 20 + random.nextInt(44)).toItemStack());
            items.add(new ItemBuilder(Material.OAK_PLANKS, 20 + random.nextInt(44)).toItemStack());

            items.add(new ItemBuilder(Material.TNT, random.nextInt(5) + 1)
                    .setName("§cVelocity TNT").addGlow().setLore("§7Dieses TNT boostet dich weit")
                    .toItemStack());
            items.add(new ItemBuilder(Material.TNT, 1 + random.nextInt(2))
                    .setName("§cInstant TNT Boost").setLore("§7Dieses TNT baut einen TNT Boost auf").addGlow()
                    .toItemStack());
            items.add(new ItemBuilder(Material.ARROW, 3 + random.nextInt(6)).toItemStack());
        }

        for (int i = 0; i < 5; i++) {
            items.add(new ItemBuilder(Material.WOODEN_SWORD).addEnchant(Enchantment.DAMAGE_ALL, 2).toItemStack());
            items.add(new ItemBuilder(Material.STONE_SWORD).addEnchant(Enchantment.DAMAGE_ALL, 1).toItemStack());
            items.add(new ItemBuilder(Material.GOLDEN_SWORD).addEnchant(Enchantment.DAMAGE_ALL, 1).toItemStack());

            items.add(new ItemBuilder(Material.CREEPER_SPAWN_EGG).setColor(50).toItemStack());
            items.add(new ItemBuilder(Material.SKELETON_SPAWN_EGG).setColor(61).toItemStack());
            items.add(new ItemBuilder(Material.ZOMBIE_SPAWN_EGG).setColor(60).toItemStack());

            items.add(new ItemBuilder(Material.BOW).addGlow().setName("Explosionsbogen").toItemStack());
            items.add(new ItemBuilder(Material.BOW).addGlow().setName("TNT-Bogen").toItemStack());

            items.add(new ItemBuilder(Material.COOKED_CHICKEN, 3 + random.nextInt(8)).toItemStack());
            items.add(new ItemBuilder(Material.COOKED_BEEF, 3 + random.nextInt(8)).toItemStack());

        }

        for (int i = 0; i < 8; i++) {

            items.add(new ItemBuilder(Material.STICK, 3 + random.nextInt(8)).toItemStack());
            items.add(new ItemBuilder(Material.LAVA_BUCKET).toItemStack());
            items.add(new ItemBuilder(Material.WATER_BUCKET).toItemStack());
            items.add(new ItemBuilder(Material.COBWEB).toItemStack());
            items.add(new ItemBuilder(Material.SNOWBALL, 1 + random.nextInt(4))
                    .setName("§cWerfbares TNT")
                    .addGlow()
                    .setLore("§7Werfe mit TNT")
                    .toItemStack());
            items.add(new ItemBuilder(Material.EGG, 3 + random.nextInt(4))
                    .toItemStack());
            items.add(new ItemBuilder(Material.ENDER_PEARL).toItemStack());
            items.add(new ItemBuilder(Material.COMPASS).toItemStack());
            items.add(new ItemBuilder(Material.GOLDEN_APPLE, 1 + random.nextInt(2)).toItemStack());

        }

        Collections.shuffle(items);

    }

    private void addItems(ArrayList<ItemStack> items) {

        Random r = new Random();

        ItemStack potion = new ItemStack(Material.SPLASH_POTION);
        PotionMeta potionMeta = (PotionMeta) potion.getItemMeta();
        potionMeta.setBasePotionData(new PotionData(PotionType.INSTANT_HEAL));
        potion.setItemMeta(potionMeta);


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
            items.add(new ItemBuilder(Material.BRICKS, 20 + r.nextInt(44)).toItemStack());
            items.add(new ItemBuilder(Material.TNT, 5 + r.nextInt(4)).toItemStack());
            items.add(new ItemBuilder(Material.OAK_PLANKS, 20 + r.nextInt(44)).toItemStack());

        }

        for (int i = 0; i < 3; i++) {

            items.add(new ItemBuilder(Material.EXPERIENCE_BOTTLE, 5 + r.nextInt(10)).toItemStack());
            items.add(new ItemBuilder(Material.LAPIS_LAZULI, 2 + r.nextInt(5)).toItemStack());

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
            items.add(new ItemBuilder(Material.BEEF, 3 + r.nextInt(8)).toItemStack());
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
            items.add(new ItemBuilder(Material.COBWEB, r.nextInt(5) + 3).toItemStack());
        }

        for (int i = 0; i < 5; i++) {
            items.add(new ItemBuilder(Material.WOODEN_SWORD).addEnchant(Enchantment.DAMAGE_ALL, 2).toItemStack());
            items.add(new ItemBuilder(Material.STONE_SWORD).addEnchant(Enchantment.DAMAGE_ALL, 1).toItemStack());
            items.add(new ItemBuilder(Material.GOLDEN_SWORD).addEnchant(Enchantment.DAMAGE_ALL, 1).toItemStack());
        }


        for (int i = 0; i < 5; i++) {
            items.add(new ItemBuilder(Material.FLINT, 1).toItemStack());
            items.add(new ItemBuilder(Material.FLINT_AND_STEEL).setDurability((short) 50).toItemStack());

        }

        items.add(new ItemBuilder(Material.IRON_PICKAXE).addEnchant(Enchantment.DIG_SPEED, 1).toItemStack());
        items.add(new ItemBuilder(Material.IRON_AXE).addEnchant(Enchantment.DIG_SPEED, 1).toItemStack());
        items.add(new ItemBuilder(Material.GOLDEN_PICKAXE).addEnchant(Enchantment.DIG_SPEED, 1).toItemStack());
        items.add(new ItemBuilder(Material.DIAMOND_AXE).addEnchant(Enchantment.DIG_SPEED, 1).toItemStack());
        items.add(new ItemBuilder(Material.DIAMOND_PICKAXE).addEnchant(Enchantment.DIG_SPEED, 1).toItemStack());

        for (int i = 0; i < 3; i++) {

            items.add(new ItemBuilder(Material.CHAINMAIL_HELMET).addEnchant(Enchantment.PROTECTION_ENVIRONMENTAL, 1).toItemStack());
            items.add(new ItemBuilder(Material.CHAINMAIL_CHESTPLATE).addEnchant(Enchantment.PROTECTION_ENVIRONMENTAL, 1).toItemStack());
            items.add(new ItemBuilder(Material.CHAINMAIL_LEGGINGS).addEnchant(Enchantment.PROTECTION_ENVIRONMENTAL, 1).toItemStack());
            items.add(new ItemBuilder(Material.CHAINMAIL_BOOTS).addEnchant(Enchantment.PROTECTION_ENVIRONMENTAL, 1).toItemStack());

        }

        for (int i = 0; i < 4; i++) {
            items.add(new ItemBuilder(Material.GOLDEN_HELMET).addEnchant(Enchantment.PROTECTION_ENVIRONMENTAL, 1).toItemStack());
            items.add(new ItemBuilder(Material.GOLDEN_LEGGINGS).addEnchant(Enchantment.PROTECTION_ENVIRONMENTAL, 1).toItemStack());
            items.add(new ItemBuilder(Material.GOLDEN_BOOTS).addEnchant(Enchantment.PROTECTION_ENVIRONMENTAL, 1).toItemStack());
            items.add(new ItemBuilder(Material.GOLDEN_CHESTPLATE).addEnchant(Enchantment.PROTECTION_ENVIRONMENTAL, 1).toItemStack());
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

        ItemStack potion = new ItemStack(Material.SPLASH_POTION);
        PotionMeta potionMeta = (PotionMeta) potion.getItemMeta();
        potionMeta.setBasePotionData(new PotionData(PotionType.INSTANT_HEAL));
        potion.setItemMeta(potionMeta);


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
            items.add(new ItemBuilder(Material.BRICKS, 20 + r.nextInt(44)).toItemStack());
            items.add(new ItemBuilder(Material.TNT, 5 + r.nextInt(4)).toItemStack());
            items.add(new ItemBuilder(Material.OAK_PLANKS, 20 + r.nextInt(44)).toItemStack());

        }
        for (int i = 0; i < 5; i++) {

            items.add(new ItemBuilder(Material.EXPERIENCE_BOTTLE, 5 + r.nextInt(10)).toItemStack());
            items.add(new ItemBuilder(Material.EXPERIENCE_BOTTLE, 5 + r.nextInt(10)).toItemStack());
            items.add(new ItemBuilder(Material.GOLDEN_APPLE).toItemStack());
            items.add(new ItemBuilder(Material.LAPIS_LAZULI, 2 + r.nextInt(5), (byte) 4).toItemStack());

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
            items.add(new ItemBuilder(Material.BEEF, 3 + r.nextInt(8)).toItemStack());
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
            items.add(new ItemBuilder(Material.COBWEB, r.nextInt(5) + 3).toItemStack());
        }

        for (int i = 0; i < 5; i++) {
            items.add(new ItemBuilder(Material.WOODEN_SWORD).addEnchant(Enchantment.DAMAGE_ALL, 2).toItemStack());
        }


        for (int i = 0; i < 7; i++) {
            items.add(new ItemBuilder(Material.FLINT, r.nextInt(2)).toItemStack());
            items.add(new ItemBuilder(Material.FLINT_AND_STEEL).setDurability((short) 50).toItemStack());

        }

        items.add(new ItemBuilder(Material.IRON_PICKAXE).addEnchant(Enchantment.DIG_SPEED, 1).toItemStack());
        items.add(new ItemBuilder(Material.IRON_AXE).addEnchant(Enchantment.DIG_SPEED, 1).toItemStack());
        items.add(new ItemBuilder(Material.GOLDEN_PICKAXE).addEnchant(Enchantment.DIG_SPEED, 1).toItemStack());
        items.add(new ItemBuilder(Material.DIAMOND_AXE).addEnchant(Enchantment.DIG_SPEED, 1).toItemStack());
        items.add(new ItemBuilder(Material.DIAMOND_PICKAXE).addEnchant(Enchantment.DIG_SPEED, 1).toItemStack());

        for (int i = 0; i < 2; i++) {
            items.add(new ItemBuilder(Material.GOLDEN_HELMET).addEnchant(Enchantment.PROTECTION_ENVIRONMENTAL, 1).toItemStack());
            items.add(new ItemBuilder(Material.GOLDEN_LEGGINGS).addEnchant(Enchantment.PROTECTION_ENVIRONMENTAL, 1).toItemStack());
            items.add(new ItemBuilder(Material.GOLDEN_BOOTS).addEnchant(Enchantment.PROTECTION_ENVIRONMENTAL, 1).toItemStack());
            items.add(new ItemBuilder(Material.GOLDEN_CHESTPLATE).addEnchant(Enchantment.PROTECTION_ENVIRONMENTAL, 1).toItemStack());
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

            items.add(new ItemBuilder(Material.SNOWBALL, 10 + r.nextInt(6)).toItemStack());
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
