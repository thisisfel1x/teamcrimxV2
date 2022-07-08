package de.fel1x.teamcrimx.mlgwars.maphandler;

import de.fel1x.teamcrimx.crimxapi.utils.ItemBuilder;
import de.fel1x.teamcrimx.mlgwars.MlgWars;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextColor;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.PotionMeta;
import org.bukkit.potion.PotionData;
import org.bukkit.potion.PotionType;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;

public class ChestFiller {

    private final static Random random = new Random();

    public static ArrayList<ItemStack> getLaborItems() {
        ArrayList<ItemStack> items = new ArrayList<>();

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
                    .setPDC(MlgWars.getInstance(), "instatnt", "")
                    .toItemStack());

            items.add(new ItemBuilder(Material.STONE, 20 + random.nextInt(44)).toItemStack());
            items.add(new ItemBuilder(Material.OAK_PLANKS, 20 + random.nextInt(44)).toItemStack());

            items.add(new ItemBuilder(Material.TNT, random.nextInt(5) + 1)
                    .setName("§cVelocity TNT").addGlow().setLore("§7Dieses TNT boostet dich weit")
                    .setPDC(MlgWars.getInstance(), "velocitytnt", "")
                    .toItemStack());
            items.add(new ItemBuilder(Material.TNT, 1 + random.nextInt(2))
                    .setName("§cInstant TNT Boost").setLore("§7Dieses TNT baut einen TNT Boost auf").addGlow()
                    .setPDC(MlgWars.getInstance(), "instaboost", "")
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

            items.add(new ItemBuilder(Material.BOW).addGlow().setName("Explosionsbogen")
                    .setPDC(MlgWars.getInstance(), "explodebow", "").toItemStack());
            items.add(new ItemBuilder(Material.BOW).addGlow().setName("TNT-Bogen")
                    .setPDC(MlgWars.getInstance(), "tntbow", "").toItemStack());

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
                    .setPDC(MlgWars.getInstance(), "snowtnt", "")
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

        return items;
    }

    public static ArrayList<ItemStack> getTier1Items() {
        ArrayList<ItemStack> items = new ArrayList<>();

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

            items.add(new ItemBuilder(Material.STONE, 20 + random.nextInt(44)).toItemStack());
            items.add(new ItemBuilder(Material.BRICKS, 20 + random.nextInt(44)).toItemStack());
            items.add(new ItemBuilder(Material.TNT, 5 + random.nextInt(4)).toItemStack());
            items.add(new ItemBuilder(Material.OAK_PLANKS, 20 + random.nextInt(44)).toItemStack());

        }

        for (int i = 0; i < 3; i++) {

            items.add(new ItemBuilder(Material.EXPERIENCE_BOTTLE, 5 + random.nextInt(10)).toItemStack());
            items.add(new ItemBuilder(Material.LAPIS_LAZULI, 2 + random.nextInt(5)).toItemStack());

        }


        items.add(new ItemBuilder(Material.GOLDEN_APPLE).toItemStack());

        for (int i = 0; i < 5; i++) {

            items.add(new ItemBuilder(Material.DIAMOND, 2 + random.nextInt(4)).toItemStack());

        }

        for (int i = 0; i < 4; i++) {
            items.add(new ItemBuilder(Material.ENDER_PEARL).toItemStack());
        }

        for (int i = 0; i < 6; i++) {

            items.add(new ItemBuilder(Material.IRON_INGOT, 3 + random.nextInt(8)).toItemStack());

        }

        for (int i = 0; i < 5; i++) {

            items.add(new ItemBuilder(Material.COOKED_CHICKEN, 3 + random.nextInt(8)).toItemStack());
            items.add(new ItemBuilder(Material.BEEF, 3 + random.nextInt(8)).toItemStack());
            items.add(new ItemBuilder(Material.COOKED_BEEF, 3 + random.nextInt(8)).toItemStack());
            items.add(new ItemBuilder(Material.PUMPKIN_PIE, 3 + random.nextInt(8)).toItemStack());

        }

        for (int i = 0; i < 6; i++) {

            items.add(new ItemBuilder(Material.STICK, 3 + random.nextInt(8)).toItemStack());
            items.add(new ItemBuilder(Material.LAVA_BUCKET).toItemStack());
            items.add(new ItemBuilder(Material.COMPASS).toItemStack());

        }

        items.add(new ItemBuilder(Material.DIRT)
                .setName(MiniMessage.builder().build().deserialize("<rainbow>Magische Erde</rainbow>"))
                .setLore(Component.empty(), Component.text("Huch? Wie kommt",
                                TextColor.fromHexString("#dda1fc")),
                        Component.text("die denn hier rein?!", TextColor.fromHexString("#dda1fc")))
                .toItemStack());

        for (int i = 0; i < 30; i++) {
            items.add(new ItemBuilder(Material.WATER_BUCKET).toItemStack());
        }

