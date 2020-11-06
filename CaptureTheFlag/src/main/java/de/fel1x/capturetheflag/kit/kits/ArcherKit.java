package de.fel1x.capturetheflag.kit.kits;

import de.fel1x.capturetheflag.gameplayer.GamePlayer;
import de.fel1x.capturetheflag.kit.IKit;
import de.fel1x.capturetheflag.team.Team;
import de.fel1x.teamcrimx.crimxapi.utils.ItemBuilder;
import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class ArcherKit implements IKit {
    @Override
    public String getKitName() {
        return "Archer";
    }

    @Override
    public String[] getKitDescription() {
        return new String[]{
                "§aVorteile:",
                " §8» §eHolzschwert (Sharp 1)",
                " §8» §eBogen (Stärke 1 / Infinity)",
                " §8» §eFull Leather"
        };
    }

    @Override
    public int getKitCost() {
        return 0;
    }

    @Override
    public Material getKitMaterial() {
        return Material.BOW;
    }

    @Override
    public void setKitInventory(Player player) {
        Color dyeColor = new GamePlayer(player).getTeam() == Team.RED ? Color.RED : Color.BLUE;

        ItemStack helmet = new ItemBuilder(Material.LEATHER_HELMET).setLeatherArmorColor(dyeColor).toItemStack();
        ItemStack chestplate = new ItemBuilder(Material.LEATHER_CHESTPLATE).setLeatherArmorColor(dyeColor).toItemStack();
        ItemStack leggins = new ItemBuilder(Material.LEATHER_LEGGINGS).setLeatherArmorColor(dyeColor).toItemStack();
        ItemStack boots = new ItemBuilder(Material.LEATHER_BOOTS).setLeatherArmorColor(dyeColor).toItemStack();

        ItemStack sword = new ItemBuilder(Material.WOODEN_SWORD).addEnchant(Enchantment.DAMAGE_ALL, 1).toItemStack();
        ItemStack bow = new ItemBuilder(Material.BOW).addEnchant(Enchantment.ARROW_INFINITE, 1).addEnchant(Enchantment.ARROW_DAMAGE, 1).toItemStack();
        ItemStack arrow = new ItemStack(Material.ARROW, 1);

        player.getInventory().addItem(sword, bow);
        player.getInventory().setItem(8, arrow);

        player.getInventory().setHelmet(helmet);
        player.getInventory().setChestplate(chestplate);
        player.getInventory().setLeggings(leggins);
        player.getInventory().setBoots(boots);
    }
}
