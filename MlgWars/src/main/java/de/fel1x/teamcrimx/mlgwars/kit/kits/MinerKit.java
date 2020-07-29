package de.fel1x.teamcrimx.mlgwars.kit.kits;

import de.fel1x.teamcrimx.crimxapi.utils.ItemBuilder;
import de.fel1x.teamcrimx.mlgwars.kit.IKit;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class MinerKit implements IKit {

    @Override
    public String getKitName() {
        return "Miner";
    }

    @Override
    public String[] getKitDescription() {
        return new String[] {
                "§7Ab in den Stollen mit dir", "", "§d§lGLÜCK AUF!"
        };
    }

    @Override
    public int getKitCost() {
        return 2000;
    }

    @Override
    public Material getKitMaterial() {
        return Material.DIAMOND_PICKAXE;
    }

    @Override
    public void setKitInventory(Player player) {
        player.getInventory().addItem(new ItemBuilder(Material.DIAMOND_PICKAXE)
                .setName("§8● §dSpitzhacke des Untergangs")
                .addEnchant(Enchantment.DIG_SPEED, 10)
                .setUnbreakable()
                .toItemStack(),

                new ItemBuilder(Material.DIAMOND_AXE)
                        .setName("§8● §aAxt des Untergangs")
                        .addEnchant(Enchantment.DIG_SPEED, 10)
                        .setUnbreakable()
                        .toItemStack(),

                new ItemBuilder(Material.DIAMOND_SPADE)
                        .setName("§8● §eSchaufel des Untergangs")
                        .addEnchant(Enchantment.DIG_SPEED, 10)
                        .setUnbreakable()
                        .toItemStack());
    }
}
