package de.fel1x.teamcrimx.mlgwars.kit.kits;

import de.fel1x.teamcrimx.crimxapi.utils.ItemBuilder;
import de.fel1x.teamcrimx.mlgwars.kit.IKit;
import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.PotionMeta;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class GhostKit implements IKit {

    @Override
    public String getKitName() {
        return "Geist";
    }

    @Override
    public String[] getKitDescription() {
        return new String[] {
                "§7Es wird gruselig!", "§7Laufe unsichtbar mit deiner Sense durch die Gegend!", ""
        };
    }

    @Override
    public int getKitCost() {
        return 3500;
    }

    @Override
    public Material getKitMaterial() {
        return Material.QUARTZ;
    }

    @Override
    public void setKitInventory(Player player) {
        ItemStack hoe = new ItemBuilder(Material.IRON_HOE)
                .addEnchant(Enchantment.KNOCKBACK, 2)
                .addEnchant(Enchantment.DURABILITY, 10)
                .setName("§7Sense")
                .toItemStack();

        player.getInventory().setItem(0, hoe);

        ItemStack potion = new ItemStack(Material.POTION);
        PotionMeta potionMeta = (PotionMeta) potion.getItemMeta();
        potionMeta.setMainEffect(PotionEffectType.INVISIBILITY);
        potionMeta.addCustomEffect(new PotionEffect(PotionEffectType.INVISIBILITY, 20 * 30, 1, true, true), true);
        potionMeta.setDisplayName("§7Trank der §f§lUnsichtbarkeit §8» §e30 Sekunden");
        potion.setItemMeta(potionMeta);

        player.getInventory().setItem(1, potion);

        potionMeta.clearCustomEffects();
        potionMeta.setMainEffect(PotionEffectType.INVISIBILITY);
        potionMeta.addCustomEffect(new PotionEffect(PotionEffectType.INVISIBILITY, 20 * 15, 1, true, true), true);
        potionMeta.setDisplayName("§7Trank der §f§lUnsichtbarkeit §8» §e15 Sekunden");
        potion.setItemMeta(potionMeta);

        player.getInventory().setItem(2, potion);

        potion = new ItemStack(Material.POTION);
        potionMeta.clearCustomEffects();
        potionMeta.setMainEffect(PotionEffectType.SPEED);
        potionMeta.addCustomEffect(new PotionEffect(PotionEffectType.SPEED, 20 * 45, 1, true, true), true);
        potionMeta.setDisplayName("§7Trank der §b§lGeschwindigkeit §8» §e45 Sekunden");
        potion.setItemMeta(potionMeta);

        player.getInventory().setItem(3, potion);

        ItemStack chestplate = new ItemBuilder(Material.LEATHER_CHESTPLATE)
                .setLeatherArmorColor(Color.WHITE).toItemStack();
        ItemStack block = new ItemBuilder(Material.QUARTZ_BLOCK, 64).toItemStack();

        player.getInventory().setItem(8, block);
        player.getInventory().setChestplate(chestplate);
    }
}