        for (int i = 0; i < 25; i++) {
            items.add(new ItemBuilder(Material.COBWEB, random.nextInt(5) + 3).toItemStack());
        }

        for (int i = 0; i < 5; i++) {
            items.add(new ItemBuilder(Material.WOODEN_SWORD).addEnchant(Enchantment.DAMAGE_ALL, 2).toItemStack());
            items.add(new ItemBuilder(Material.STONE_SWORD).addEnchant(Enchantment.DAMAGE_ALL, 1).toItemStack());
            items.add(new ItemBuilder(Material.GOLDEN_SWORD).addEnchant(Enchantment.DAMAGE_ALL, 1).toItemStack());
        }


        for (int i = 0; i < 5; i++) {
            items.add(new ItemBuilder(Material.FLINT, 1).toItemStack());
            items.add(new ItemBuilder(Material.FLINT_AND_STEEL).toItemStack());

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

        return items;

    }

    public static ArrayList<ItemStack> getTier2Items() {
        ArrayList<ItemStack> items = new ArrayList<>();

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

            items.add(new ItemBuilder(Material.STONE, 20 + random.nextInt(44)).toItemStack());
            items.add(new ItemBuilder(Material.BRICKS, 20 + random.nextInt(44)).toItemStack());
            items.add(new ItemBuilder(Material.TNT, 5 + random.nextInt(4)).toItemStack());
            items.add(new ItemBuilder(Material.OAK_PLANKS, 20 + random.nextInt(44)).toItemStack());

        }
        for (int i = 0; i < 5; i++) {

            items.add(new ItemBuilder(Material.EXPERIENCE_BOTTLE, 5 + random.nextInt(10)).toItemStack());
            items.add(new ItemBuilder(Material.EXPERIENCE_BOTTLE, 5 + random.nextInt(10)).toItemStack());
            items.add(new ItemBuilder(Material.GOLDEN_APPLE).toItemStack());
            items.add(new ItemBuilder(Material.LAPIS_LAZULI, 2 + random.nextInt(5)).toItemStack());

        }

        for (int i = 0; i < 5; i++) {

            items.add(new ItemBuilder(Material.DIAMOND, 2 + random.nextInt(4)).toItemStack());

        }

        for (int i = 0; i < 6; i++) {
            items.add(new ItemBuilder(Material.ENDER_PEARL).toItemStack());
        }

        for (int i = 0; i < 3; i++) {

            items.add(new ItemBuilder(Material.IRON_INGOT, 3 + random.nextInt(8)).toItemStack());

        }

        for (int i = 0; i < 3; i++) {

            items.add(new ItemBuilder(Material.COOKED_CHICKEN, 3 + random.nextInt(8)).toItemStack());
            items.add(new ItemBuilder(Material.BEEF, 3 + random.nextInt(8)).toItemStack());
            items.add(new ItemBuilder(Material.COOKED_BEEF, 3 + random.nextInt(8)).toItemStack());
            items.add(new ItemBuilder(Material.PUMPKIN_PIE, 3 + random.nextInt(8)).toItemStack());

        }

        for (int i = 0; i < 6; i++) {

            items.add(new ItemBuilder(Material.STICK, 3 + random.nextInt(8)).toItemStack());
            items.add(new ItemBuilder(Material.LAVA_BUCKET).toItemStack());
            items.add(new ItemBuilder(Material.COMPASS).toItemStack());

        }

        items.add(new ItemBuilder(Material.DIRT)
                .setName(MiniMessage.builder().build().deserialize("<rainbow>Magische Erde</rainbow>"))
                .setLore(Component.empty(), Component.text("Huch? Wie kommt",
                                TextColor.fromHexString("#dda1fc")),
                        Component.text("die denn hier rein?!", TextColor.fromHexString("#dda1fc")))
                .toItemStack());

        for (int i = 0; i < 20; i++) {
            items.add(new ItemBuilder(Material.WATER_BUCKET).toItemStack());
        }

        for (int i = 0; i < 20; i++) {
            items.add(new ItemBuilder(Material.COBWEB, random.nextInt(5) + 3).toItemStack());
        }

        for (int i = 0; i < 5; i++) {
            items.add(new ItemBuilder(Material.WOODEN_SWORD).addEnchant(Enchantment.DAMAGE_ALL, 2).toItemStack());
        }


        for (int i = 0; i < 7; i++) {
            items.add(new ItemBuilder(Material.FLINT, random.nextInt(2)).toItemStack());
            items.add(new ItemBuilder(Material.FLINT_AND_STEEL).toItemStack());

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

            items.add(new ItemBuilder(Material.SNOWBALL, 10 + random.nextInt(6)).toItemStack());
            items.add(new ItemBuilder(Material.EGG, 10 + random.nextInt(6)).toItemStack());

        }


        Collections.shuffle(items);
        Collections.shuffle(items);
        Collections.shuffle(items);
        Collections.shuffle(items);

        return items;

    }

}
