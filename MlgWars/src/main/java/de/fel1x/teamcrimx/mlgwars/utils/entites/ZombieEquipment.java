package de.fel1x.teamcrimx.mlgwars.utils.entites;

import de.fel1x.teamcrimx.crimxapi.utils.ItemBuilder;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;

public class ZombieEquipment {

    private final ItemStack[] swords = {
            new ItemStack(Material.WOODEN_AXE),
            new ItemBuilder(Material.WOODEN_SWORD).addEnchant(org.bukkit.enchantments.Enchantment.DAMAGE_ALL, 2).toItemStack(),
            new ItemBuilder(org.bukkit.Material.STONE_SWORD).addEnchant(org.bukkit.enchantments.Enchantment.DAMAGE_ALL, 1).toItemStack(),
            new ItemBuilder(org.bukkit.Material.GOLDEN_SWORD).addEnchant(org.bukkit.enchantments.Enchantment.DAMAGE_ALL, 1).toItemStack(),
    };

    private final ItemStack[] helmets = {

            new ItemBuilder(org.bukkit.Material.CHAINMAIL_HELMET).addEnchant(org.bukkit.enchantments.Enchantment.PROTECTION_ENVIRONMENTAL, 1).toItemStack(),
            new ItemBuilder(org.bukkit.Material.GOLDEN_HELMET).addEnchant(org.bukkit.enchantments.Enchantment.PROTECTION_ENVIRONMENTAL, 1).toItemStack(),
            new ItemBuilder(org.bukkit.Material.IRON_HELMET).addEnchant(org.bukkit.enchantments.Enchantment.PROTECTION_ENVIRONMENTAL, 2).toItemStack(),
            new ItemBuilder(org.bukkit.Material.DIAMOND_HELMET).addEnchant(org.bukkit.enchantments.Enchantment.PROTECTION_ENVIRONMENTAL, 1).toItemStack(),
            new ItemStack(Material.CARVED_PUMPKIN),
            new ItemStack(Material.GLASS),
            null

    };

    private final ItemStack[] chestplates = {
            new ItemBuilder(org.bukkit.Material.CHAINMAIL_CHESTPLATE).addEnchant(org.bukkit.enchantments.Enchantment.PROTECTION_ENVIRONMENTAL, 1).toItemStack(),
            new ItemBuilder(org.bukkit.Material.GOLDEN_LEGGINGS).addEnchant(org.bukkit.enchantments.Enchantment.PROTECTION_ENVIRONMENTAL, 1).toItemStack(),
            new ItemBuilder(org.bukkit.Material.IRON_BOOTS).addEnchant(org.bukkit.enchantments.Enchantment.PROTECTION_ENVIRONMENTAL, 2).toItemStack(),
            new ItemBuilder(org.bukkit.Material.DIAMOND_BOOTS).addEnchant(org.bukkit.enchantments.Enchantment.PROTECTION_ENVIRONMENTAL, 1).toItemStack(),
            null

    };

    private final ItemStack[] leggins = {
            new ItemBuilder(org.bukkit.Material.CHAINMAIL_LEGGINGS).addEnchant(org.bukkit.enchantments.Enchantment.PROTECTION_ENVIRONMENTAL, 1).toItemStack(),
            new ItemBuilder(org.bukkit.Material.GOLDEN_BOOTS).addEnchant(org.bukkit.enchantments.Enchantment.PROTECTION_ENVIRONMENTAL, 1).toItemStack(),
            new ItemBuilder(org.bukkit.Material.IRON_LEGGINGS).addEnchant(org.bukkit.enchantments.Enchantment.PROTECTION_ENVIRONMENTAL, 2).toItemStack(),
            new ItemBuilder(org.bukkit.Material.DIAMOND_CHESTPLATE).addEnchant(org.bukkit.enchantments.Enchantment.PROTECTION_ENVIRONMENTAL, 1).toItemStack(),
            null

    };

    private final ItemStack[] shoes = {
            new ItemBuilder(org.bukkit.Material.CHAINMAIL_BOOTS).addEnchant(org.bukkit.enchantments.Enchantment.PROTECTION_ENVIRONMENTAL, 1).toItemStack(),
            new ItemBuilder(org.bukkit.Material.GOLDEN_CHESTPLATE).addEnchant(org.bukkit.enchantments.Enchantment.PROTECTION_ENVIRONMENTAL, 1).toItemStack(),
            new ItemBuilder(org.bukkit.Material.IRON_CHESTPLATE).addEnchant(org.bukkit.enchantments.Enchantment.PROTECTION_ENVIRONMENTAL, 2).toItemStack(),
            new ItemBuilder(Material.DIAMOND_LEGGINGS).addEnchant(Enchantment.PROTECTION_ENVIRONMENTAL, 1).toItemStack(),
            null

    };

    public ItemStack[] getSwords() {
        return this.swords;
    }

    public ItemStack[] getHelmets() {
        return this.helmets;
    }

    public ItemStack[] getChestplates() {
        return this.chestplates;
    }

    public ItemStack[] getLeggins() {
        return this.leggins;
    }

    public ItemStack[] getShoes() {
        return this.shoes;
    }
}
